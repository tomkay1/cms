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
import com.huotu.cms.manage.page.DownloadPage;
import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.entity.Download;
import com.huotu.hotcms.service.entity.Site;

import java.beans.PropertyDescriptor;
import java.util.UUID;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author CJ
 */
public class DownloadControllerTest extends ContentManageTest<Download> {

    public DownloadControllerTest() {
        super(ContentType.Download, DownloadPage.class);
    }

    @Override
    protected void normalRandom(Download value, Site site) {
        value.setFileName(UUID.randomUUID().toString());
    }

    @Override
    protected void assertCreation(Download entity, Download data) {
        assertThat(entity.getFileName()).isEqualTo(data.getFileName());
    }

    @Override
    protected Predicate<? super PropertyDescriptor> editableProperty() throws Exception {
        return propertyDescriptor -> propertyDescriptor.getName().equals("fileName");
    }
}