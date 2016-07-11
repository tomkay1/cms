/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller;

import com.huotu.cms.manage.controller.support.SiteManageController;
import com.huotu.cms.manage.exception.RedirectException;
import com.huotu.cms.manage.service.PageFilterBehavioral;
import com.huotu.hotcms.service.entity.PageInfo;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Login;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

/**
 * 页面控制器
 *
 * @author CJ
 */
@Controller
@RequestMapping("/manage/page")
public class PageInfoController extends SiteManageController<PageInfo, Long, Void, Void> {

    private static final Log log = LogFactory.getLog(PageInfoController.class);

    @Autowired
    private PageFilterBehavioral pageFilterBehavioral;

    @Override
    protected PageInfo preparePersist(Login login, Site site, PageInfo data, Void extra, RedirectAttributes attributes)
            throws RedirectException {
        if (data.getPagePath() != null && !pageFilterBehavioral.ableToUse(data.getPagePath())) {
            throw new RedirectException("/manage/page", "这个路径无法使用。");
        }
        data.setCreateTime(LocalDateTime.now());
        data.setSite(site);
        return data;
    }

    @Override
    protected String indexViewName() {
        return "/views/page/index.html";
    }

    @Override
    protected void prepareSave(Login login, PageInfo entity, PageInfo data, Void extra, RedirectAttributes attributes)
            throws RedirectException {
        throw new NoSuchMethodError("no support for save category");
    }

    @Override
    protected String openViewName() {
        return null;
    }


}
