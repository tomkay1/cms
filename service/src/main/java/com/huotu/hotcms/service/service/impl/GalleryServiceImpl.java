package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.entity.Gallery;
import com.huotu.hotcms.service.entity.GalleryList;
import com.huotu.hotcms.service.repository.GalleryListRepository;
import com.huotu.hotcms.service.repository.GalleryRepository;
import com.huotu.hotcms.service.service.GalleryService;
import com.huotu.hotcms.service.widget.service.StaticResourceService;
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
public class GalleryServiceImpl implements GalleryService {

    @Autowired
    private GalleryRepository galleryRepository;
    @Autowired
    private GalleryListRepository galleryListRepository;

    private StaticResourceService resourceServer;

    @Override
    public Boolean saveGallery(Gallery gallery) {
        galleryRepository.save(gallery);
        return true;
    }

    @Override
    public Gallery findById(Long id) {
        Gallery gallery =  galleryRepository.findOne(id);
        return gallery;
    }

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
}
