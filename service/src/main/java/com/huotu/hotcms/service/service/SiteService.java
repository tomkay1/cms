package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.util.PageData;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Set;

/**
 * Created by chendeyu on 2015/12/24.
 */
public interface SiteService {

    PageData<Site> getPage(Integer customerId,String name,int page,int pageSize);

    Site getSite(long siteId);
    Boolean save(Site site);

    Site saveAndFlush(Site site);

    Site findBySiteIdAndCustomerId(Long siteId,int customerId);

    Set<Site> findByCustomerIdAndDeleted(Integer customerId,boolean deleted);

    /**
     * 实现站点的可复制
     * @param templateSite 模板对应的匿名站点
     * @param customerSite 用户站点
     * @return true,成功 反之失败
     *
     * @since v2.0
     * @author fawzi
     */
    boolean siteCopy(Site templateSite,Site customerSite) throws Exception;
}
