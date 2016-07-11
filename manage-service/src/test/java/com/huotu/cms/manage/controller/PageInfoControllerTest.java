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
import com.huotu.cms.manage.page.PageInfoPage;
import com.huotu.hotcms.service.entity.Site;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author CJ
 */
public class PageInfoControllerTest extends SiteManageTest {

    @Test
    @Transactional
    public void flow() throws Exception {
        Site site = loginAsSite();

        ManageMainPage manageMainPage = initPage(ManageMainPage.class);

        PageInfoPage page = manageMainPage.toPageInfo();


    }


}