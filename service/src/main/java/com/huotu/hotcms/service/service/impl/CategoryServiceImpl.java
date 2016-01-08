package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.common.ModelType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.thymeleaf.CategoryForeachParam;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
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
            return categoryRepository.findAll(specification,new Sort(Sort.Direction.DESC,"orderWeight"));
        }

        final ModelType modelType = convertStringToEnum(foreachParam.getType());

        if(!StringUtils.isEmpty(foreachParam.getExcludeid())) {
            List<String> ids = Arrays.asList(foreachParam.getExcludeid().split(","));
            List<Long> categoryIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
            Specification<Category> specification = (root, query, cb) -> {
                List<Predicate> predicates = categoryIds.stream().map(id -> cb.notEqual(root.get("id").as(Long.class), id)).collect(Collectors.toList());
                predicates.add(cb.equal(root.get("site").get("siteId").as(Long.class),foreachParam.getSiteid()));
                predicates.add(cb.equal(root.get("modelType").as(ModelType.class),modelType));
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            };
            return categoryRepository.findAll(specification, new Sort(Sort.Direction.DESC, "orderWeight"));
        }
        return categoryRepository.findBySite_SiteIdAndDeletedAndModelTypeOrderByOrderWeightDesc(Long.parseLong(foreachParam.getSiteid()), false,modelType);
    }

    private ModelType convertStringToEnum(String common) {
        if(!StringUtils.isEmpty(common)) {
            switch (common) {
                case "0":
                    return ModelType.ARTICLE;
                case "1":
                    return ModelType.NOTICE;
                case "2":
                    return ModelType.VIDEO;
                case "3":
                    return ModelType.GALLERY;
                case "4":
                    return ModelType.DOWNLOAD;
                case "5":
                    return ModelType.LINK;
                default:
                    return null;
            }
        }
        return null;
    }


}
