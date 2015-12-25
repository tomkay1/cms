package com.huotu.hotcms.service.impl;

import com.huotu.hotcms.entity.Site;
import com.huotu.hotcms.repository.SiteRepository;
import com.huotu.hotcms.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * Created by chendeyu on 2015/12/24.
 */
@Service
public class SiteServiceImpl implements SiteService {

    @Autowired
    SiteRepository siteRepository ;

    @Override
    public void addSite(HttpServletRequest request) {
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
//        site.setHosts(request.getParameter("hosts"));
//        site.setRegion(request.getParameter("region"));
        site.setCreateTime(localDateTime);
//        Site sitefalg=siteRepository.findByCustomerIdAndName(site.getCustomerId(), site.getName());
//        if (sitefalg==null){
//        }
//        else {
//            site.setCreateTime(sitefalg.getCreateTime());
//        }
        siteRepository.save(site);

    }


}
