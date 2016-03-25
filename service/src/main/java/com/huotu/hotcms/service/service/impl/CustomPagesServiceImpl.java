package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.entity.CustomPages;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.repository.CustomPagesRepository;
import com.huotu.hotcms.service.service.CustomPagesService;
import com.huotu.hotcms.service.util.PageData;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/18.
 */
@Service
public class CustomPagesServiceImpl implements CustomPagesService {
    @Autowired
    private CustomPagesRepository customPagesRepository;

    @Override
    public PageData<CustomPages> getPage(String name,Long siteId,boolean delete,boolean publish,int page,int pageSize) {
        PageData<CustomPages> data = null;
        Specification<CustomPages> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(name)) {
                predicates.add(cb.like(root.get("name").as(String.class), "%" + name + "%"));
            }
            predicates.add(cb.equal(root.get("deleted").as(Boolean.class), delete));
            predicates.add(cb.equal(root.get("publish").as(Boolean.class), publish));
            predicates.add(cb.equal(root.get("site").get("siteId").as(Integer.class), siteId));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        Page<CustomPages> pageData = customPagesRepository.findAll(specification,new PageRequest(page - 1, pageSize));
        if (pageData != null) {
            List<CustomPages> customPages =pageData.getContent();
            for(CustomPages customPages1 : customPages){
                customPages1.setSite(null);
            }
            data = new PageData<CustomPages>();
            data.setPageCount(pageData.getTotalPages());
            data.setPageIndex(pageData.getNumber());
            data.setPageSize(pageData.getSize());
            data.setTotal(pageData.getTotalElements());
            data.setRows((CustomPages[])pageData.getContent().toArray(new CustomPages[pageData.getContent().size()]));
        }
        return  data;
    }

    @Override
    public CustomPages getCustomPages(long id) {
      return customPagesRepository.findOne(id);
    }

    @Override
    public CustomPages save(CustomPages customPages) {
        return customPagesRepository.save(customPages);
    }
}
