/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.huotu.cms.manage.ManageTest;
import com.huotu.cms.manage.page.EditInPage;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Owner;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 编辑器内容测试
 *
 * @author CJ
 */
@Transactional
@ActiveProfiles({"test", "unit_test"})
public class EditInTest extends ManageTest {

    private Site site;

    @Test
    public void doTest() throws Exception {
        // 所有内容 以及数据源
        Owner owner = randomOwner();
        loginAsOwner(owner);
        site = randomSite(owner);
        randomSiteData(site, true);

//        forContentType("category");
        forContentType("gallery");
    }

    /**
     * 1 检查 URI /manage/name?siteId=x 是否跟data.json一致
     * 2 检查 URI /manage/name/editIn?siteId=x
     *
     * @param name
     */
    private void forContentType(String name) throws Exception {
        // 第一步 检查数据

        String contentString = mockMvc.perform(get("/manage/" + name)
                .param("siteId", site.getSiteId().toString())
                .session(session)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andReturn().getResponse().getContentAsString();

        JsonNode array = objectMapper.readTree(contentString);
        assertSimilarJsonArray(array, new ClassPathResource("web/mock/data.json").getInputStream());

        // 第二步 检查页面
        driver.get("http://localhost/testEditIn/" + name + "?siteId=" + site.getSiteId());

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
        }


    }

}
