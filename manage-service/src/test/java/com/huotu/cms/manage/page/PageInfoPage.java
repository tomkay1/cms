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
import com.huotu.hotcms.service.entity.PageInfo;
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
public class PageInfoPage extends AbstractCRUDPage<PageInfo> {

    public PageInfoPage(WebDriver webDriver) {
        super("fa-sitemap", "pageForm", webDriver);
    }

    @Override
    protected void fillValueToForm(PageInfo value) {
        inputText(form, "title", value.getTitle());
        inputSelect(form, "typeId", value.getPageType().getValue().toString());
        if (value.getCategory() == null) {
            inputSelect(form, "dataTypeId", "无");
        } else {
            inputSelect(form, "dataTypeId", value.getCategory().getName());
        }
        inputText(form, "pagePath", value.getPagePath());
    }

    public Predicate<? super WebElement> findRow(PageInfo pageInfo) {
        return row -> {
            String id = row.getAttribute("data-id");
            return !StringUtils.isEmpty(id) && id.equals(pageInfo.getPageId().toString());
        };
    }

    @Override
    protected Predicate<WebElement> rowPredicate(PageInfo value) {
        return row -> {
            try {
                // 找到名字 也就算了
                List<WebElement> tds = row.findElements(By.tagName("td"));
                assertThat(tds)
                        .haveAtLeastOne(new Condition<>(td
                                -> td.getText().contains(value.getTitle()), "需显示标题"));

                assertThat(tds)
                        .haveAtLeastOne(new Condition<>(td
                                -> td.getText().contains(value.getPagePath()), "需显示类型"));

                assertThat(tds)
                        .haveAtLeastOne(new Condition<>(td
                                -> td.getText().contains(value.getPageType().getValue().toString()), "需显示路径"));

                if (value.getCategory() != null)
                    assertThat(tds)
                            .haveAtLeastOne(new Condition<>(td
                                    -> td.getText().contains(value.getCategory().getName()), "需显示数据源名"));

            } catch (RuntimeException ex) {
                printThisPage();
                throw ex;
            }
            return true;
        };
    }
}
