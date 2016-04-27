package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Gallery;
import com.huotu.hotcms.service.model.thymeleaf.foreach.PageableForeachParam;
import com.huotu.hotcms.service.repository.GalleryRepository;
import com.huotu.hotcms.service.service.CategoryService;
import com.huotu.hotcms.service.service.GalleryService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by chendeyu on 2016/1/10.
 */
@Service
public class GalleryServiceImpl implements GalleryService {

    private static Log log = LogFactory.getLog(GalleryServiceImpl.class);

    @Autowired
    private GalleryRepository galleryRepository;


    @Autowired
    private CategoryService categoryService;

    @Override
    public Boolean saveGallery(Gallery gallery) {
        galleryRepository.save(gallery);
        return true;
    }

    @Override
    public Gallery findById(Long id) {
        Gallery gallery = galleryRepository.findOne(id);
        return gallery;
    }

    @Override
    public List<Gallery> getSpecifyGallerys(String[] specifyIds) {
        List<String> ids = Arrays.asList(specifyIds);
        List<Long> noticeIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
        Specification<Gallery> specification = (root, query, cb) -> {
            List<Predicate> predicates = noticeIds.stream().map(id -> cb.equal(root.get("id").as(Long.class), id)).collect(Collectors.toList());
            return cb.or(predicates.toArray(new Predicate[predicates.size()]));
        };
        return galleryRepository.findAll(specification, new Sort(Sort.Direction.DESC, "orderWeight"));
    }


    @Override
    public Page<Gallery> getGalleryList(PageableForeachParam galleryForeachParam) throws Exception {
        int pageIndex = galleryForeachParam.getPageNo() - 1;
        int pageSize = galleryForeachParam.getPageSize();
        Sort sort = new Sort(Sort.Direction.DESC, "orderWeight");
        if (!StringUtils.isEmpty(galleryForeachParam.getSpecifyIds())) {
            return getSpecifyGallerys(galleryForeachParam.getSpecifyIds(), pageIndex, pageSize, sort);
        }
        if (!StringUtils.isEmpty(galleryForeachParam.getCategoryId())) {
            return getGalleries(galleryForeachParam, pageIndex, pageSize, sort);
        } else {
            return getAllGallery(galleryForeachParam, pageIndex, pageSize, sort);
        }
    }

    private Page<Gallery> getAllGallery(PageableForeachParam params, int pageIndex, int pageSize, Sort sort) {
        List<Category> subCategories = categoryService.getSubCategories(params.getParentcId());
        if (subCategories.size() == 0) {
            try {
                throw new Exception("父栏目节点没有子栏目");
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        Specification<Gallery> specification = (root, criteriaQuery, cb) -> {
            List<Predicate> p1 = new ArrayList<>();
            for (Category category : subCategories) {
                p1.add(cb.equal(root.get("category").as(Category.class), category));
            }
            Predicate predicate = cb.or(p1.toArray(new Predicate[p1.size()]));
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(params.getExcludeIds())) {
                List<String> ids = Arrays.asList(params.getExcludeIds());
                List<Long> galleryIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
                predicates = galleryIds.stream().map(id -> cb.notEqual(root.get("id").as(Long.class), id)).collect(Collectors.toList());
            }
            predicates.add(cb.equal(root.get("deleted").as(Boolean.class), false));
            predicates.add(predicate);
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        return galleryRepository.findAll(specification, new PageRequest(pageIndex, pageSize, sort));
    }

    private Page<Gallery> getGalleries(PageableForeachParam params, int pageIndex, int pageSize, Sort sort) throws Exception {
        try {
            Specification<Gallery> specification = (root, criteriaQuery, cb) -> {
                List<Predicate> predicates = new ArrayList<>();
                if (!StringUtils.isEmpty(params.getExcludeIds())) {
                    List<String> ids = Arrays.asList(params.getExcludeIds());
                    List<Long> galleryIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
                    predicates = galleryIds.stream().map(id -> cb.notEqual(root.get("id").as(Long.class), id)).collect(Collectors.toList());
                }
                predicates.add(cb.equal(root.get("deleted").as(Boolean.class), false));
                predicates.add(cb.equal(root.get("category").get("id").as(Long.class), params.getCategoryId()));
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            };
            return galleryRepository.findAll(specification, new PageRequest(pageIndex, pageSize, sort));
        } catch (Exception ex) {
            throw new Exception("获得图库列表出现错误");
        }
    }

    private Page<Gallery> getSpecifyGallerys(String[] specifyIds, int pageIndex, int pageSize, Sort sort) {
        List<String> ids = Arrays.asList(specifyIds);
        List<Long> galleryIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
        Specification<Gallery> specification = (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = galleryIds.stream().map(id -> cb.equal(root.get("id").as(Long.class), id)).collect(Collectors.toList());
            return cb.or(predicates.toArray(new Predicate[predicates.size()]));
        };
        return galleryRepository.findAll(specification, new PageRequest(pageIndex, pageSize, sort));
    }


}
