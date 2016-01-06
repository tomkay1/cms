package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;

import java.util.Set;

/**
 * Created by chendeyu on 2015/12/31.
 */
public interface CategoryService {
    Category getCategoryById(Long id);
    Boolean save(Category category);
    Set<Category>  getCategoryBySiteAndDeleted(Site site,Boolean deleted);
}
