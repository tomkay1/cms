/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller;

import com.huotu.cms.manage.controller.support.CRUDController;
import com.huotu.cms.manage.exception.RedirectException;
import com.huotu.cms.manage.util.ImageHelper;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Login;
import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.service.repository.OwnerRepository;
import com.huotu.hotcms.service.service.SiteService;
import lombok.Data;
import me.jiangcai.lib.resource.Resource;
import me.jiangcai.lib.resource.service.ResourceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Locale;

/**
 * Created by chendeyu on 2015/12/24.
 * 全面重写 by CJ
 */
@Controller
@RequestMapping("/manage/site")
public class SiteController extends CRUDController<Site, Long, SiteController.AboutNewSite, Void> {

    private static final Log log = LogFactory.getLog(SiteController.class);

    @Autowired
    private SiteService siteService;

    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private ResourceService resourceService;

    @Override
    protected Specification<Site> prepareIndex(Login login, RedirectAttributes attributes) throws RedirectException {
        return (root, query, cb)
                -> cb.and(cb.isFalse(root.get("deleted")), cb.equal(root.get("owner").get("id")
                , login.currentOwnerId()));
    }

    @Override
    protected String indexViewName() {
        return "/view/site/index.html";
    }

    @Override
    protected Site preparePersist(Login login, Site data, AboutNewSite extra, RedirectAttributes attributes) throws RedirectException {
        //  只有Root才可以干这事。
        if (!login.isRoot())
            throw new AccessDeniedException("无法访问。");
        Owner owner = ownerRepository.getOne(login.currentOwnerId());
        data.setOwner(owner);

        data = siteService.newSite(extra.getDomains(), extra.getHomeDomain(), data, Locale.CHINA);
        if (!StringUtils.isEmpty(extra.getTmpLogoPath())) {
            Resource tmp = resourceService.getResource(extra.getTmpLogoPath());
            if (tmp.exists()) {
                try {
                    String path = ImageHelper.storeAsImage("png", resourceService, tmp.getInputStream());
                    data.setLogoUri(path);
                } catch (IOException e) {
                    throw new RedirectException("/manage/site", e.getMessage(), e);
                }
                try {
                    resourceService.deleteResource(extra.getTmpLogoPath());
                } catch (IOException ignored) {
                }
            }
        }
        return data;
    }

    @Override
    protected void prepareSave(Login login, Site entity, Site data, Void extra, RedirectAttributes attributes) throws RedirectException {
        System.out.println(entity);
    }

    @Override
    protected String openViewName() {
        return "/view/site/site.html";
    }

    @Data
    static class AboutNewSite {
        private String[] domains;
        private String homeDomain;
        private String tmpLogoPath;
    }
}
