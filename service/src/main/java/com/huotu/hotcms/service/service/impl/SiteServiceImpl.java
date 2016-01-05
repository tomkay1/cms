package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.repository.HostRepository;
import com.huotu.hotcms.service.repository.SiteRepository;
import com.huotu.hotcms.service.service.SiteService;
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
 * Created by chendeyu on 2015/12/24.
 */
@Service
public class SiteServiceImpl implements SiteService {

    @Autowired
    SiteRepository siteRepository ;

    @Autowired
    HostRepository hostRepository;


    @Override
    public PageData<Site> getPage(Integer customerId,String name, int page, int pageSize) {
        PageData<Site> data = null;
        Specification<Site> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(name)) {
                predicates.add(cb.like(root.get("name").as(String.class), "%" + name + "%"));
            }
            predicates.add(cb.equal(root.get("deleted").as(String.class), false));
            predicates.add(cb.equal(root.get("customerId").as(Integer.class), customerId));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        Page<Site> pageData = siteRepository.findAll(specification,new PageRequest(page - 1, pageSize));
        if (pageData != null) {
            List<Site> site =pageData.getContent();
            for(Site site1 : site){
                site1.setHosts(null);
            }
            data = new PageData<Site>();
            data.setPageCount(pageData.getTotalPages());
            data.setPageIndex(pageData.getNumber());
            data.setPageSize(pageData.getSize());
            data.setTotal(pageData.getTotalElements());
            data.setRows((Site[])pageData.getContent().toArray(new Site[pageData.getContent().size()]));
        }
        return  data;
    }

    @Override
    public Site findBySiteIdAndCustomerId(Long siteId,int customerId) {
        Site site =siteRepository.findBySiteIdAndCustomerId(siteId,customerId);
        return  site;
    }

    @Override
    public Set<Site> getSite(int customerId) {
        return siteRepository.findByCustomerId(customerId);
    }

    @Override
    public boolean deleteSite(Long id) {
        return false;
    }


    @Override
    public Site getSite(long siteId) {
        return siteRepository.findOne(siteId);
    }

    @Override
    public Boolean save(Site site) {
        siteRepository.save(site);
        return true;
    }


}
