package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.entity.CustomPages;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.repository.CustomPagesRepository;
import com.huotu.hotcms.service.service.CustomPagesService;
import com.huotu.hotcms.service.util.PageData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/18.
 */
@Service
public class CustomPagesServiceImpl implements CustomPagesService {
    private static final Log log = LogFactory.getLog(CustomPagesServiceImpl.class);

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
//            predicates.sort();
            query.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
            query.orderBy(cb.desc(root.get("createTime").as(LocalDateTime.class)));

            return query.getRestriction();
//            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
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

    @Override
    public CustomPages findHomePages() {
        List<CustomPages> customPages=customPagesRepository.findByHome(true);
        if(customPages!=null&&customPages.size()==1){
            return  customPages.get(0);
        }
        return null;
    }

    @Override
    public Boolean setHomePages(Long id) {
        Boolean flag=false;
        try {
            List<CustomPages> customPages = customPagesRepository.findByHome(true);
            for (CustomPages customPages1 : customPages) {
                customPages1.setHome(false);
                customPagesRepository.save(customPages1);
            }
            CustomPages customPages1 = customPagesRepository.findOne(id);
            if (customPages1 != null) {
                customPages1.setHome(true);
                customPagesRepository.saveAndFlush(customPages1);
                return true;
            }
        }catch (Exception ex){
            log.error(ex);
        }
        return false;
    }
}
