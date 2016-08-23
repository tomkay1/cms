/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.page;

import com.huotu.cms.manage.page.support.AbstractCMSContentPage;
import com.huotu.cms.manage.page.support.BodyId;
import com.huotu.hotcms.service.entity.Link;
import org.assertj.core.api.Condition;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Collections;

/**
 * @author CJ
 */
@BodyId("fa-link")
public class LinkPage extends AbstractCMSContentPage<Link> {

    /**
     * @param webDriver driver
     */
    public LinkPage(WebDriver webDriver) {
        super("linkForm", webDriver);
    }

    @Override
    protected void fillContentValue(Link value, boolean update) {
        WebElement form = getForm();
        inputText(form, "linkUrl", value.getLinkUrl());
    }

    @Override
    public void assertResourcePage(Link entity) throws Exception {
        WebElement form = getForm();
        assertInputText(form, "linkUrl", entity.getLinkUrl());
    }

    @Override
    protected Iterable<Condition<WebElement>> rowPredicateConditions(Link value) {
        return Collections.singleton(new Condition<>(td
                -> td.getText().contains(value.getLinkUrl()), "需显示链接"));
    }
}
