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
import com.huotu.cms.manage.page.LinkPage;
import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.entity.Site;

import java.beans.PropertyDescriptor;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author CJ
 */
public class LinkControllerTest extends ContentManageTest<Link> {

    public LinkControllerTest() {
        super(ContentType.Link, LinkPage.class);
    }

    @Override
    protected void normalRandom(Link value, Site site) {
        value.setLinkUrl(randomHttpURL());
    }

    @Override
    protected void assertCreation(Link entity, Link data) {
        assertThat(entity.getLinkUrl()).isEqualTo(data.getLinkUrl());
    }

    @Override
    protected Predicate<? super PropertyDescriptor> editableProperty() throws Exception {
        return (propertyDescriptor -> propertyDescriptor.getName().equals("linkUrl"));
    }
}