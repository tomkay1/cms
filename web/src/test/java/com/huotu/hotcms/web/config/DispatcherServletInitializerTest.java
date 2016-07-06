/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.web.config;

import com.huotu.hotcms.web.WebTestBase;
import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * 测试过滤器是否工作正常
 *
 * @author CJ
 */
public class DispatcherServletInitializerTest extends WebTestBase {

    @Test
    public void abc() throws Exception {
        mockMvc.perform(post("/abc"))
                .andDo(print());
    }


}