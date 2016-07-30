/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.controller;

import com.huotu.hotcms.widget.Component;
import com.huotu.hotcms.widget.test.TestBase;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 在描述一个widget中携带的widgetHref是我们一个{@link WidgetController}负责解析的
 * 这个测试就是检查这个地址是有效可用的
 *
 * @author CJ
 */
public class WidgetControllerTest extends TestBase {

    @Test
    @Transactional
    public void js() throws Exception {
        Component component = randomComponent();


        mockMvc.perform(
                put("/widget/{widget}.js", component.getWidgetIdentity())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.parseMediaType("application/javascript")))
        ;

    }

}
