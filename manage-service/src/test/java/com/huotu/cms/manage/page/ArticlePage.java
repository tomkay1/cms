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
import com.huotu.hotcms.service.entity.Article;
import org.assertj.core.api.Condition;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 文章管理页面
 */
@BodyId("fa-tasks")
public class ArticlePage extends AbstractCMSContentPage<Article> {

    /**
     * @param webDriver driver
     */
    public ArticlePage(WebDriver webDriver) {
        super("articleForm", webDriver);
    }

//    @Override
//    protected void fillValueToForm(Article article) {
//        WebElement form = getForm();
//        inputText(form, "title", article.getTitle());
//        inputText(form, "categoryName", article.getCategory().getName());
//        inputText(form, "parentCategoryId", String.valueOf(article.getCategory().getParent().getId()));
//        inputText(form, "type", article.getType());
//        inputText(form, "articleSource", article.getArticleSource().name());
//        inputText(form, "createTime", article.getCreateTime().toString());
//    }

    @Override
    protected Predicate<WebElement> rowPredicate(Article value) {
        return row -> {
            try {
                // 找到名字 也就算了
                List<WebElement> tds = row.findElements(By.tagName("td"));
                assertThat(tds)
                        .haveAtLeastOne(new Condition<>(td
                                -> td.getText().contains(value.getTitle()), "需显示标题"));

                assertThat(tds)
                        .haveAtLeastOne(new Condition<>(td
                                -> td.getText().contains(value.getCreateTime().toString()), "需显示时间"));

                assertThat(tds)
                        .haveAtLeastOne(new Condition<>(td
                                -> td.getText().contains(value.getType()), "需显示类型"));

                if (value.getCategory() != null)
                    assertThat(tds)
                            .haveAtLeastOne(new Condition<>(td
                                    -> td.getText().contains(value.getCategory().getParent().getName()), "需显示父级数据源名"));

            } catch (RuntimeException ex) {
                printThisPage();
                throw ex;
            }
            return true;
        };
    }
}
