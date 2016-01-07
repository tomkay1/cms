package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.thymeleaf.CategoryForeachParam;
import com.huotu.hotcms.service.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

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
        return categories;
    }

    @Override
    public List<Category> getCategoryList(CategoryForeachParam foreachParam) {
        List<Category> categories = categoryRepository.findBySite_SiteIdAndDeletedOrderByOrderWeightDesc(Long.parseLong(foreachParam.getSiteid()), false);
        for(Iterator<Category> it = categories.iterator();it.hasNext();) {
            Category category = it.next();
            if(category.getId().toString().equals(foreachParam.getExcludeid())) {
                it.remove();
            }
        }
        return categories;
    }

}
