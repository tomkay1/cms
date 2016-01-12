package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Route;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.CategoryTreeModel;
import com.huotu.hotcms.service.model.thymeleaf.CategoryForeachParam;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.service.CategoryService;
import com.huotu.hotcms.service.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by chendeyu on 2015/12/31.
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

//    @Autowired
//    CategoryService categoryService;

    @Autowired
    RouteService routeService;

    @Override
    public Category getCategoryById(Long id) {
        try {
            Category category = categoryRepository.getOne(id);
            return category;
        }catch (Exception ex){

        }
        return  null;
    }

    @Override
    public Boolean save(Category category) {
        categoryRepository.save(category);
        return true;
    }

    @Override
    public List<Category> getCategoryBySiteAndDeletedAndNameContainingOrderByOrderWeightDesc(Site site,Boolean deleted,String name) {
        List<Category> categories=null;
        if(StringUtils.isEmpty(name)){
            categories = categoryRepository.findBySiteAndDeletedOrderByOrderWeightDesc(site, deleted);
        }else{
            categories = categoryRepository.findBySiteAndDeletedAndNameContainingOrderByOrderWeightDesc(site,deleted,name);
        }
        for(Category category:categories){
            if(category!=null){
                category.setRoute(null);
                category.setSite(null);
                category=setReleationEmpty(category);
            }
        };
        return categories;
    }
    public Category setReleationEmpty(Category category) {
        if (category != null) {
            Category parentCategory = category.getParent();
            if(parentCategory!=null) {
                parentCategory.setSite(null);
                parentCategory.setRoute(null);
                category.setParent(parentCategory);
                if (parentCategory.getParent() != null) {
                    return setReleationEmpty(parentCategory.getParent());
                } else {
                    return category;
                }
            }
        }
        return category;
    }


    @Override
    public List<Category> getSpecifyCategories(String[] specifyIds) {
        List<String> ids = Arrays.asList(specifyIds);
        List<Long> categoryIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
        Specification<Category> specification = (root, query, cb) -> {
            List<Predicate> predicates = categoryIds.stream().map(id -> cb.equal(root.get("id").as(Long.class), id)).collect(Collectors.toList());
            return cb.or(predicates.toArray(new Predicate[predicates.size()]));
        };
        return categoryRepository.findAll(specification,new Sort(Sort.Direction.DESC,"orderWeight"));
    }

    @Override
    public List<Category> getGivenTypeCategories(CategoryForeachParam param) {
        if(!StringUtils.isEmpty(param.getExcludeid())) {
            List<String> ids = Arrays.asList(param.getExcludeid());
            List<Long> categoryIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
            Specification<Category> specification = (root, query, cb) -> {
                List<Predicate> predicates = categoryIds.stream().map(id -> cb.notEqual(root.get("id").as(Long.class), id)).collect(Collectors.toList());
                predicates.add(cb.equal(root.get("site").get("siteId").as(Long.class),param.getSiteid()));
                predicates.add(cb.equal(root.get("route").get("routeType").as(Integer.class),param.getRoutetype()));
                predicates.add(cb.equal(root.get("deleted").as(Boolean.class),0));
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            };
            return categoryRepository.findAll(specification, new Sort(Sort.Direction.DESC, "orderWeight"));
        }
        return categoryRepository.findBySite_SiteIdAndRoute_RouteTypeAndDeletedOrderByOrderWeightDesc(param.getSiteid(), param.getRoutetype(), false);
    }

    @Override
    public List<Category> getSubCategories(Long parenId) {
        return categoryRepository.findByParent_Id(parenId);
    }


    @Override
    public List<CategoryTreeModel> ConvertCateGoryTreeByCategotry(List<Category> categories) {
        List<CategoryTreeModel> categoryTreeModels=new ArrayList<CategoryTreeModel>();
        if(categories!=null&&categories.size()>0){
            for(Category category : categories){
                if(category!=null){
                    if(category.getParent()==null){//父节点为空时
                        if(!isExistsCategory(categoryTreeModels,category)){
                            categoryTreeModels.add(CategoryTreeModel.ConvertToCateboryTreeModel(category));
                        }
                    }else{//父节点不为空
                        Long index=getCategoryTreeId(categoryTreeModels, category.getParent());
                        if(index!=null){//父节点存在则把该节点加入到父节点下面
                            List<CategoryTreeModel> categoryTreeModels1 =insertCategoryTreeById(categoryTreeModels, index, CategoryTreeModel.ConvertToCateboryTreeModel(category));
                            if(categoryTreeModels1!=null){
                                categoryTreeModels=categoryTreeModels1;
                            }
                        }else {
                            CategoryTreeModel categoryTreeModel = CategoryTreeModel.ConvertToCateboryTreeModel(category.getParent());
                            categoryTreeModel.addChildren(category);
                            categoryTreeModels.add(categoryTreeModel);
                        }
                    }
                }
            }
        }
        return categoryTreeModels;
    }

    public Boolean isExistsCategory(List<CategoryTreeModel> categoryTreeModelList,Category category) {
        if(categoryTreeModelList!=null){
            for(CategoryTreeModel categoryTreeModel : categoryTreeModelList){
                if(categoryTreeModel!=null&&categoryTreeModel.getId().equals(category.getId())){
                    return true;
                }else{
                    boolean Flag=isExistsCategory(categoryTreeModel.getChildren(),category);
                    if(Flag) {
                        return Flag;
                    }else{
                        continue;
                    }
                }
            }
        }
        return false;
    }

    public Long getCategoryTreeId(List<CategoryTreeModel> categoryTreeModelList,Category category){
        if(categoryTreeModelList!=null){
            for(Integer i=0;i<categoryTreeModelList.size();i++){
                CategoryTreeModel categoryTreeModel=categoryTreeModelList.get(i);
                if(categoryTreeModel!=null&&categoryTreeModel.getId().equals(category.getId())){
                    return  categoryTreeModel.getId();
                }else{
                    Long id=getCategoryTreeId(categoryTreeModel.getChildren(),category);
                    if(id!=null){
                        return id;
                    }else{
                        continue;
                    }
                }
            }
        }
        return null;
    }

    public List<CategoryTreeModel> insertCategoryTreeById(List<CategoryTreeModel> categoryTreeModelList,Long id,CategoryTreeModel categoryTree){
        for(CategoryTreeModel categoryTreeModel : categoryTreeModelList) {
            if(categoryTreeModel!=null&&categoryTreeModel.getId().equals(id)){
                categoryTreeModel.addChildren(categoryTree);
                return categoryTreeModelList;
            }else {
                if(categoryTreeModel.getChildren()!=null) {
                    List<CategoryTreeModel> categoryTreeModelList1 = insertCategoryTreeById(categoryTreeModel.getChildren(), id, categoryTree);
                    if(categoryTreeModelList1==null) {
                        continue;
                    }else{
                        categoryTreeModel.setChildren(categoryTreeModelList1);
                        return  categoryTreeModelList;
                    }
                }
            }
        }
        return null;
    }


    @Override
    @Transactional
    public Boolean saveCategoryAndRoute(Category category, String rule,String template) {
        if(!routeService.isPatterBySiteAndRule(category.getSite(), rule)) {
            if(!StringUtils.isEmpty(rule)) {
                Route route1 = new Route();
                route1.setDescription(category.getName()+"路由");
                route1.setRule(rule);
                route1.setSite(category.getSite());
                route1.setTemplate(template);
                route1.setCreateTime(LocalDateTime.now());
                route1.setCustomerId(category.getCustomerId());
                route1.setDeleted(false);
                route1.setOrderWeight(50);
                route1.setUpdateTime(LocalDateTime.now());
                routeService.save(route1);
                category.setRoute(route1);
            }
        }
        Category category1= categoryRepository.saveAndFlush(category);
        CategorySetParents(category1);
        return true;
    }

    @Override
    @Transactional
    public Boolean updateCategoryAndRoute(Category category, String rule, String template,String noRule) {
        if(!routeService.isPatterBySiteAndRuleIgnore(category.getSite(), rule, noRule)) {
            Route route1 =category.getRoute();
            if(route1!=null) {
                route1.setRule(rule);
                route1.setSite(category.getSite());
                route1.setTemplate(template);
                route1.setCreateTime(LocalDateTime.now());
                route1.setCustomerId(category.getCustomerId());
                route1.setDeleted(false);
                route1.setOrderWeight(50);
                route1.setUpdateTime(LocalDateTime.now());
                routeService.save(route1);
            }else{
                route1 = new Route();
                route1.setDescription(category.getName()+"路由");
                route1.setRule(rule);
                route1.setSite(category.getSite());
                route1.setTemplate(template);
                route1.setCreateTime(LocalDateTime.now());
                route1.setCustomerId(category.getCustomerId());
                route1.setDeleted(false);
                route1.setOrderWeight(50);
                route1.setUpdateTime(LocalDateTime.now());
                routeService.save(route1);
                category.setRoute(route1);
            }
        }
        save(category);
        return true;
    }

    @Override
    @Transactional
    public Boolean deleteCategory(Category category) {
        category.setDeleted(true);
        Route route=category.getRoute();
        if(route!=null) {
            category.setRoute(null);
            save(category);
            routeService.delete(route);
        }else{
            save(category);
        }
        return true;
    }

    @Override
    public Category getCategoryByRoute(Route route) {
        return categoryRepository.findByRoute(route);
    }

    @Override
    public Boolean CategorySetParents(Category category) {
        Long categoryId=category.getId();
        if(categoryId!=null) {
            String categoryPath = category.getParentIds();
            if (StringUtils.isEmpty(categoryPath)) {
                category.setParentIds(categoryId.toString());
            } else {
                category.setParentIds(categoryPath + "|" + categoryId);
            }
            categoryRepository.saveAndFlush(category);
        }
        return true;
    }
    @Override
    public List<Category> getCategoryList(Category parent) {
        return categoryRepository.findByParentOrderByOrderWeightDesc(parent);
    }

}
