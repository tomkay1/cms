/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.entity.GalleryItem;
import com.huotu.hotcms.service.model.thymeleaf.foreach.GalleryForeachParam;
import com.huotu.hotcms.service.repository.GalleryItemRepository;
import com.huotu.hotcms.service.service.GalleryItemService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chendeyu on 2016/1/10.
 */
@Service
public class GalleryItemServiceImpl implements GalleryItemService {

    private static Log log = LogFactory.getLog(GalleryItemServiceImpl.class);

    @Autowired
    private GalleryItemRepository galleryItemRepository;


    @Override
    public Page<GalleryItem> getPage(long ownerId, Long galleryId, int page, int pageSize) throws URISyntaxException {
        Specification<GalleryItem> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("deleted").as(String.class), false));
            predicates.add(cb.equal(root.get("site").get("owner").get("id").as(Long.class), ownerId));
            predicates.add(cb.equal(root.get("gallery").get("id").as(Long.class), galleryId));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        return galleryItemRepository.findAll(specification, new PageRequest(page - 1, pageSize
                , new Sort(Sort.Direction.DESC, "orderWeight")));
    }


    @Override
    public Boolean saveGalleryItem(GalleryItem galleryItem) {
        galleryItemRepository.save(galleryItem);
        return true;
    }

    @Override
    public GalleryItem findGalleryItemById(Long id) {
        return galleryItemRepository.findOne(id);
    }

    @Override
    public Page<GalleryItem> getGalleryItem(GalleryForeachParam foreachParam) throws Exception {
        Specification<GalleryItem> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("deleted").as(String.class), false));
            predicates.add(cb.equal(root.get("gallery").get("id").as(Long.class), foreachParam.getGalleryId()));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        return galleryItemRepository.findAll(specification, new PageRequest(foreachParam.getPageNo() - 1
                , foreachParam.getPageSize(), new Sort(Sort.Direction.DESC, "orderWeight")));
    }
}
