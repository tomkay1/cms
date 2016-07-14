/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.page;

import com.huotu.cms.manage.page.support.AbstractCRUDPage;
import com.huotu.cms.manage.page.support.BodyId;
import com.huotu.hotcms.service.entity.Template;
import org.assertj.core.api.Condition;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author CJ
 */
@BodyId("fa-suitcase")
public class TemplatePage extends AbstractCRUDPage<Template> {
    /**
     * @param webDriver driver
     */
    public TemplatePage(WebDriver webDriver) {
        super("templateForm", webDriver);
    }

    @Override
    protected void fillValueToForm(Template value) {
        WebElement form = getForm();
        inputText(form, "name", value.getName());
        //类型不管了 以后可能就不要的呢
    }

    @Override
    public Predicate<? super WebElement> findRow(Template value) {
        return row -> value.getSiteId().toString().equals(row.getAttribute("data-id"));
    }

    @Override
    protected Predicate<WebElement> rowPredicate(Template value) {
        return row -> {
            List<WebElement> tds = row.findElements(By.tagName("td"));
            assertThat(tds).haveAtLeastOne(new Condition<>(td
                    -> td.getText().contains(value.getName()), "显示名称"));
            assertThat(tds).haveAtLeastOne(new Condition<>(td
                    -> td.getText().contains(String.valueOf(value.getScans())), "显示浏览量"));
            assertThat(tds).haveAtLeastOne(new Condition<>(td
                    -> td.getText().contains(String.valueOf(value.getLauds())), "显示点赞数"));
            return true;
        };
    }
}
