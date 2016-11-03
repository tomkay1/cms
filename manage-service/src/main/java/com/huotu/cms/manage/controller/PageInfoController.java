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
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Login;
import com.huotu.hotcms.service.exception.PageNotFoundException;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.widget.entity.PageInfo;
import com.huotu.hotcms.widget.service.PageService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * 页面控制器
 *
 * @author CJ
 */
@Controller
@RequestMapping("/manage/page")
public class PageInfoController extends SiteManageController<PageInfo, Long, Long, Void> {

    private static final Log log = LogFactory.getLog(PageInfoController.class);
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PageFilterBehavioral pageFilterBehavioral;
    @Autowired
    private PageService pageService;

    @Override
    protected String resourceName(Locale locale) {
        return "页面";
    }

    @Override
    protected PageInfo preparePersist(Login login, Site site, PageInfo data, Long extra, RedirectAttributes attributes, HttpServletRequest request)
            throws RedirectException {
        if (data.getPagePath() != null && !pageFilterBehavioral.ableToUse(data.getPagePath())) {
            throw new RedirectException("/manage/page", "这个路径无法使用。");
        }
        // 看看有没有同地址的页面
        if (data.getPagePath() == null) {
            data.setPagePath("");
        }
        try {
            pageService.findBySiteAndPagePath(site, data.getPagePath());
            throw new RedirectException("/manage/page", "这个路径无法使用。");
        } catch (PageNotFoundException ignored) {
            //情况正常继续
        }

        data.setSite(site);
        if (extra != null && extra > 0) {
            Category category = categoryRepository.getOne(extra);
            if (!category.getSite().equals(site)) {
                throw new RedirectException("/manage/page", "无法选用" + category.getName() + "作为数据源。");
            }
            data.setCategory(category);
        }
        return data;
    }

    @Override
    protected String indexViewName() {
        return "/view/page/index.html";
    }

    @Override
    protected void prepareUpdate(Login login, PageInfo entity, PageInfo data, Void extra, RedirectAttributes attributes, HttpServletRequest request)
            throws RedirectException {
        throw new NoSuchMethodError("no support for save category");
    }

    @Override
    protected String openViewName() {
        return null;
    }


}
