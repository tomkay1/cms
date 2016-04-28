package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.entity.GalleryList;
import com.huotu.hotcms.service.model.thymeleaf.foreach.GalleryForeachParam;
import com.huotu.hotcms.service.model.thymeleaf.foreach.PageableForeachParam;
import com.huotu.hotcms.service.repository.GalleryListRepository;
import com.huotu.hotcms.service.service.GalleryListService;
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
public class GalleryListServiceImpl implements GalleryListService {

    private static Log log = LogFactory.getLog(GalleryListServiceImpl.class);

    @Autowired
    private GalleryListRepository galleryListRepository;



    @Override
    public Page<GalleryList> getPage(Integer customerId, Long galleryId, int page, int pageSize) throws URISyntaxException {
        Specification<GalleryList> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("deleted").as(String.class), false));
            predicates.add(cb.equal(root.get("customerId").as(Integer.class), customerId));
            predicates.add(cb.equal(root.get("gallery").get("id").as(Long.class), galleryId));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        Page<GalleryList> pageData = galleryListRepository.findAll(specification,new PageRequest(page - 1, pageSize,new Sort(Sort.Direction.DESC,"orderWeight")));
        return  pageData;
    }


    @Override
    public Boolean saveGalleryList(GalleryList galleryList) {
        galleryListRepository.save(galleryList);
        return true;
    }

    @Override
    public GalleryList findGalleryListById(Long id) {
        return galleryListRepository.findOne(id);
    }

    @Override
    public Page<GalleryList> getGalleryList(GalleryForeachParam foreachParam) throws Exception {
        Specification<GalleryList> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("deleted").as(String.class), false));
            predicates.add(cb.equal(root.get("gallery").get("id").as(Long.class), foreachParam.getGalleryId()));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        Page<GalleryList> pageData = galleryListRepository.findAll(specification,new PageRequest(foreachParam.getPageNo() - 1, foreachParam.getPageSize(),new Sort(Sort.Direction.DESC,"orderWeight")));
        return  pageData;
    }
}
