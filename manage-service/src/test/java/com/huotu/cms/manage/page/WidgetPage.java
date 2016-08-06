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
import com.huotu.hotcms.service.entity.WidgetInfo;
import org.assertj.core.api.Condition;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author CJ
 */
@BodyId("fa-asterisk")
public class WidgetPage extends AbstractCRUDPage<WidgetInfo> {

    public WidgetPage(WebDriver webDriver) {
        super("widgetForm", webDriver);
    }

    @Override
    protected void fillValueToForm(WidgetInfo widgetInfo) {
        WebElement form = getForm();
        inputText(form, "groupId", widgetInfo.getGroupId());
        inputText(form, "artifactId", widgetInfo.getArtifactId());
        inputText(form, "version", widgetInfo.getVersion());
        inputText(form, "type", widgetInfo.getType());
        if (widgetInfo.getOwner() != null) {
            inputSelect(form, "ownerId", widgetInfo.getOwner().getUsername());
        }
        // jar
    }

    @Override
    public Predicate<? super WebElement> findRow(WidgetInfo value) {
        return row -> {
            String id = row.getAttribute("data-id");
            return !StringUtils.isEmpty(id) && id.equals(value.getIdentifier().toString());
        };
    }

    @Override
    protected Predicate<WebElement> rowPredicate(WidgetInfo value) {
        return row -> {
            try {
                // 找到名字 也就算了
                List<WebElement> tds = row.findElements(By.tagName("td"));
                assertThat(tds)
                        .haveAtLeastOne(new Condition<>(td
                                -> td.getText().contains(value.getArtifactId()), "需显示artifactId"));

                assertThat(tds)
                        .haveAtLeastOne(new Condition<>(td
                                -> td.getText().contains(value.getVersion()), "需显示version"));

                assertThat(tds)
                        .haveAtLeastOne(new Condition<>(td
                                -> td.getText().contains(value.getType().trim()), "需显示类型"));

                if (value.getOwner() != null)
                    assertThat(tds)
                            .haveAtLeastOne(new Condition<>(td
                                    -> td.getText().contains(value.getOwner().getUsername()), "需显示专享商户"));

            } catch (RuntimeException ex) {
                printThisPage();
                throw ex;
            }
            return true;
        };
    }
}
