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
import com.huotu.hotcms.service.common.EnumUtils;
import com.huotu.hotcms.service.common.SiteType;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
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
        data.setCreateTime(LocalDateTime.now());
        data.setSiteType(EnumUtils.valueOf(SiteType.class, extra.getSiteTypeId()));

        data = siteService.newSite(extra.getDomains(), extra.getHomeDomain(), data, Locale.CHINA);
        if (extra.getTmpLogoPath() != null) {
            Resource tmp = resourceService.getResource(extra.getTmpLogoPath());
            if (tmp.exists()) {

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
        private int siteTypeId;
        private String[] domains;
        private String homeDomain;
        private String tmpLogoPath;
    }
}
