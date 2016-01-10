package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.CategoryTreeModel;
import com.huotu.hotcms.service.model.thymeleaf.CategoryForeachParam;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by chendeyu on 2015/12/31.
 */
public interface CategoryService {
    Category getCategoryById(Long id);
    Boolean save(Category category);
    List<Category>  getCategoryBySiteAndDeleted(Site site,Boolean deleted);
    List<Category> getCategoryList(CategoryForeachParam foreachParam);

    List<CategoryTreeModel> ConvertCateGoryTreeByCategotry(List<Category> categories);

    Boolean saveCategoryAndRoute(Category category,String route,String template);

    Boolean updateCategoryAndRoute(Category category, String rule, String template,String noRule);
}
