package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.service.SiteService;
import com.huotu.hotcms.service.util.PageData;

/**
 * Created by cwb on 2015/12/31.
 */
public class SiteServiceWebImpl implements SiteService {
    @Override
    public PageData<Site> getPage(String name, int page, int pageSize) {
        return null;
    }

    @Override
    public Site getSite(long siteId) {
        return null;
    }

    @Override
    public Boolean save(Site site) {
        return null;
    }

    @Override
    public Site findBySiteIdAndCustomerId(Long siteId, int customerId) {
        return null;
    }

    @Override
    public boolean deleteSite(Long id) {
        return false;
    }
}
