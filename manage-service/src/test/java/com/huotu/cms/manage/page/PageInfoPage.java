/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.page;

import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.PageInfo;
import org.assertj.core.api.Condition;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author CJ
 */
public class PageInfoPage extends AbstractContentPage {

    @FindBy(id = "fa-sitemap")
    private WebElement body;
    @FindBy(id = "pageForm")
    private WebElement form;

    public PageInfoPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public WebElement getBody() {
        return body;
    }

    @Override
    public void validatePage() {
//        System.out.println(webDriver.getPageSource());
        normalValid();
    }

    public PageInfoPage addPage(PageInfo value, Category category) {
        inputText(form, "title", value.getTitle());
        inputSelect(form, "typeId", value.getPageType().getValue().toString());
        if (category == null) {
            inputSelect(form, "dataTypeId", "无");
        } else {
            inputSelect(form, "dataTypeId", category.getName());
        }

        inputText(form, "pagePath", value.getPagePath());
        form.findElement(By.className("btn-primary")).click();
        return initPage(PageInfoPage.class);
    }

    public List<WebElement> listTableRows() {
        beforeDriver();
        // //*[@id="DataTables_Table_0"]/tbody/tr[1]
        return body.findElements(By.cssSelector("tbody>tr"));
    }

    public Predicate<? super WebElement> findRow(PageInfo pageInfo) {
        return row -> {
            String id = row.getAttribute("data-id");
            return !StringUtils.isEmpty(id) && id.equals(pageInfo.getPageId().toString());
        };
    }

    public Condition<? super WebElement> rowCondition(PageInfo pageInfo) {
        return new Condition<>(row -> {
            try {
                // 找到名字 也就算了
                List<WebElement> tds = row.findElements(By.tagName("td"));
                assertThat(tds)
                        .haveAtLeastOne(new Condition<>(td
                                -> td.getText().contains(pageInfo.getTitle()), "需显示标题"));

                assertThat(tds)
                        .haveAtLeastOne(new Condition<>(td
                                -> td.getText().contains(pageInfo.getPagePath()), "需显示类型"));

                if (pageInfo.getPageType() != null)
                    assertThat(tds)
                            .haveAtLeastOne(new Condition<>(td
                                    -> td.getText().contains(pageInfo.getPageType().getValue().toString()), "需显示路径"));

                if (pageInfo.getCategory() != null)
                    assertThat(tds)
                            .haveAtLeastOne(new Condition<>(td
                                    -> td.getText().contains(pageInfo.getCategory().getName()), "需显示数据源名"));

            } catch (RuntimeException ex) {
                printThisPage();
                throw ex;
            }
            return true;
        }, "");
    }
}
