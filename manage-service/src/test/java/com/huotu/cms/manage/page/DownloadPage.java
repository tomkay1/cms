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
import com.huotu.hotcms.service.entity.Download;
import org.assertj.core.api.Condition;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Collections;
import java.util.function.Predicate;

/**
 * @author CJ
 */
@BodyId("fa-download")
public class DownloadPage extends AbstractCMSContentPage<Download> {

    /**
     * @param webDriver driver
     */
    public DownloadPage(WebDriver webDriver) {
        super("downloadForm", webDriver);
    }

    @Override
    protected Iterable<Condition<WebElement>> rowPredicateConditions(Download value) {
        return Collections.singleton(new Condition<>(new Predicate<WebElement>() {
            @Override
            public boolean test(WebElement webElement) {
                return webElement.getText().contains(value.getFileName());
            }
        }, "显示文件名"));
    }

    @Override
    protected void fillContentValue(Download value, boolean update) {
        inputText(getForm(), "fileName", value.getFileName());
    }

    @Override
    public void assertResourcePage(Download entity) throws Exception {
        assertInputText(getForm(), "fileName", entity.getFileName());
    }
}
