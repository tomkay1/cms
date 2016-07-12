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
import com.huotu.cms.manage.controller.support.CRUDHelper;
import com.huotu.cms.manage.controller.support.CRUDTest;
import com.huotu.cms.manage.page.ManageMainPage;
import com.huotu.cms.manage.page.PageInfoPage;
import com.huotu.cms.manage.page.support.AbstractCRUDPage;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.PageInfo;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.repository.PageInfoRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.function.BiConsumer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author CJ
 */
public class PageInfoControllerTest extends SiteManageTest {

    @Autowired
    private PageInfoRepository pageInfoRepository;

    @Test
    @Transactional
    public void flow() throws Exception {
        Site site = loginAsSite();

        ManageMainPage manageMainPage = initPage(ManageMainPage.class);

        CRUDHelper.flow(manageMainPage.toPage(PageInfoPage.class), new CRUDTest<PageInfo>() {
            @Override
            public Collection<PageInfo> list() {
                return pageInfoRepository.findBySite(site);
            }

            @Override
            public PageInfo randomValue() {
                Category category = random.nextBoolean() ? randomCategory(site) : null;
                PageInfo randomPageInfoValue = randomPageInfoValue();
                randomPageInfoValue.setCategory(category);
                return randomPageInfoValue;
            }

            @Override
            public BiConsumer<AbstractCRUDPage<PageInfo>, PageInfo> customAddFunction() {
                return null;
            }

            @Override
            public void assertCreation(PageInfo entity, PageInfo data) {
                assertThat(entity.getCategory())
                        .isEqualTo(data.getCategory());
                assertThat(entity.getTitle())
                        .isEqualToIgnoringCase(data.getTitle());
                assertThat(entity.getPageType())
                        .isEqualByComparingTo(data.getPageType());
                assertThat(entity.getPagePath())
                        .isEqualTo(data.getPagePath());
            }
        });

    }


}