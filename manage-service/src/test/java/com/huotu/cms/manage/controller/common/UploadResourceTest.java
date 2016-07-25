/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller.common;

import com.huotu.cms.manage.ManageTest;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UploadResourceTest extends ManageTest {

    @Test
    public void uploadResource() throws Exception {
        loginAsOwner(testOwner);
        Resource resource = new ClassPathResource("thumbnail.png");

        mockMvc.perform(
                post("/manage/cms/resourceUpload"
                        , new MockMultipartFile("file", resource.getInputStream())).session(session)
        ).andExpect(status().isOk());
    }

    @Test
    public void deleteResource() throws Exception {
        loginAsOwner(testOwner);
        mockMvc.perform(delete("/manage/cms/deleteResource", "thumbnail.png").session(session))
                .andExpect(status().isOk());
    }

}
