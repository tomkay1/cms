package com.huotu.hotcms.service.impl;

import com.huotu.hotcms.entity.Host;
import com.huotu.hotcms.entity.Site;
import com.huotu.hotcms.repository.HostRepository;
import com.huotu.hotcms.repository.SiteRepository;
import com.huotu.hotcms.service.SiteService;
import com.huotu.hotcms.util.PageData;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
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

    @Transactional(value = "transactionManager")
    @Override
    public boolean addSite(HttpServletRequest request,String... hosts) {
        Site site = new Site();
        site.setCustomerId(Integer.parseInt(request.getParameter("customerid")));
        site.setName(request.getParameter("name"));
        site.setTitle(request.getParameter("title"));
        site.setKeywords(request.getParameter("keywords"));
        site.setDescription(request.getParameter("description"));
        site.setCopyright(request.getParameter("copyright"));
        LocalDateTime localDateTime =LocalDateTime.now();
        site.setUpdateTime(localDateTime);
        String flag =request.getParameter("custom");
        if(flag.equals("1")){
            site.setCustom(true);
        }
        else {
            site.setCustom(false);
        }
        site.setCustomTemplateUrl(request.getParameter("customTemplateUrl"));

        for(String host:hosts) {
            Host host1 = hostRepository.findByDomain(host);
            if(host1==null){
                Host host2 = new Host();
                host2.setDomain(host);
                hostRepository.save(host2);
                site.addHost(host2);
            }
            else{
                return false;
            }
        }

//        site.setRegion(request.getParameter("region"));
        site.setCreateTime(localDateTime);
        siteRepository.save(site);

        return true;
    }

    @Override
    public PageData<Site> getPage(String name, int page, int pageSize) {
        PageData<Site> data = null;
        Specification<Site> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(name)) {
                predicates.add(cb.like(root.get("name").as(String.class), "%" + name + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        Page<Site> pageData = siteRepository.findAll(specification,new PageRequest(page - 1, pageSize));
        if (pageData != null) {
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
    public Site getSite(long siteId) {
        return siteRepository.findOne(siteId);
    }


}
