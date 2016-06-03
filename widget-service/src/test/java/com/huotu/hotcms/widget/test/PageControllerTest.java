package com.huotu.hotcms.widget.test;

import org.junit.Test;

import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * Created by wenqi on 2016/5/31.
 */

public class PageControllerTest extends TestBase{

    @Test
    public void testPageInfo() throws Exception {
        MvcResult mockResult=
                mockMvc.perform(get("/pages/pageInfo")
                        .param("ownerId", String.valueOf(random.nextInt(100))))
                        .andDo(print())
                .andReturn();
       String responseContent= mockResult.getResponse().getContentAsString();
        int status=mockResult.getResponse().getStatus();
        logger.info("返回结果："+responseContent+",返回状态："+status);
    }

    @Test
    public void testReplace(){
        String s="com.huotu.cms";
        s=s.replace(".","/");
        System.out.println(s);
    }
}
