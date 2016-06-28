/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.page;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.core.io.Resource;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author CJ
 */
public class SitePage extends AbstractContentPage {
    private static final Log log = LogFactory.getLog(SitePage.class);

    @FindBy(id = "fa-sitemap")
    private WebElement body;
    @FindBy(id = "logo-uploader")
    private WebElement uploader;
    @FindBy(id = "addSiteForm")
    private WebElement addSiteForm;

    public SitePage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public void validatePage() {
//        System.out.println(webDriver.getPageSource());
        assertThat(body.isDisplayed()).isTrue();
    }

    public void uploadLogo(String name, Resource resource) throws IOException {
//        Path tempFile = Files.createTempFile(name,name);
//        Files.copy(resource.getInputStream(),tempFile,REPLACE_EXISTING);
//        tempFile.toFile().deleteOnExit();
//        WebElement uploaderFile = uploader.findElement(By.tagName("input"));
//        uploaderFile.sendKeys(tempFile.toString());
    }

    public void addSite(String name, String title, String desc, String[] keywords, String logo, String typeName
            , String copyright, String[] domains, String homeDomain) {
        beforeDriver();
        inputText(addSiteForm, "name", name);
        inputText(addSiteForm, "title", title);
        inputText(addSiteForm, "description", desc);
        inputTags(addSiteForm, "keywords", keywords);
        // TODO logo
        inputSelect(addSiteForm, "siteTypeId", typeName);
        inputText(addSiteForm, "copyright", copyright);
        inputTags(addSiteForm, "domains", domains);
        inputText(addSiteForm, "homeDomain", homeDomain);

        log.info("to click submit for add site.");
//        System.out.println(webDriver.getPageSource());
        addSiteForm.findElement(By.className("btn-primary")).click();
        reloadPageInfo();
    }


}
