package com.huotu.hotcms.service.impl;

import com.huotu.hotcms.entity.Site;
import com.huotu.hotcms.repository.SiteRepository;
import com.huotu.hotcms.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by chendeyu on 2015/12/24.
 */
@Service
public class SiteServiceImpl implements SiteService {

    @Autowired
    SiteRepository siteRepository ;

    @Override
    public void modifySite(Site site) {
        siteRepository.save(site);
    }
}
