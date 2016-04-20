package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.common.RouteType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Route;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.CategoryTreeModel;
import com.huotu.hotcms.service.model.thymeleaf.foreach.CategoryForeachParam;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.service.CategoryService;
import com.huotu.hotcms.service.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
 * Created by xhl on 2015/12/31.
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
                category.setSite(null);
                category=setReleationEmpty(category);
            }
        }
        return categories;
    }

    public Category setReleationEmpty(Category category) {
        if (category != null) {
            Category parentCategory = category.getParent();
            if(parentCategory!=null) {
                parentCategory.setSite(null);
//                parentCategory.setRoute(null);
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
    public List<Category> findByRouteTypeAndParentId(CategoryForeachParam param) {
        Category parent = categoryRepository.findOne(param.getParentid());
        Sort sort = new Sort(Sort.Direction.DESC, "orderWeight");
        Specification<Category> specification = (root, query, cb) -> {
            List<Predicate> predicates;
            if(!StringUtils.isEmpty(param.getExcludeids())) {
                List<String> ids = Arrays.asList(param.getExcludeids());
                List<Long> categoryIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
                predicates = categoryIds.stream().map(id -> cb.notEqual(root.get("id").as(Long.class), id)).collect(Collectors.toList());
            }else {
                predicates = new ArrayList<>();
            }
            predicates.add(cb.equal(root.get("site").get("siteId").as(Long.class),param.getSiteid()));
            predicates.add(cb.equal(root.get("route").get("routeType").as(RouteType.class), param.getRoutetype()));
            predicates.add(cb.equal(root.get("deleted").as(Boolean.class),false));
            predicates.add(cb.equal(root.get("parent").as(Category.class), parent));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        return categoryRepository.findAll(specification,new PageRequest(0,param.getSize(),sort)).getContent();
    }

    @Override
    public List<Category> getHeaderCategoryList(CategoryForeachParam param) {
        return categoryRepository.findBySite_SiteIdAndRoute_RouteTypeAndDeletedOrderByOrderWeightDesc(param.getSiteid(), RouteType.HEADER_NAVIGATION, false);
    }


    @Override
    public List<Category> getGivenTypeCategories(CategoryForeachParam param) {
        int requestSize = param.getSize();
        Category parent = param.getParentid()==null ? null : categoryRepository.findOne(param.getParentid());
        if(!StringUtils.isEmpty(param.getExcludeids())) {
            Sort sort = new Sort(Sort.Direction.DESC, "orderWeight");
            List<String> ids = Arrays.asList(param.getExcludeids());
            List<Long> categoryIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
            Specification<Category> specification = (root, query, cb) -> {
                List<Predicate> predicates = categoryIds.stream().map(id -> cb.notEqual(root.get("id").as(Long.class), id)).collect(Collectors.toList());
                predicates.add(cb.equal(root.get("site").get("siteId").as(Long.class),param.getSiteid()));
                RouteType routeType = param.getRoutetype()==null?RouteType.HEADER_NAVIGATION:param.getRoutetype();
                predicates.add(cb.equal(root.get("route").get("routeType").as(Integer.class),routeType));
                if(param.getParentid()!=null) {
                    predicates.add(cb.equal(root.get("parent").as(Category.class), parent));
                }
                predicates.add(cb.equal(root.get("deleted").as(Boolean.class),false));
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            };
            return categoryRepository.findAll(specification,new PageRequest(0,requestSize,sort)).getContent();
        }
        List<Category> categoryList = categoryRepository.findBySite_SiteIdAndRoute_RouteTypeAndDeletedAndParentOrderByOrderWeightDesc(param.getSiteid(), param.getRoutetype(), false, parent);
        int origionSize = categoryList.size();
        if(requestSize > origionSize - 1) {
            requestSize = origionSize;
        }
        return categoryList.subList(0,requestSize);
    }

    @Override
    public List<Category> getSubCategories(Long parenId,int size) {
        List<Category> categoryList = categoryRepository.findByParent_Id(parenId);
        int origionSize = categoryList.size();
        if(size > origionSize - 1) {
            size = origionSize;
        }
        return categoryList.subList(0,size);
    }

    @Override
    public List<Category> getSubCategories(Long parenId) {
        return categoryRepository.findByParent_Id(parenId);
    }


    @Override
    public List<CategoryTreeModel> ConvertCategoryTreeByCategory(List<Category> categories) {
        List<CategoryTreeModel> categoryTreeModels=new ArrayList<CategoryTreeModel>();
        if(categories!=null&&categories.size()>0){
            for(Category category : categories){
                if(category!=null){
                    if(category.getParent()==null){//父节点为空时
                        if(!isExistsCategory(categoryTreeModels,category)){
                            categoryTreeModels.add(CategoryTreeModel.ConvertToCategoryTreeModel(category));
                        }
                    }else{//父节点不为空
                        Long index=getCategoryTreeId(categoryTreeModels, category.getParent());
                        if(index!=null){//父节点存在则把该节点加入到父节点下面
                            List<CategoryTreeModel> categoryTreeModels1 =insertCategoryTreeById(categoryTreeModels, index, CategoryTreeModel.ConvertToCategoryTreeModel(category));
                            if(categoryTreeModels1!=null){
                                categoryTreeModels=categoryTreeModels1;
                            }
                        }else {
                            CategoryTreeModel categoryTreeModel = CategoryTreeModel.ConvertToCategoryTreeModel(category.getParent());
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
    public Boolean saveCategoryAndRoute(Category category, String rule,String template,RouteType routeType) throws Exception{
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
                route1.setRouteType(routeType);
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
    public Boolean updateCategoryAndRoute(Category category, String rule, String template,String noRule,RouteType routeType) {
        if(!routeService.isPatterBySiteAndRuleIgnore(category.getSite(), rule, noRule)) {
            Route route1 =category.getRoute();
            if(route1!=null) {
                if(!StringUtils.isEmpty(rule)) {
                    route1.setRule(rule);
                    route1.setSite(category.getSite());
                    route1.setTemplate(template);
                    route1.setCreateTime(LocalDateTime.now());
                    route1.setCustomerId(category.getCustomerId());
                    route1.setDeleted(false);
                    route1.setOrderWeight(50);
                    route1.setRouteType(routeType);
                    route1.setUpdateTime(LocalDateTime.now());
                    routeService.save(route1);
                    save(category);
                }else{
                    category.setRoute(null);
                    routeService.delete(route1);
                    save(category);
                }
            }else{
                if(!StringUtils.isEmpty(rule)) {
                    route1 = new Route();
                    route1.setDescription(category.getName() + "路由");
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
                    save(category);
                }else{
                    category.setRoute(null);
                    save(category);
                }
            }
        }
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

    @Override
    public List<Category> findByParentIdsLike(String parentId) {
        List<Category> categoryList=new ArrayList<>();
        List<Category> categories=categoryRepository.findByParentIdsContainingAndDeleted(parentId, false);
        for(Category category:categories){
            if(category!=null){
                if(category!=null){
                    if(category.getModelId()!=null&&category.getModelId()<=5) {
                        categoryList.add(category);
                    }
                }
            }
        }
        return categoryList;
    }

    public String getCategoryParentIds(Long parentId){
        String parentIds="";
        List<Category> categories=categoryRepository.findByParentIdsContainingAndDeleted(parentId.toString(),false);
        if(categories!=null){
            for(Category category:categories){
                if(category!=null){
                    if(category.getModelId()!=null&&category.getModelId()<=5) {
                        parentIds += category.getId() + ",";
                    }
                }
            }
        }
        if(!StringUtils.isEmpty(parentIds)){
            parentIds=parentIds.substring(0,parentIds.length()-1);
        }
        return parentIds;
    }
}
