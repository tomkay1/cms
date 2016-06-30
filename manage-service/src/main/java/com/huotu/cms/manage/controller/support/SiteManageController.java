/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller.support;

import com.huotu.cms.manage.exception.RedirectException;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Login;
import com.huotu.hotcms.service.service.SiteService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.Serializable;

/**
 * 管理站点内容的控制器,需要当前登录操作人员已经选择好站点
 *
 * @author CJ
 */
public abstract class SiteManageController<T, ID extends Serializable, PD, MD> extends CRUDController<T, ID, PD, MD> {

    @Autowired
    private SiteService siteService;

    @Override
    protected final Specification<T> prepareIndex(Login login, RedirectAttributes attributes) throws RedirectException {
        Site site = checkSite(login);

        return prepareIndex(login, site, attributes);
    }

    @NotNull
    private Site checkSite(Login login) throws RedirectException {
        Long siteId = login.currentSiteId();
        if (siteId == null) {
            throw new RedirectException("/manage/site", "请先选择一个站点,再进行操作");
        }
        Site site = siteService.getSite(siteId);
        if (site == null || !login.siteManageable(site)) {
            throw new RedirectException("/manage/site", "你无权操作。");
        }
        return site;
    }

    @Override
    protected final T preparePersist(Login login, T data, PD extra, RedirectAttributes attributes)
            throws RedirectException {
        Site site = checkSite(login);

        return preparePersist(login, site, data, extra, attributes);
    }


    /**
     * 默认会认为资源会有一个名为<code>site</code>的字段并以此作为过滤条件
     *
     * @param login
     * @param site       当前站点
     * @param attributes
     * @return
     * @see CRUDController#prepareIndex(Login, RedirectAttributes)
     */
    @SuppressWarnings({"WeakerAccess", "JavaDoc"})
    protected Specification<T> prepareIndex(Login login, Site site, RedirectAttributes attributes)
            throws RedirectException {
        return (root, query, cb) -> cb.equal(root.get("site").as(Site.class), site);
    }

    /**
     * @param login
     * @param site       当前站点
     * @param data
     * @param extra
     * @param attributes
     * @return
     * @see CRUDController#preparePersist(Login, Object, Object, RedirectAttributes)
     */
    @SuppressWarnings({"WeakerAccess", "JavaDoc"})
    protected abstract T preparePersist(Login login, Site site, T data, PD extra, RedirectAttributes attributes)
            throws RedirectException;

}
