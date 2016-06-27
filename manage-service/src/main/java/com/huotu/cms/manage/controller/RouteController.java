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
import com.huotu.hotcms.service.entity.Route;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Login;
import com.huotu.hotcms.service.service.SiteService;
import com.huotu.hotcms.service.service.impl.RouteServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Created by Administrator xhl 2016/1/9.
 */
@Controller
@RequestMapping("/manage/route")
public class RouteController extends CRUDController<Route, Long, Void, Void> {
    private static final Log log = LogFactory.getLog(RouteController.class);


    @Autowired
    private SiteService siteService;

    @Autowired
    private RouteServiceImpl routeService;

    @Override
    protected String indexViewName() {
        return null;
    }

    @Override
    protected Route preparePersist(Login login, Route data, Void extra, RedirectAttributes attributes) throws RedirectException {
        return null;
    }

    @Override
    protected void prepareSave(Login login, Route entity, Route data, Void extra, RedirectAttributes attributes) throws RedirectException {

    }

    @Override
    protected String openViewName() {
        return null;
    }

    @RequestMapping(value = "/isExistsRouteBySiteAndRule", method = RequestMethod.POST)
    @ResponseBody
    public Boolean isExistsRouteBySiteAndRule(@RequestParam(value = "siteId", defaultValue = "0") Long siteId,
                                              @RequestParam(value = "rule") String rule) {
        try {
            if (!StringUtils.isEmpty(rule)) {
                Site site = siteService.getSite(siteId);
                return !routeService.isPatterBySiteAndRule(site, rule);
            }
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return true;
        }
    }

    @RequestMapping(value = "/isExistsRouteBySiteAndRuleIgnore", method = RequestMethod.POST)
    @ResponseBody
    public Boolean isExistsRouteBySiteAndRuleIgnore(@RequestParam(value = "siteId", defaultValue = "0") Long siteId,
                                                    @RequestParam(value = "rule") String rule,
                                                    @RequestParam(value = "noRule") String noRule) {
        try {
            if (!StringUtils.isEmpty(rule)) {
                Site site = siteService.getSite(siteId);
                return !routeService.isPatterBySiteAndRuleIgnore(site, rule, noRule);
            }
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return true;
        }
    }

}
