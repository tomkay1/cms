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
import com.huotu.hotcms.service.entity.WidgetInfo;
import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.widget.Component;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetStyle;
import com.huotu.hotcms.widget.entity.PageInfo;
import com.huotu.hotcms.widget.page.Layout;
import com.huotu.hotcms.widget.page.PageElement;
import com.huotu.hotcms.widget.page.PageLayout;
import com.huotu.hotcms.widget.page.PageModel;
import me.jiangcai.lib.resource.Resource;
import me.jiangcai.lib.resource.service.ResourceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 针对页面编辑功能的几个测试,
 * 要求标准<a href="https://huobanplus.quip.com/Y9mVAeo9KnTh">服务标准</a>
 */
@Transactional
public class PageControllerTest extends ManageTest {

    private static final Log log = LogFactory.getLog(PageControllerTest.class);

    @Autowired
    private ResourceService resourceService;

    @Test
    public void previewWidgetEditor() throws Exception {
        loginAsOwner(randomOwner());
        WidgetInfo widgetInfo = randomWidgetInfoValue(null);
        Component component = makeComponent(widgetInfo.getGroupId(), widgetInfo.getArtifactId(), widgetInfo.getVersion());
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> toPost = new HashMap<>();
        mockMvc.perform(
                post("/preview/widgetEditor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.TEXT_HTML)
                        .content(objectMapper.writeValueAsBytes(toPost))
                        .session(session)
        ).andExpect(status().isNotFound());

        toPost.put("widgetIdentity", component.getWidgetIdentity());
        mockMvc.perform(
                post("/preview/widgetEditor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.TEXT_HTML)
                        .content(objectMapper.writeValueAsBytes(toPost))
                        .session(session)
        ).andExpect(status().isNotFound());

        toPost.put("widgetIdentity", component.getWidgetIdentity());
        toPost.put("properties", component.getProperties());
        mockMvc.perform(
                post("/preview/widgetEditor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.TEXT_HTML)
                        .content(objectMapper.writeValueAsBytes(toPost))
                        .session(session)
        ).andDo(print())
                .andExpect(status().isOk());


    }

    @Test
    public void previewComponent() throws Exception {
        loginAsOwner(randomOwner());
        WidgetInfo widgetInfo = randomWidgetInfoValue(null);
//        WidgetInfo widgetInfo = randomWidgetInfoValue("com.huotu.hotcms.widget.productList", "productList", "1.0-SNAPSHOT");
        Component component = makeComponent(widgetInfo.getGroupId(), widgetInfo.getArtifactId(), widgetInfo.getVersion());

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> toPost = new HashMap<>();

        mockMvc.perform(
                post("/preview/component")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.TEXT_HTML)
                        .content(objectMapper.writeValueAsBytes(toPost))
                        .session(session)
        ).andExpect(status().isNotFound());

        toPost.put("widgetIdentity", component.getWidgetIdentity());
        mockMvc.perform(
                post("/preview/component")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.TEXT_HTML)
                        .content(objectMapper.writeValueAsBytes(toPost))
                        .session(session)
        ).andExpect(status().isNotFound());

        toPost.put("widgetIdentity", component.getWidgetIdentity());
        toPost.put("properties", component.getProperties());
        mockMvc.perform(
                post("/preview/component")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.TEXT_HTML)
                        .content(objectMapper.writeValueAsBytes(toPost))
                        .session(session)
        )
                .andExpect(status().isOk());

        toPost.put("id", Math.abs(random.nextLong()));

        mockMvc.perform(
                post("/preview/component")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.TEXT_HTML)
                        .content(objectMapper.writeValueAsBytes(toPost))
                        .session(session)
        )
                .andExpect(status().isOk());


        org.springframework.core.io.Resource widgetCss = component.getInstalledWidget().getWidget()
                .widgetDependencyContent(Widget.CSS);
        boolean cssExisting = widgetCss != null && widgetCss.exists();
        for (WidgetStyle style : component.getInstalledWidget().getWidget().styles()) {
            toPost.put("styleId", style.id());
            toPost.put("componentId", "efg");
            MockHttpServletResponse response = mockMvc.perform(
                    post("/preview/component")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.TEXT_HTML)
                            .content(objectMapper.writeValueAsBytes(toPost))
                            .session(session)
            )
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                    .andDo(print()).andReturn().getResponse();
            String header = response.getHeader("cssPath");
            String location = response.getHeader("cssLocation");

            if (cssExisting) {
                assertThat(location).isNotEmpty();
                Resource resource = resourceService.getResource(header);
                assertThat(resource).isNotNull();
                assertThat(resource.exists()).isTrue();

                StreamUtils.copy(resource.getInputStream(), System.out);

            } else {
                assertThat(location).isEmpty();
            }
        }

    }

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
        // 不包含一个组件就不算
        while (haveNoComponent(page))
            page = randomPageLayout();
        PageModel model = new PageModel();
        model.setRoot(page.getRoot());
        model.setTitle(pageInfo.getTitle());
        model.setPageIdentity(pageInfo.getId());

        ObjectMapper objectMapper = new ObjectMapper();
        String pageJson = objectMapper.writeValueAsString(model);
        //保存
        mockMvc.perform(put("/manage/pages/{id}", pageInfo.getId())
                .session(session)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(pageJson))
                .andExpect(status().isAccepted());

        //获取上面保存的页面信息
        MvcResult result = mockMvc.perform(get("/manage/pages/{id}", pageInfo.getId())
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
        // 略有不同的是 必然有previewHtml
        for (Layout layout : PageLayout.NoNullLayout(getPage)) {
            for (PageElement pageElement : layout.elements()) {
                if (pageElement instanceof Component) {
                    Component component = (Component) pageElement;
                    assertThat(component.getPreviewHTML())
                            .isNotEmpty();
                }
            }
        }

//        Assert.assertTrue(page.getPageIdentity().equals(getPage.getPageIdentity()));
        //删除
        mockMvc.perform(delete("/manage/pages/{id}", pageInfo.getId()).session(session))
                .andExpect(status().isAccepted());
        // 删掉之后，页面应该不存在
        mockMvc.perform(get("/manage/pages/{id}", pageInfo.getId())
                .session(session))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    private boolean haveNoComponent(PageLayout page) {
        for (Layout layout : PageLayout.NoNullLayout(page)) {
            for (PageElement pageElement : layout.elements()) {
                if (pageElement instanceof Component) {
                    return false;
                }
            }
        }
        return true;
    }

}
