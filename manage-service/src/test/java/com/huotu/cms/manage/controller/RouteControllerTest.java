/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller;

import com.huotu.cms.manage.ManageTest;
import com.huotu.cms.manage.page.ManageMainPage;
import com.huotu.cms.manage.page.RoutePage;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Owner;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author CJ
 */
@Transactional
public class RouteControllerTest extends ManageTest {

    @Test
    public void index() throws Exception {
        Owner owner = randomOwner();
        Site site = randomSite(owner);
        loginAsOwner(owner);

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

}