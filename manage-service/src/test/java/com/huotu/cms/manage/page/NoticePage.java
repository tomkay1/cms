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
import com.huotu.hotcms.service.entity.Notice;
import org.assertj.core.api.Condition;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Collections;

/**
 * @author CJ
 */
@BodyId("fa-bullhorn")
public class NoticePage extends AbstractCMSContentPage<Notice> {
    /**
     * @param webDriver driver
     */
    public NoticePage(WebDriver webDriver) {
        super("noticeForm", webDriver);
    }

    @Override
    protected Iterable<Condition<WebElement>> rowPredicateConditions(Notice value) {
        return Collections.emptySet();
    }

    @Override
    protected void fillContentValue(Notice value, boolean update) {
        inputText(getForm(), "content", value.getContent());
    }

    @Override
    public void assertResourcePage(Notice entity) throws Exception {
        assertInputText(getForm(), "content", entity.getContent());
    }
}
