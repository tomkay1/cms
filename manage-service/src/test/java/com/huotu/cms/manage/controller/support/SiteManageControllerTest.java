/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller.support;

import com.fasterxml.jackson.databind.JsonNode;
import com.huotu.cms.manage.ManageTest;
import com.huotu.cms.manage.page.EditInPage;
import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.service.service.CategoryService;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author CJ
 */
@Transactional
@ActiveProfiles({"test", "unit_test", "no_ck"})
public class SiteManageControllerTest extends ManageTest {

    private Site site;
    @Autowired
    private CategoryService categoryService;

    @Test
    public void doTest() throws Exception {
        // 所有内容 以及数据源
        Owner owner = randomOwner();
        loginAsOwner(owner);
        site = randomSite(owner);
        randomSiteData(site, true);

        ContentType[] contentTypes = new ContentType[]{
                ContentType.Gallery, ContentType.Article, ContentType.Link, ContentType.Notice
        };

        forContentType("category", contentTypes[random.nextInt(contentTypes.length)]);
        forContentType("category", contentTypes[random.nextInt(contentTypes.length)]);
        // 执行2次 确保无误

        forContentType("gallery");
        forContentType("article");
        forContentType("link");
        forContentType("notice");
//        forContentType("download");
    }

    private void forContentType(String name) throws Exception {
        forContentType(name, null);
    }

    /**
     * 1 检查 URI /manage/name?siteId=x 是否跟data.json一致
     * 2 检查 URI /manage/name/editIn?siteId=x
     *
     * @param name
     * @param fixedType 锁定类型,只有对数据源有用
     */
    private void forContentType(String name, ContentType fixedType) throws Exception {
        // 第一步 检查数据

        MockHttpServletRequestBuilder requestBuilder = get("/manage/" + name)
                .param("siteId", site.getSiteId().toString())
                .session(session)
                .accept(MediaType.APPLICATION_JSON);

        if (fixedType != null) {
            requestBuilder = requestBuilder.param("fixedType", fixedType.name());
        }

        String contentString = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andReturn().getResponse().getContentAsString();

        JsonNode array = objectMapper.readTree(contentString);
        assertSimilarJsonArray(array, new ClassPathResource("web/mock/data.json").getInputStream());

        // 额外步骤 查看render界面
        for (JsonNode content : array) {
            // serial
            mockMvc.perform(get("/manage/" + name + "/render/" + siteAndSerial(site, content.get("serial").asText()))
                    .session(session)
            )
//                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
        }

        // 第二步 检查页面
        StringBuilder urlBuilder = new StringBuilder("http://localhost/testEditIn/");
        urlBuilder.append(name)
                .append("?siteId=")
                .append(site.getSiteId());
        if (fixedType != null) {
            urlBuilder.append("&fixedType=").append(fixedType.name());
        }
        driver.get(urlBuilder.toString());

        WebElement current = driver.findElement(By.id("current"));
        assertThat(current.getText())
                .isEmpty();

        driver.switchTo().frame(driver.findElement(By.tagName("iframe")));
        EditInPage page = initPage(EditInPage.class);
        // 选择一个
        page.chooseRandomOne();

        driver.switchTo().parentFrame();
        assertThat(current.getText())
                .isNotEmpty();
        assertContentType(current, fixedType);

        // 重置再尝试更新
        driver.findElement(By.id("resetButton")).click();
        assertThat(current.getText())
                .isEmpty();

        // 有可能根本没提供更新的功能呢?
        driver.switchTo().frame(driver.findElement(By.tagName("iframe")));
        if (page.ableModify()) {
            page.submitForm();

            driver.switchTo().parentFrame();
            assertThat(current.getText())
                    .isNotEmpty();
            assertContentType(current, fixedType);
            driver.switchTo().frame(driver.findElement(By.tagName("iframe")));
        }

        driver.switchTo().parentFrame();
        // 再度重置
        driver.findElement(By.id("resetButton")).click();
        assertThat(current.getText())
                .isEmpty();

        // 这里存在问题了啊 我们不知道怎么伪造数据。。
        driver.switchTo().frame(driver.findElement(By.tagName("iframe")));

        if (page.ableInsert()) {
            page.newRandomData();
            page.submitForm();

            driver.switchTo().parentFrame();
            assertThat(current.getText())
                    .isNotEmpty();
            assertContentType(current, fixedType);
        }


    }

    private void assertContentType(WebElement current, ContentType fixedType) {
        if (fixedType == null)
            return;

        assertThat(categoryService.get(site, current.getText()))
                .isNotNull();
        assertThat(categoryService.get(site, current.getText()).getContentType())
                .isEqualByComparingTo(fixedType);
    }

}
