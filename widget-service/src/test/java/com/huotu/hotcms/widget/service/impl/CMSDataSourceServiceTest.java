/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.service.impl;

import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.LinkModel;
import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.service.CMSDataSourceService;
import com.huotu.hotcms.widget.test.TestBase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 这是一个级别很低的测试
 *
 * @author CJ
 */
@Transactional
public class CMSDataSourceServiceTest extends TestBase {
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private CMSDataSourceService cmsDataSourceService;
    @Autowired(required = false)
    private HttpServletResponse response;

    private Site site;

    @Before
    public void init() {
        // 准备下CMSContent
        site = randomSite(randomOwner());
        CMSContext.PutContext(request, response, site);
    }

    @Test
    public void link() {
        assertThat(cmsDataSourceService.findLinkCategory())
                .isEmpty();

        Category category = randomCategory(site, ContentType.Link);
        assertThat(cmsDataSourceService.findLinkCategory())
                .contains(category);

        assertThat(cmsDataSourceService.findLinkContent(category.getId()))
                .isEmpty();

        List<Link> linkList = new ArrayList<>();
        List<LinkModel> linkModelList = new ArrayList<>();
        int count = random.nextInt(10) + 2;
        while (count-- > 0) {
            Link link = randomLink(category);
            linkList.add(link);

            LinkModel model = new LinkModel();
            model.setLinkUrl(link.getLinkUrl());
            model.setThumbUri(link.getThumbUri());
            linkModelList.add(model);
        }

        assertThat(cmsDataSourceService.findLinkContent(category.getId()))
                .containsAll(linkModelList);
    }



}