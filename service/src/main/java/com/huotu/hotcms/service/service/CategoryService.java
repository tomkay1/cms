package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Route;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.CategoryTreeModel;
import com.huotu.hotcms.service.model.thymeleaf.CategoryForeachParam;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by chendeyu on 2015/12/31.
 */
public interface CategoryService {
    Category getCategoryById(Long id);
    Boolean save(Category category);
    List<Category>  getCategoryBySiteAndDeletedAndNameContainingOrderByOrderWeightDesc(Site site,Boolean deleted,String name);
    List<Category> getCategoryList(CategoryForeachParam foreachParam);

    List<CategoryTreeModel> ConvertCateGoryTreeByCategotry(List<Category> categories);

    Boolean saveCategoryAndRoute(Category category,String route,String template);

    Boolean updateCategoryAndRoute(Category category, String rule, String template,String noRule);

    Boolean deleteCategory(Category category);

    Category getCategoryByRoute(Route route);

    List<Category> getCategoryList(Category parent);
}
