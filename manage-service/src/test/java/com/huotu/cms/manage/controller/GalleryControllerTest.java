/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller;

import com.huotu.cms.manage.ContentManageTest;
import com.huotu.cms.manage.page.GalleryPage;
import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.entity.Gallery;
import com.huotu.hotcms.service.entity.Site;

import java.beans.PropertyDescriptor;
import java.util.function.Predicate;

/**
 * @author CJ
 */
public class GalleryControllerTest extends ContentManageTest<Gallery> {

    public GalleryControllerTest() {
        super(ContentType.Gallery, GalleryPage.class);
    }

    @Override
    protected void normalRandom(Gallery value, Site site) {

    }

    @Override
    protected void assertCreation(Gallery entity, Gallery data) {

    }

    @Override
    protected Predicate<? super PropertyDescriptor> editableProperty() throws Exception {
        return propertyDescriptor -> false;
    }
}