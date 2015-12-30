package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.util.PageData;

/**
 * Created by chendeyu on 2015/12/24.
 */
public interface SiteService {

    PageData<Site> getPage(String name,int page,int pageSize);

    Site getSite(long siteId);
    Boolean save(Site site);

    Site findBySiteIdAndCustomerId(Long siteId,int customerId);
    boolean deleteSite(Long id);
}
