/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.huotu.cms.manage.ManageTest;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Owner;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 编辑器内容测试
 *
 * @author CJ
 */
@Transactional
public class EditInTest extends ManageTest {

    private Site site;

    @Test
    public void doTest() throws Exception {
        // 所有内容 以及数据源
        Owner owner = randomOwner();
        loginAsOwner(owner);
        site = randomSite(owner);
        randomSiteData(site);

        forContentType("category");
    }

    /**
     * 1 检查 URI /manage/name?siteId=x 是否跟data.json一致
     * 2 检查 URI /manage/name/editIn?siteId=x
     *
     * @param name
     */
    private void forContentType(String name) throws Exception {
        // 第一步 检查数据

        String contentString = mockMvc.perform(get("/manage/" + name)
                .param("siteId", site.getSiteId().toString())
                .session(session)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andReturn().getResponse().getContentAsString();

        JsonNode array = objectMapper.readTree(contentString);
        assertSimilarJsonArray(array, new ClassPathResource("web/mock/data.json").getInputStream());

        // 第二步 检查页面
        driver.get("/manage/" + name + "/editIn?siteId=" + site.getSiteId());

    }

}
