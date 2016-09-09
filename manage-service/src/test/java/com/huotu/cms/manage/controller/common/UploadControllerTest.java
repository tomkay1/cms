/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huotu.cms.manage.ManageTest;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author CJ
 */
public class UploadControllerTest extends ManageTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void uploadResource() throws Exception {
        loginAsOwner(testOwner);
        Resource resource = new ClassPathResource("thumbnail.png");

        String responseBody = mockMvc.perform(
                fileUpload("/manage/cms/resourceUpload"
                ).file(new MockMultipartFile("file", "thumbnail.png", null, resource.getInputStream()))
                        .session(session)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.path").isString())
                .andExpect(jsonPath("$.fileUri").isString())
                .andReturn().getResponse().getContentAsString();

        String path = objectMapper.readTree(responseBody).get("path").asText();

        mockMvc.perform(delete("/manage/cms/deleteResource").param("path", path).session(session))
                .andExpect(status().isNoContent());
    }


}