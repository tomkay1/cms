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
import org.openqa.selenium.By;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.net.URLEncoder;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 测试过滤器是否工作正常
 *
 * @author CJ
 */
@ContextConfiguration(classes = DispatcherServletInitializerTest.ControllerLoader.class)
public class DispatcherServletInitializerTest extends WebTestBase {

    @Test
    public void abc() throws Exception {
        // 不知为何测试
        mockMvc.perform(post("/abc"))
                .andExpect(status().isNotFound())
//                .andDo(print())
        ;

        // 测试非GET不会被转发
        mockMvc.perform(post("/TPATH/TCT"))
                .andExpect(status().isNotFound())
//                .andDo(print())
        ;

        String input = UUID.randomUUID().toString();

//        mockMvc.perform(get("/TPATH/TCT")
//                .param("input", input))
//                .andDo(print())
//                .andExpect(status().isOk())
////                .andExpect(model().attribute("input", input))
//        ;

        driver.get("http://localhost/TPATH/TCT?input=" + URLEncoder.encode(input, "UTF-8"));
        assertThat(driver.findElement(By.tagName("body"))
                .getText()).isEqualTo(input);
    }

    @Controller
    static class TestController {

        @RequestMapping(value = "/_web/TPATH")
        @ResponseStatus(HttpStatus.OK)
        public void testPath() {

        }

        @RequestMapping(value = "/_web/TPATH/TCT")
        @ResponseBody
        public String testPathContent(@RequestParam(value = "input", required = false) String input) {
            return input;
        }

    }

    static class ControllerLoader {
        @Bean
        public TestController testController() {
            return new TestController();
        }
    }


}