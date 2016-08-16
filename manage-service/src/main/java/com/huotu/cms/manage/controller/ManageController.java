/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller;

import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Login;
import com.huotu.hotcms.service.service.SiteService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author CJ
 */
@Controller
@RequestMapping(value = "/manage")
public class ManageController {

    private static final Log log = LogFactory.getLog(ManageController.class);

    @Autowired
    private SiteService siteService;

    /**
     * 切换当前站点
     *
     * @param id 要切换的站点id
     */
    @RequestMapping(value = "/switch/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROOT','" + Login.Role_Manage_Value + "')")
    public void switchCurrentSite(@AuthenticationPrincipal Login login, @PathVariable("id") Long id) {
        log.debug("user " + login + " switching current site to " + id);

        Site site = siteService.getSite(id);
        if (login.siteManageable(site))
            login.updateSiteId(id);
        else {
            throw new AccessDeniedException("你无权访问。");
        }

    }

    @RequestMapping(value = {"/", ""}, method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String index() {
        return "redirect:/manage/main";
    }

}
