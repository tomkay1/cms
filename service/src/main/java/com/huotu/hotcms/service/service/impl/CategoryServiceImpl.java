package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.thymeleaf.CategoryForeachParam;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
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
            List<String> ids = Arrays.asList(foreachParam.getSpecifyids().split(","));
            List<Long> categoryIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
            Specification<Category> specification = (root, query, cb) -> {
                List<Predicate> predicates = categoryIds.stream().map(id -> cb.equal(root.get("id").as(Long.class), id)).collect(Collectors.toList());
                return cb.or(predicates.toArray(new Predicate[predicates.size()]));
            };
            return categoryRepository.findAll(specification);
        }
        return categoryRepository.findBySite_SiteIdAndDeletedAndIdNotOrderByOrderWeightDesc(Long.parseLong(foreachParam.getSiteid()), false,Long.parseLong(foreachParam.getExcludeid()));
    }



}
