/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.cms.manage.controller.common;

import com.huotu.cms.manage.ManageTest;
import me.jiangcai.lib.resource.service.ResourceService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author CJ
 */
public class ImageControllerTest extends ManageTest {

    @Autowired
    private ImageController imageController;
    @Autowired
    private ResourceService resourceService;

    @Test
    public void go() throws Exception {
        String name = UUID.randomUUID().toString() + "/中/" + UUID.randomUUID().toString();
        resourceService.uploadResource(name + ".png", randomImageStream());

        // 106*82
        mockMvc.perform(get(imageController.forThumbnailForHeight(request, name + ".png", 100)))
                .andExpect(status().is3xxRedirection());
        mockMvc.perform(get(imageController.forThumbnailForHeight(request, name + ".png", 60)))
                .andExpect(status().isOk());

        mockMvc.perform(get(imageController.forThumbnailForWidth(request, name + ".png", 110)))
                .andExpect(status().is3xxRedirection());
        mockMvc.perform(get(imageController.forThumbnailForWidth(request, name + ".png", 100)))
                .andExpect(status().isOk());


    }

}