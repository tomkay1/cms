/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.controller;

import com.alibaba.fastjson.JSON;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.repository.AbstractContentRepository;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.widget.entity.PageInfo;
import com.huotu.hotcms.widget.page.Page;
import com.huotu.hotcms.service.repository.PageInfoRepository;
import com.huotu.hotcms.widget.test.TestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <p>针对页面服务controller层{@link com.huotu.hotcms.widget.controller.PageController}的单元测试</p>
 */
public class PageControllerTest extends TestBase {

    @Autowired
    private AbstractContentRepository abstractContentRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PageInfoRepository pageInfoRepository;
    /**
     * 最基本的测试流
     */
    @Test
    public void flow() throws Exception {
        //首先确保虚拟出来的ownerId 并没有存在任何页面
        long ownerId = 200 + random.nextInt(20000);
        mockMvc.perform(get("/owners/{ownerId}/pages", ownerId)
                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));

        // Array of Page
        mockMvc.perform(get("/sites/{siteId}/pages", ownerId)
                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));

        // TODO 在其他逻辑都完成以后 应该创建随机数量的页面,以确保每一项属性
        // 创建一个page
        Page page = randomPage();
        String json = JSON.toJSONString(page);

        // 新建Page
        mockMvc.perform(post("/sites/{siteId}/pages", page.getPageIdentity())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getRedirectedUrl();

        // 保存它 save

        String pageHref = mockMvc.perform(put("/pages/{pageId}", page.getPageIdentity())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getRedirectedUrl();

        // 单独获取
        mockMvc.perform(get(pageHref).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        // TODO 更多数据校验 以确保返回的数据 是之前创建的Page
//                .andExpect()
        ;

        //保存页面部分属性
        String propertyName= UUID.randomUUID().toString();
        mockMvc.perform(delete("/pages/{pageId}/{propertyName}", page.getPageIdentity(),propertyName)).andDo(print())
                .andExpect(status().isAccepted())
                .andReturn();


        mockMvc.perform(get("/owners/{ownerId}/pages", ownerId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
        // https://github.com/jayway/JsonPath
        // TODO 更多数据校验 以确保返回的数据 是之前创建的Page
//        .andExpect(jsonPath("$.[0]").value(..))
        ;

        // 删除
        mockMvc.perform(delete(pageHref))
                .andExpect(status().isNoContent());
        // 现在长度应该是0
        mockMvc.perform(get("/owners/{ownerId}/pages", ownerId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }

    /**
     * 最基本的测试流
     */
    @Test
    public void page() throws Exception {
        String pagePath = "test";
        mockMvc.perform(get("/_web/{pagePath}/", pagePath)
                .accept(MediaType.TEXT_HTML)).andDo(print());

        // Array of Page
        long contentId = 1L;
        com.huotu.hotcms.service.entity.Page page = new com.huotu.hotcms.service.entity.Page();
        page.setTitle("test");
        Link link = new Link();
        link.setId(contentId);
        Category category = new Category();
        category.setId(1L);
        category = categoryRepository.saveAndFlush(category);
        link.setCategory(category);
        page.setCategory(category);
        page.setPagePath("test");
        abstractContentRepository.saveAndFlush(link);
        pageInfoRepository.saveAndFlush(page);
        mockMvc.perform(get("/_web/{pagePath}/{contentId}", pagePath,contentId)
                .accept(MediaType.TEXT_HTML)).andDo(print());

//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))


    }

//    /**
//     * <p>获取页面信息test</p>
//     *
//     * @throws Exception mockMvc异常
//     * @see com.huotu.hotcms.widget.controller.PageController#getPage(long)
//     */
//    @Test
//    public void testGetPage() throws Exception {
//        long ownerId = random.nextInt(100);
//        mockMvc.perform(get("/owners/{ownerId}/pages", ownerId)
//                .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json"))
//                .andReturn();
//    }
//
//    /**
//     * <p>保存界面信息test</p>
//     *
//     * @throws Exception mockMvc异常
//     * @see com.huotu.hotcms.widget.controller.PageController#savePage(String, HttpServletRequest)
//     */
//    @Test
//    public void testSavePage() throws Exception {
//        long pageId = random.nextInt(100);
//
//        Page page = randomPage();
//        String json = JSON.toJSONString(page);
//        mockMvc.perform(put("/pages/{pageId}", pageId)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(json)
//
//        ).andDo(print()).andReturn();
//    }
//
//    /**
//     * <p>新增页面 test</p>
//     *
//     * @throws Exception mockMvc异常
//     * @see com.huotu.hotcms.widget.controller.PageController#addPage(long, HttpServletRequest)
//     */
//    @Test
//    public void testAddPage() throws Exception {
//        long ownerId = random.nextInt(100);
//        Page page = randomPage();
//        String json = JSON.toJSONString(page);
//        mockMvc.perform(post("/owners/{ownerId}/pages", ownerId)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(json))
//                .andDo(print()).andReturn();
//    }
//
//    /**
//     * <p>删除页面 test</p>
//     *
//     * @throws Exception mockMvc异常
//     * @see com.huotu.hotcms.widget.controller.PageController#deletePage(String)
//     */
//    @Test
//    public void testDeletePage() throws Exception {
//        long pageId = random.nextInt(100);
//        mockMvc.perform(delete("/pages/{pageId}", pageId)).andDo(print())
//                .andExpect(status().isAccepted())
//                .andReturn();
//    }
//
//    /**
//     * <p>测试 保存界面部分属性</p>
//     *  @throws Exception mockMvc异常
//     *  @see com.huotu.hotcms.widget.controller.PageController#savePagePartProperties(String, String)
//     */
//    @Test
//    public void testSavePagePartProperties() throws Exception {
//        long pageId = random.nextInt(100);
//        String propertyName= UUID.randomUUID().toString();
//        mockMvc.perform(delete("/pages/{pageId}/{propertyName}", pageId,propertyName)).andDo(print())
//                .andExpect(status().isAccepted())
//                .andReturn();
//    }
}
