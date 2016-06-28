/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.Host;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.exception.NoSiteFoundException;
import com.huotu.hotcms.service.util.PageData;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Set;

public interface SiteService {

    /**
     * 寻找最适合的站点
     *
     * @param host   主机
     * @param locale 要求语言
     * @return 最合适的站点
     * @throws NoSiteFoundException
     */
    @Transactional(readOnly = true)
    Site closestSite(Host host, Locale locale) throws NoSiteFoundException;

    /**
     * @param ownerId 指定商户的owerId
     * @param deleted 是否已删除
     * @return 指定商户的删除也是指定的站点
     */
    @Transactional(readOnly = true)
    Set<Site> findByOwnerIdAndDeleted(long ownerId, boolean deleted);

    PageData<Site> getPage(long owner, String name, int page, int pageSize);

    /**
     *
     * @param siteId 站点id
     * @return 如果没有找到返回null
     */
    @Transactional(readOnly = true)
    Site getSite(long siteId);

    Boolean save(Site site);

    /**
     * 实现站点的可复制
     *
     * @param templateId   模板ID
     * @param customerSite 用户站点
     * @since v2.0
     */
    void siteCopy(long templateId, Site customerSite) throws Exception;

    /**
     * 新建站点
     *
     * @param domains     可用域名
     * @param homeDomains 主推域名
     * @param site        站点（可能只是一个JO）
     * @param locale      要求的语言
     * @return 已保存的站点
     */
    @Transactional
    Site newSite(String[] domains, String homeDomains, Site site, Locale locale);

    /**
     * 修改当前站点
     *
     * @param domains     可用域名
     * @param homeDomains 主推域名
     * @param site        站点
     * @param locale      要求的语言
     * @return 已保存的站点
     */
    @Transactional
    Site patchSite(String[] domains, String homeDomains, Site site, Locale locale);
}
