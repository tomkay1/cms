package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.thymeleaf.CategoryForeachParam;
import com.huotu.hotcms.service.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
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
        return categories;
    }

    @Override
    public List<Category> getCategoryList(CategoryForeachParam foreachParam) {
        if(!StringUtils.isEmpty(foreachParam.getSpecifyids())) {
            String[] specifyIds = StringUtils.split(foreachParam.getSpecifyids(), ",");
            List<String> ids = Arrays.asList(specifyIds);
            Collection<Long> categoryIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
            return categoryRepository.findByIdInAndDeletedOrderByOrderWeightDesc(categoryIds, false);
        }
        return categoryRepository.findBySite_SiteIdAndDeletedAndIdNotOrderByOrderWeightDesc(Long.parseLong(foreachParam.getSiteid()), false,Long.parseLong(foreachParam.getExcludeid()));
    }



}
