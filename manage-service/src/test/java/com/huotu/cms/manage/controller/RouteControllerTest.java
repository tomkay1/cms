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
import com.huotu.cms.manage.page.ManageMainPage;
import com.huotu.cms.manage.page.RoutePage;
import com.huotu.hotcms.service.entity.Route;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.service.RouteService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author CJ
 */
@Transactional
public class RouteControllerTest extends SiteManageTest {

    @Autowired
    private RouteService routeService;

    @Test
    public void index() throws Exception {
        Site site = loginAsOwnerReturnSite();

        ManageMainPage mainPage = initPage(ManageMainPage.class);

        RoutePage page;
        try {
            page = mainPage.toRoute();
            throw new AssertionError("现在应该还看不到页面");
        } catch (Exception ignored) {
        }
        // 试下使用{{}}
        mainPage.switchSite(site);
        page = mainPage.toRoute();
    }

    @Test
    public void add() throws Exception {
        Site site = loginAsSite();

        addRoute();

        Set<Route> routeSet = routeService.getRoute(site);
        assertThat(routeSet)
                .hasSize(1);
    }

    @Test
    public void delete() throws Exception {
        Site site = loginAsSite();

        RoutePage page = addRoute();
        page.deleteAny();

        Set<Route> routeSet = routeService.getRoute(site);
        assertThat(routeSet)
                .isEmpty();
    }

    @Test
    public void update() throws Exception {
        Site site = loginAsSite();

        RoutePage page = addRoute();
        RoutePage openPage = page.openOne(RoutePage.class, null);

        Set<Route> routeSet = routeService.getRoute(site);
        Route route = routeSet.stream().findAny().orElseThrow(() -> new IllegalStateException("似乎添加失败了。"));
        openPage.checkRouteData(route);
        Route routeData = randomRouteValue();
        page = openPage.modifyRoute(routeData);

        assertThat(route.getRule())
                .isEqualTo(routeData.getRule());
        assertThat(route.getTargetUri())
                .isEqualTo(routeData.getTargetUri());
        assertThat(route.getDescription())
                .isEqualTo(routeData.getDescription());

    }

    private RoutePage addRoute() {
        RoutePage page = initPage(ManageMainPage.class).toRoute();
        Route route = randomRouteValue();
        page.addRoute(route);
        page.reloadPageInfo();
        return page;
    }

}