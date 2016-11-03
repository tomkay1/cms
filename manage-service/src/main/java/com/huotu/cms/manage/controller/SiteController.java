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
import com.huotu.hotcms.service.entity.Host;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Login;
import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.service.repository.OwnerRepository;
import com.huotu.hotcms.service.service.CommonService;
import com.huotu.hotcms.service.service.HostService;
import com.huotu.hotcms.service.service.SiteService;
import lombok.Data;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Created by chendeyu on 2015/12/24.
 * 全面重写 by CJ
 */
@Controller
@RequestMapping("/manage/site")
public class SiteController extends CRUDController<Site, Long, SiteController.AboutNewSite, SiteController.AboutNewSite> {

    private static final Log log = LogFactory.getLog(SiteController.class);
    @Autowired
    protected CommonService commonService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private HostService hostService;
    @Autowired
    private OwnerRepository ownerRepository;

    @Override
    protected Specification<Site> prepareIndex(Login login, HttpServletRequest request, Model model, RedirectAttributes attributes) throws RedirectException {
        return (root, query, cb)
                -> cb.and(cb.isFalse(root.get("deleted")), cb.equal(root.get("owner").get("id")
                , login.currentOwnerId()));
    }

    @Override
    protected void prepareOpen(Login login, HttpServletRequest request, Site data, Model model, RedirectAttributes attributes) throws RedirectException {
        super.prepareOpen(login, request, data, model, attributes);
        String domains = String.join(",", hostService.hookOn(data).stream()
                .map(Host::getDomain)
                .collect(Collectors.toSet()));
        model.addAttribute("domains", domains);
    }

    @Override
    protected String indexViewName() {
        return "/view/site/index.html";
    }

    @Override
    protected Site preparePersist(HttpServletRequest request, Login login, Site data, AboutNewSite extra, RedirectAttributes attributes)
            throws RedirectException {
        //  只有Root才可以干这事。
        if (!login.isRoot())
            throw new AccessDeniedException("无法访问。");
        Owner owner = ownerRepository.getOne(login.currentOwnerId());
        data.setOwner(owner);

        data = siteService.newSite(extra.getDomains(), extra.getHomeDomain(), data, Locale.CHINA);
        try {
            commonService.updateImageFromTmp(data, 0, extra.getTmpLogoPath());
        } catch (IOException e) {
            throw new RedirectException(rootUri(), e.getMessage(), e);
        }

        return data;
    }


    @Override
    protected void prepareUpdate(Login login, Site entity, Site data, AboutNewSite extra, RedirectAttributes attributes, HttpServletRequest request)
            throws RedirectException {
        if (!login.siteManageable(entity)) {
            throw new AccessDeniedException("您无权修改该网站。");
        }
        entity.setDescription(data.getDescription());
        entity.setKeywords(data.getKeywords());
        entity.setTitle(data.getTitle());
        entity.setCopyright(data.getCopyright());

        siteService.patchSite(extra.getDomains(), extra.getHomeDomain(), entity, Locale.CHINA);
        try {
            commonService.updateImageFromTmp(entity, 0, extra.getTmpLogoPath());
        } catch (IOException e) {
            throw new RedirectException(rootUri() + "/" + entity.getSiteId(), e.getMessage(), e);
        }

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
