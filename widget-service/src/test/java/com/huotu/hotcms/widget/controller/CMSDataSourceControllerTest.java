/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.Video;
import com.huotu.hotcms.service.model.DataObject;
import com.huotu.hotcms.service.repository.SiteRepository;
import com.huotu.hotcms.widget.test.TestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
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
    @Autowired(required = false)
    private HttpServletResponse response;
    @Autowired
    private SiteRepository siteRepository;

    @Test
    public void testFindLink() throws Exception {
        Site site = siteRepository.findByRecommendDomain("localhost");
        Category category = randomCategory(site, ContentType.Link);

        mockMvc.perform(get("/dataSource/findLink/{serial}", String.valueOf(category.getSerial()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0))
        ;
        category.setSerial("444555");
        category = categoryRepository.saveAndFlush(category);
        Link link = randomLink(category);

        mockMvc.perform(get("/dataSource/findLink/{serial}", String.valueOf(category.getSerial()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void testFindArticleContent() throws Exception {
        Site site = siteRepository.findByRecommendDomain("localhost");
        Category category = randomCategory(site, ContentType.Article);
        mockMvc.perform(get("/dataSource/findArticleContent/{serial}", String.valueOf(category.getSerial()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0))
        ;
        category.setSerial("444666");
        category = categoryRepository.saveAndFlush(category);
        Article article = randomArticle(category);

        mockMvc.perform(get("/dataSource/findArticleContent/{serial}", String.valueOf(category.getSerial()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void testFindVideoContent() throws Exception {
        Site site = siteRepository.findByRecommendDomain("localhost");
        Category category = randomCategory(site, ContentType.Video);
        mockMvc.perform(get("/dataSource/findVideoContent/{serial}", String.valueOf(category.getSerial()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0))
        ;
        category.setSerial("444777");
        category = categoryRepository.saveAndFlush(category);
        Video video = randomVideo(category);

        mockMvc.perform(get("/dataSource/findVideoContent/{serial}", String.valueOf(category.getSerial()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }


    @Test
    public void findContentType() throws Exception {
        Site site = siteRepository.findByRecommendDomain("localhost");
        Category category = randomCategory(site, ContentType.Video);
        Video video1 = randomVideo(category);
        Video video2 = randomVideo(category);
        Video video3 = randomVideo(category);
        Video video4 = randomVideo(category);
        Video video5 = randomVideo(category);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> toGet = new HashMap<>();
        toGet.put("contentType", 2);
        toGet.put("draw", 0);
        toGet.put("length", 2);
        toGet.put("pageId", "123");
        toGet.put("search[value]", "");
        String json = mockMvc.perform(get("/dataSource/findContentType")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(toGet))
                .accept(MediaType.APPLICATION_JSON)
        )
                .andReturn().getResponse().getContentAsString();
        Map map = objectMapper.readValue(json, Map.class);
        System.out.println(json);
        List<DataObject> list = (List<DataObject>) map.get("data");
        assertThat(list.size()).isEqualTo(2);


    }

}
