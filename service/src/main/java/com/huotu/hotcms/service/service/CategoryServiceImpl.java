package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

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
    public Set<Category> getCategoryBySiteAndDeleted(Site site, Boolean deleted) {
        Set<Category> categories = categoryRepository.findBySiteAndDeleted(site,deleted);
        return categories;
    }


}
