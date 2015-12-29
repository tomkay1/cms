package com.huotu.hotcms.service;

import com.huotu.hotcms.entity.Site;
import com.huotu.hotcms.util.PageData;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chendeyu on 2015/12/24.
 */
public interface SiteService {

    boolean addSite(HttpServletRequest request,String... hosts);
//    Page<Site> getPage( InvoiceSearcher invoiceSearcher);
    PageData<Site> getPage(String name,int page,int pageSize);

    Site getSite(long siteId);
    Boolean save(Site site);

    Site findBySiteIdAndCustomerId(Long siteId,int customerId);
    boolean deleteSite(Long id);
}
