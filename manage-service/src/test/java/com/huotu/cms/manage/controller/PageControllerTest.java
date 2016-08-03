/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huotu.cms.manage.ManageTest;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.widget.entity.PageInfo;
import com.huotu.hotcms.widget.page.PageLayout;
import com.huotu.hotcms.widget.page.PageModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by hzbc on 2016/7/9.
 */
@Transactional
public class PageControllerTest extends ManageTest {

    private static final Log log = LogFactory.getLog(PageControllerTest.class);

    @Test
    public void flow() throws Exception {
        //首先确保虚拟出来的siteId 并没有存在任何页面
        Owner owner = randomOwner();
        Site site = randomSite(owner);
        long siteId = site.getSiteId();
        loginAsOwner(owner);

        mockMvc.perform(
                get("/manage/" + siteId + "/pages")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0))
                .andDo(print());


        //随机创建一个Page
        PageInfo pageInfo = randomPageInfo(site);


        PageLayout page = randomPageLayout();
        PageModel model = new PageModel();
        model.setRoot(page.getRoot());
        model.setTitle(pageInfo.getTitle());
        model.setPageIdentity(pageInfo.getPageId());

        ObjectMapper objectMapper = new ObjectMapper();
        String pageJson = objectMapper.writeValueAsString(model);
        //保存
        mockMvc.perform(put("/manage/pages/{pageId}", pageInfo.getPageId())
                .session(session)
                .accept(MediaType.APPLICATION_JSON)
                .content(pageJson))
                .andExpect(status().isAccepted());

        //获取上面保存的页面信息
        MvcResult result = mockMvc.perform(get("/manage/pages/{pageId}", pageInfo.getPageId())
                .session(session))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        pageJson = result.getResponse().getContentAsString();
        //校验Page信息,并不能直接拿前后得到的Page对象进行比较
        //因为在后续返回的Page中并没有InstallWidget的信息，为null，与randomPage生成的Page肯定不一致
        PageModel getPage = objectMapper.readValue(pageJson, PageModel.class);
        assertThat(getPage)
                .isEqualTo(model);

//        Assert.assertTrue(page.getPageIdentity().equals(getPage.getPageIdentity()));
        //删除
        mockMvc.perform(delete("/manage/pages/{pageId}", pageInfo.getPageId()).session(session))
                .andExpect(status().isAccepted());
        // 删掉之后，页面应该不存在
        mockMvc.perform(get("/manage/pages/{pageId}", pageInfo.getPageId())
                .session(session))
                .andExpect(status().isNotFound())
                .andReturn();
    }

}
