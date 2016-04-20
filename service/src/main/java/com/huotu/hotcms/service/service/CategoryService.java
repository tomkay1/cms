package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.common.RouteType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Route;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.CategoryTreeModel;
import com.huotu.hotcms.service.model.thymeleaf.foreach.CategoryForeachParam;

import java.util.List;

/**
 * Created by chendeyu on 2015/12/31.
 */
public interface CategoryService {
    Category getCategoryById(Long id);

    Boolean save(Category category);

    List<Category>  getCategoryBySiteAndDeletedAndNameContainingOrderByOrderWeightDesc(Site site,Boolean deleted,String name);

    /**
     * 根据栏目获取栏目树
     */
    List<CategoryTreeModel> ConvertCategoryTreeByCategory(List<Category> categories);

    Boolean saveCategoryAndRoute(Category category,String route,String template,RouteType routeType) throws Exception;

    Boolean updateCategoryAndRoute(Category category, String rule, String template,String noRule,RouteType routeType);

    Boolean deleteCategory(Category category);

    Category getCategoryByRoute(Route route);

    Boolean CategorySetParents(Category category);

    List<Category> getSpecifyCategories(String[] specifyIds);

    List<Category> getGivenTypeCategories(CategoryForeachParam param);

    List<Category> getSubCategories(Long parenId,int size);

    List<Category> getSubCategories(Long parenId);

    List<Category> getCategoryList(Category parent);

    List<Category> findByRouteTypeAndParentId(CategoryForeachParam categoryForeachParam);

    List<Category> getHeaderCategoryList(CategoryForeachParam param);

    List<Category> findByParentIdsLike(String parentId);

    String getCategoryParentIds(Long parentId);
}
