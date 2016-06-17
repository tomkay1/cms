/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.util.PageData;

import java.util.Set;

public interface SiteService {

    PageData<Site> getPage(long owner, String name, int page, int pageSize);

    Site getSite(long siteId);
    Boolean save(Site site);

    Site saveAndFlush(Site site);

    Set<Site> findByOwnerIdAndDeleted(long ownerId, boolean deleted);

    /**
     * 实现站点的可复制
     * @param templateId 模板ID
     * @param customerSite 用户站点
     * @return true,成功 反之失败
     *
     * @since v2.0
     */
    void siteCopy(long templateId,Site customerSite) throws Exception;
}
