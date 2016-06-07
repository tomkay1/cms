package com.huotu.hotcms.widget.controller;

import com.huotu.hotcms.widget.test.TestBase;
import org.junit.Test;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by wenqi on 2016/5/31.
 */

/**
 * <p>针对页面服务controller层{@link com.huotu.hotcms.widget.controller.PageController}的单元测试</p>
 */
public class PageControllerTest extends TestBase {

    /**
     * <p>获取页面信息test</p>
     * @throws Exception mockMvc异常
     * @see com.huotu.hotcms.widget.controller.PageController#getPage(long)
     */
    @Test
    public void testGetPage() throws Exception {
        long ownerId=random.nextInt(100);
        MvcResult mockResult=
                mockMvc.perform(get("/owners/{ownerId}/pages",ownerId)
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(content().contentType("application/json"))
                .andReturn();
       String responseContent= mockResult.getResponse().getContentAsString();
        int status=mockResult.getResponse().getStatus();
        logger.info("返回结果："+responseContent+",返回状态："+status);
    }

    /**
     * <p>保存界面信息test</p>
     * @throws Exception mockMvc异常
     * @see com.huotu.hotcms.widget.controller.PageController#savePage(long)
     */
    @Test
    public void testSavePage() throws Exception {
        long pageId=random.nextInt(100);
        mockMvc.perform(put("/pages/{pageId}",pageId)).andDo(print()).andReturn();
    }

    /**
     * <p>新增页面 test</p>
     * @throws Exception mockMvc异常
     * @see com.huotu.hotcms.widget.controller.PageController#addPage(long)
     */
    @Test
    public void testAddPage() throws Exception {
        long ownerId=random.nextInt(100);
        mockMvc.perform(post("/owners/{ownerId}/pages",ownerId)).andDo(print()).andReturn();
    }

    /**
     * <p>删除页面 test</p>
     * @throws Exception mockMvc异常
     * @see com.huotu.hotcms.widget.controller.PageController#deletePage(long)
     */
    @Test
    public void testDeletePage() throws Exception {
        long pageId=random.nextInt(100);
        mockMvc.perform(delete("/pages/{pageId}", pageId)).andDo(print()).andReturn();
    }
}
