/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.controller;

import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.widget.test.TestBase;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * /dataSource 的测试 具体数据测试已经在{@link com.huotu.hotcms.widget.service.impl.CMSDataSourceServiceTest}完成
 * 这里只是确定下controller是否可互动
 * Created by lhx on 2016/7/29.
 */
@Transactional
public class CMSDataSourceControllerTest extends TestBase {

    @Test
    public void testFindLink() throws Exception {
        Site site = randomSite(randomOwner());
        Category category = randomCategory(site, ContentType.Link);

        mockMvc.perform(get("/dataSource/findLink/{parentId}", String.valueOf(category.getId()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0))
        ;

        Link link = randomLink(category);

        mockMvc.perform(get("/dataSource/findLink/{parentId}", String.valueOf(category.getId()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
