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
import com.huotu.hotcms.service.common.LinkType;
import com.huotu.hotcms.service.common.PageType;
import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.service.ContentService;
import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.entity.PageInfo;
import com.huotu.hotcms.widget.repository.PageInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author CJ
 */
public class LinkControllerTest extends ContentManageTest<Link> {

    @Autowired
    private PageInfoRepository pageInfoRepository;
    @Autowired
    private ContentService contentService;

    public LinkControllerTest() {
        super(ContentType.Link, LinkPage.class);
    }

    @Override
    protected void normalRandom(Link value, Site site) {
        value.setLinkUrl(randomHttpURL());
        value.setLinkType(LinkType.page);
        List<PageInfo> list = pageInfoRepository.findBySite(site);
        if (!list.isEmpty())
            value.setPageInfoID(list.get(0).getId());
        else {
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPageType(PageType.Ordinary);
            pageInfo.setSite(site);
            pageInfo.setPagePath(UUID.randomUUID().toString().replace("-", ""));
            contentService.init(pageInfo);
            pageInfo = pageInfoRepository.saveAndFlush(pageInfo);
            value.setPageInfoID(pageInfo.getId());
        }
    }


    @Override
    protected void assertCreation(Link entity, Link data) {
        assertThat(entity.getLinkUrl()).isEqualTo(data.getLinkUrl());
        assertThat(entity.getLinkType()).isEqualTo(data.getLinkType());
        List<PageInfo> list = pageInfoRepository.findBySite(CMSContext.RequestContext().getSite());
        if (!list.isEmpty()) {
            assertThat(entity.getPageInfoID()).isEqualTo(data.getPageInfoID());
        }
    }

    @Override
    protected Predicate<? super PropertyDescriptor> editableProperty() throws Exception {
        return (propertyDescriptor -> propertyDescriptor.getName().equals("linkUrl"));
    }
}