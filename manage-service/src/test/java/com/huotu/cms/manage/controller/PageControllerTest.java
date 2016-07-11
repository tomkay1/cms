/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huotu.cms.manage.ManageTest;
import com.huotu.hotcms.service.entity.PageInfo;
import com.huotu.hotcms.service.repository.PageInfoRepository;
import com.huotu.hotcms.widget.page.Page;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by hzbc on 2016/7/9.
 */
public class PageControllerTest extends ManageTest {

    @Autowired
    private PageInfoRepository pageInfoRepository;

    @Test
    public void flow() throws Exception {
        //首先确保虚拟出来的siteId 并没有存在任何页面
        long siteId = 200 + random.nextInt(20000);
        mockMvc.perform(get("/manage/{siteId}/pages", siteId)
                .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk());
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.length()").value(0));//exception:json can not be null or empty
//      Page page = randomPage();
//      String json = JSON.toJSONString(page);
//      创建一个page,page应该是从界面上put上来的,此处从测试类路劲下的page.json中获取
        InputStream is= this.getClass().getClassLoader().getResourceAsStream("page.json");
        String pageJson= StreamUtils.copyToString(is, Charset.forName("utf-8"));

        // 保存它 save
        String pageHref = mockMvc.perform(put("/manage/pages/{siteId}", siteId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(pageJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getRedirectedUrl();

        // 单独获取 pageID为1的原因是json中保存的
        mockMvc.perform(get("/manage/pages/{pageId}",1).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        // TODO 更多数据校验 以确保返回的数据 是之前创建的Page
//                .andExpect()
        ;

        //保存页面部分属性
        String propertyName = UUID.randomUUID().toString();
        mockMvc.perform(put("/manage/pages/{pageId}/{propertyName}", 1, propertyName)).andDo(print())
                .andExpect(status().isAccepted())
                .andReturn();


        mockMvc.perform(get("/manage/owners/{ownerId}/pages", siteId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
        // https://github.com/jayway/JsonPath
        // TODO 更多数据校验 以确保返回的数据 是之前创建的Page
//        .andExpect(jsonPath("$.[0]").value(..))
        ;

        // 删除
        mockMvc.perform(delete(pageHref))
                .andExpect(status().isNoContent());
        // 现在长度应该是0
        mockMvc.perform(get("/manage/owners/{ownerId}/pages", siteId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void readJson() throws IOException {
        InputStream is= this.getClass().getClassLoader().getResourceAsStream("page.json");
        String pageJson= StreamUtils.copyToString(is, Charset.forName("utf-8"));
        ObjectMapper objectMapper=new ObjectMapper();
        Page page=objectMapper.readValue(pageJson, Page.class);
    }

    @Test
    public void testGetPage() throws Exception {
        PageInfo pageInfo=pageInfoRepository.findAll().get(0);

        mockMvc.perform(get("/manage/pages/{pageId}", pageInfo.getPageId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));
    }
}
