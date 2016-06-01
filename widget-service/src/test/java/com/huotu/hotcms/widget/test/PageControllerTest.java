package com.huotu.hotcms.widget.test;

import org.junit.Test;

import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Created by wenqi on 2016/5/31.
 */

public class PageControllerTest extends TestBase{

    @Test
    public void testPageSave() throws Exception {
        MvcResult mockResult= mockMvc.perform(post("/pages/pageInfo").param("ownerId", String.valueOf(random.nextInt(100))))
                .andReturn();
       String responseContent= mockResult.getResponse().getContentAsString();
        int status=mockResult.getResponse().getStatus();
        logger.info("返回结果："+responseContent+",返回状态："+status);
    }
}
