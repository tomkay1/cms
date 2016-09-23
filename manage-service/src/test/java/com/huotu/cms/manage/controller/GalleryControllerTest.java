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
import com.huotu.cms.manage.ContentManageTest;
import com.huotu.cms.manage.page.GalleryPage;
import com.huotu.hotcms.service.Serially;
import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Gallery;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.SiteAndSerial;
import com.huotu.hotcms.service.util.ImageHelper;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author CJ
 */
public class GalleryControllerTest extends ContentManageTest<Gallery> {

    public GalleryControllerTest() {
        super(ContentType.Gallery, GalleryPage.class);
    }

    @Test
    @Transactional
    public void items() throws Exception {
        Site site = loginAsSite();
        // /manage/gallery/{id}/items

        // 创建一个Gallery
        Category category = randomCategory();
        category.setSite(site);
        category.setContentType(ContentType.Gallery);
        Gallery gallery = randomGallery(category);

        mockMvc.perform(get("/manage/gallery/{id}/items", gallery.getId())
                .session(session)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        List<String> toPostItemNames = new ArrayList<>();

        int count = random.nextInt(10) + 4;
        while (count-- > 0)
            toPostItemNames.add("中" + UUID.randomUUID().toString());

        for (String name : toPostItemNames) {
            mockMvc.perform(fileUpload("/manage/gallery/{id}/items", gallery.getId())
                    .file("qqfile", StreamUtils.copyToByteArray(new ClassPathResource("web/mock/sexy.jpg").getInputStream()))
                    .session(session)
                    .accept(MediaType.APPLICATION_JSON)
                    .param("qquuid", UUID.randomUUID().toString())
                    .param("qqfilename", name)
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.newUuid").isString());
        }
        // 先post

        // 查看下数量
        // 我们还支持了新标准SiteAndSerial
        mockMvc.perform(get("/manage/gallery/{id}/items2", siteAndSerial(site, gallery))
                .session(session)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(toPostItemNames.size()));

        String responseString = mockMvc.perform(get("/manage/gallery/{id}/items", gallery.getId())
                .session(session)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(toPostItemNames.size()))
                .andReturn().getResponse().getContentAsString();

        JsonNode items = objectMapper.readTree(responseString);
        assertSimilarJsonArray(items, new ClassPathResource("web/mock/galleryItem.json").getInputStream());

        //
        for (JsonNode item : items) {
            String name = item.get("name").asText();
            assertThat(name)
                    .isIn(toPostItemNames);
            String thumbnailUrl = item.get("thumbnailUrl").asText();
            // 这是一个相对uri
            //
            Resource urlResource;
            if (thumbnailUrl.startsWith("http://"))
                urlResource = new UrlResource(thumbnailUrl);
            else
                urlResource = new ByteArrayResource(
                        mockMvc.perform(get(thumbnailUrl).session(session).accept("*/*"))
//                            .andDo(print())
                                .andExpect(status().isOk())
                                .andReturn()
                                .getResponse()
                                .getContentAsByteArray());
            ImageHelper.assertSame(urlResource, new ClassPathResource("web/mock/sexy.jpg"));
        }

        // 删除
        int remaining = toPostItemNames.size();
        for (JsonNode item : items) {
            remaining--;
            mockMvc.perform(delete("/manage/gallery/{id}/items/{uuid}", gallery.getId(), item.get("uuid").asText())
                            .session(session)
//                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isNoContent());

            mockMvc.perform(get("/manage/gallery/{id}/items", gallery.getId())
                    .session(session)
                    .accept(MediaType.APPLICATION_JSON)
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(remaining));
        }

    }

    @NotNull
    private SiteAndSerial siteAndSerial(Site site, Serially serial) {
        return new SiteAndSerial(site, serial.getSerial());
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