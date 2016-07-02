/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller;

import com.huotu.cms.manage.SiteManageTest;
import com.huotu.cms.manage.page.CategoryPage;
import com.huotu.cms.manage.page.ManageMainPage;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.service.CategoryService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author CJ
 */
public class CategoryControllerTest extends SiteManageTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    public void index() throws Exception {
        Site site = loginAsOwnerReturnSite();

        ManageMainPage mainPage = initPage(ManageMainPage.class);

        CategoryPage page;
        try {
            page = mainPage.toCategory();
            throw new AssertionError("现在应该还看不到页面");
        } catch (Exception ignored) {
        }
        // 试下使用{{}}
        mainPage.switchSite(site);
        page = mainPage.toCategory();
    }

    @Test
    public void add() throws Exception {
        Site site = loginAsSite();

//        addRoute();
//
//        Set<Route> routeSet = routeService.getRoute(site);
//        assertThat(routeSet)
//                .hasSize(1);
    }


}