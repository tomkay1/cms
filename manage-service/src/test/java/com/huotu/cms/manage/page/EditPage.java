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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by wenqi on 2016/7/4.
 */
public class EditPage extends AbstractContentPage {

    private static final Log log = LogFactory.getLog(EditPage.class);

    @FindBy(id = "edit")
    private WebElement body;

    @FindBy(id="confBtn")
    private WebElement savaBtn;

    EditPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public void validatePage() {
        assertThat(body.isDisplayed()).isTrue();
    }

    /**
     * 加载组件列表
     */
    public void loadWidget(){

    }

    /**
     * 保存
     */
    public void save(){

    }
}
