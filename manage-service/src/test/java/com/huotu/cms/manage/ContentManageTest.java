/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage;

import com.huotu.cms.manage.controller.support.CRUDHelper;
import com.huotu.cms.manage.controller.support.CRUDTest;
import com.huotu.cms.manage.page.ManageMainPage;
import com.huotu.cms.manage.page.support.AbstractCMSContentPage;
import com.huotu.cms.manage.page.support.AbstractCRUDPage;
import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.entity.AbstractContent;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.repositoryi.AbstractContentRepository;
import com.huotu.hotcms.service.service.ContentService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.UUID;
import java.util.function.BiConsumer;

/**
 * 所有正文管理测试的公用基类
 *
 * @author CJ
 */
public abstract class ContentManageTest<T extends AbstractContent> extends SiteManageTest {

    private final ContentType contentType;
    private final Class<? extends AbstractCMSContentPage<T>> pageClass;
    @Autowired
    protected ContentService contentService;
    @Autowired
    private AbstractContentRepository<T> abstractContentRepository;

    protected ContentManageTest(ContentType contentType, Class<? extends AbstractCMSContentPage<T>> pageClass) {
        this.contentType = contentType;
        this.pageClass = pageClass;
    }

    protected T randomValue(Site site) {
        @SuppressWarnings("unchecked")
        T value = (T) contentService.newContent(contentType);
        value.setTitle(UUID.randomUUID().toString());
        value.setDescription(UUID.randomUUID().toString());
        normalRandom(value, site);
        return value;
    }

    protected abstract void normalRandom(T value, Site site);

    protected abstract void assertCreation(T entity, T data);


    @Test
    public void flow() throws Exception {
        Site site = loginAsSite();

        ManageMainPage manageMainPage = initPage(ManageMainPage.class);

        AbstractCMSContentPage<T> page = manageMainPage.toPage(pageClass);

        ContentTest contentTest = new ContentTest(site);

        CRUDHelper.flow(page, contentTest);
    }


    class ContentTest implements CRUDTest<T> {

        final Site site;

        public ContentTest(Site site) {
            this.site = site;
        }

        @Override
        public boolean modify() {
            return true;
        }

        @Override
        public Collection<T> list() throws Exception {
            return abstractContentRepository.findByCategory_SiteAndDeletedFalse(site);
        }

        @Override
        public T randomValue() throws Exception {
            return ContentManageTest.this.randomValue(site);
        }

        @Override
        public BiConsumer<AbstractCRUDPage<T>, T> customAddFunction() throws Exception {
            return null;
        }

        @Override
        public void assertCreation(T entity, T data) throws Exception {
// TODO 通用的先来
            ContentManageTest.this.assertCreation(entity, data);
        }
    }


}
