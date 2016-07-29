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
import com.huotu.cms.manage.page.EditPage;
import com.huotu.cms.manage.page.ManageMainPage;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.widget.entity.PageInfo;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;


/**
 * 页面服务测试
 */
public class PageEditTest extends SiteManageTest {

    /**
     * 测试页面上点击“保存”功能
     */
    @Test
    @Transactional
    public void flow() throws Exception {

        Site site = loginAsSite();
        long siteID=site.getSiteId();
        ManageMainPage manageMainPage = initPage(ManageMainPage.class);
        PageInfo pageInfo = randomPageInfo(site);
        EditPage editPage=manageMainPage.toEditPage(pageInfo.getPageId());
        editPage.loadWidget();
        editPage.save();
    }
}
