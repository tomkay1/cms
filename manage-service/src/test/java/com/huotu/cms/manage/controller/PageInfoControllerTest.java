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
import com.huotu.cms.manage.service.PageFilterBehavioral;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.widget.entity.PageInfo;
import com.huotu.hotcms.widget.repository.PageInfoRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.function.BiConsumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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

        CRUDHelper.flow(manageMainPage.toPage(PageInfoPage.class), new PageInfoCRUDTest(site));

        // 现在测试添加敏感uri
        CRUDHelper.flow(manageMainPage.toPage(PageInfoPage.class), new PageInfoCRUDTest(site) {
            @Override
            public PageInfo randomValue() {
                PageInfo info = super.randomValue();
                info.setPagePath(PageFilterBehavioral.protectedPath.stream()
                        .findAny().orElseThrow(IllegalStateException::new));
                return info;
            }

            @Override
            public String errorMessageAfterAdd(PageInfo data) {
//                return null;
                return "无法使用";
            }
        });

        PageInfo pageInfo = new PageInfoCRUDTest(site).randomValue();
//        现在测试下添加相同的链接
        CRUDHelper.flow(manageMainPage.toPage(PageInfoPage.class), new OnePagePageInfoCRUDTest(site, pageInfo));
        CRUDHelper.flow(manageMainPage.toPage(PageInfoPage.class), new OnePagePageInfoCRUDTest(site, pageInfo) {
            @Override
            public String errorMessageAfterAdd(PageInfo data) {
                return "无法使用";
            }
        });
    }

    @Test
    @Transactional
    public void update() throws Exception {
        Site site = loginAsSite();
        Owner owner = randomOwner();
        loginAsOwner(owner);
        PageInfo pageInfo1 = new PageInfoCRUDTest(site).randomValue();
        pageInfo1 = pageInfoRepository.saveAndFlush(pageInfo1);
        mockMvc.perform(post("/manage/page/update/{id}", "" + pageInfo1.getId())
                .param("title", "成功")
                .param("pagePath", "pagePath123").session(session));
        pageInfo1 = pageInfoRepository.findOne(pageInfo1.getId());
        assertThat(pageInfo1.getTitle()).as("修改成功").isEqualTo("成功");
        assertThat(pageInfo1.getPagePath()).as("修改成功").isEqualTo("pagePath123");
    }

    private class OnePagePageInfoCRUDTest extends PageInfoCRUDTest {
        private final PageInfo pageInfo;

        public OnePagePageInfoCRUDTest(Site site, PageInfo pageInfo) {
            super(site);
            this.pageInfo = pageInfo;
        }

        @Override
        public PageInfo randomValue() {
            return pageInfo;
        }
    }


    private class PageInfoCRUDTest implements CRUDTest<PageInfo> {
        private final Site site;

        PageInfoCRUDTest(Site site) {
            this.site = site;
        }

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

        @Override
        public void assertUpdate(PageInfo entity, PageInfo data) throws Exception {
            assertThat(entity.getTitle())
                    .isEqualToIgnoringCase(data.getTitle());
            assertThat(entity.getPagePath())
                    .isEqualTo(data.getPagePath());
        }
    }
}