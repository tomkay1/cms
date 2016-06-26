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
import org.springframework.util.StreamUtils;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * @author CJ
 */
public class ResourceControllerTest extends ManageTest {

    @Test
    public void upload() throws Exception {
        loginAsOwner(testOwner);

        Resource resource = new ClassPathResource("log4j2.xml");

        mockMvc.perform(fileUpload("/manage/upload")
                .file("file", StreamUtils.copyToByteArray(resource.getInputStream()))
                .session(session)
        )
                .andDo(print());
    }

}