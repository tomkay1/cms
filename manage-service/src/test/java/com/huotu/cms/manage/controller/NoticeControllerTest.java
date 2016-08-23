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
import com.huotu.cms.manage.page.NoticePage;
import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.entity.Notice;
import com.huotu.hotcms.service.entity.Site;

import java.beans.PropertyDescriptor;
import java.util.UUID;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author CJ
 */
public class NoticeControllerTest extends ContentManageTest<Notice> {

    public NoticeControllerTest() {
        super(ContentType.Notice, NoticePage.class);
    }

    @Override
    protected void normalRandom(Notice value, Site site) {
        value.setContent(UUID.randomUUID().toString());
    }

    @Override
    protected void assertCreation(Notice entity, Notice data) {
        assertThat(entity.getContent()).isEqualTo(data.getContent());
    }

    @Override
    protected Predicate<? super PropertyDescriptor> editableProperty() throws Exception {
        return propertyDescriptor -> propertyDescriptor.getName().equals("content");
    }
}