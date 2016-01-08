package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.common.ModelType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.CategoryTreeModel;
import com.huotu.hotcms.service.model.thymeleaf.CategoryForeachParam;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by chendeyu on 2015/12/31.
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(Long id) {
       Category category= categoryRepository.getOne(id);
        return  category;
    }

    @Override
    public Boolean save(Category category) {
        categoryRepository.save(category);
        return true;
    }

    @Override
    public List<Category> getCategoryBySiteAndDeleted(Site site, Boolean deleted) {
        List<Category> categories = categoryRepository.findBySiteAndDeletedOrderByOrderWeightDesc(site, deleted);
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
    public List<Category> getCategoryList(CategoryForeachParam foreachParam) {
        if(!StringUtils.isEmpty(foreachParam.getSpecifyids())) {
            List<String> ids = Arrays.asList(foreachParam.getSpecifyids());
            List<Long> categoryIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
            Specification<Category> specification = (root, query, cb) -> {
                List<Predicate> predicates = categoryIds.stream().map(id -> cb.equal(root.get("id").as(Long.class), id)).collect(Collectors.toList());
                return cb.or(predicates.toArray(new Predicate[predicates.size()]));
            };
            return categoryRepository.findAll(specification,new Sort(Sort.Direction.DESC,"orderWeight"));
        }

        Integer categoryType = foreachParam.getType();

        if(!StringUtils.isEmpty(foreachParam.getExcludeid())) {
            List<String> ids = Arrays.asList(foreachParam.getExcludeid());
            List<Long> categoryIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
            Specification<Category> specification = (root, query, cb) -> {
                List<Predicate> predicates = categoryIds.stream().map(id -> cb.notEqual(root.get("id").as(Long.class), id)).collect(Collectors.toList());
                predicates.add(cb.equal(root.get("site").get("siteId").as(Long.class),foreachParam.getSiteid()));
                predicates.add(cb.equal(root.get("modelType").as(Integer.class),categoryType));
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            };
            return categoryRepository.findAll(specification, new Sort(Sort.Direction.DESC, "orderWeight"));
        }
        return categoryRepository.findBySite_SiteIdAndDeletedAndModelTypeOrderByOrderWeightDesc(foreachParam.getSiteid(), false, categoryType);
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

    public boolean isExistsCategory(List<CategoryTreeModel> categoryTreeModelList,Category category) {
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
//                    return insertCategoryTreeById(categoryTreeModel.getChildren(), id, categoryTree);
//                    if (categoryTreeModelList1 != null) {
//                        return categoryTreeModelList1;
//                    } else {
//                        return insertCategoryTreeById(categoryTreeModel.getChildren(), id, categoryTree);
//                    }
                }
            }
        }
        return null;
    }
}
