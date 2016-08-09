/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.page.support;

import com.huotu.hotcms.service.entity.AbstractContent;
import com.huotu.hotcms.service.entity.Category;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.util.StringUtils;

import java.util.function.Predicate;

/**
 * 正文管理页面的基类
 *
 * @author CJ
 */
public abstract class AbstractCMSContentPage<T extends AbstractContent> extends AbstractCRUDPage<T> {

    /**
     * @param formId    form的id
     * @param webDriver driver
     */
    protected AbstractCMSContentPage(String formId, WebDriver webDriver) {
        super(formId, webDriver);
    }

    @Override
    public Predicate<? super WebElement> findRow(T value) {
        return row -> {
            String id = row.getAttribute("data-id");
            return !StringUtils.isEmpty(id) && id.equals(String.valueOf(value.getId()));
        };
    }

    @Override
    protected final void fillValueToForm(T value) {
        Category category = value.getCategory();
        WebElement form = getForm();

        inputText(form, "categoryName", category.getName());
        if (category.getParent() == null) {
            inputSelect(form, "parentCategoryId", "无");
        } else
            inputSelect(form, "parentCategoryId", category.getParent().getName());

        inputText(form, "title", value.getTitle());
        // description

        fillContentValue(value);
    }

    protected abstract void fillContentValue(T value);

    public abstract void assertResourcePage(T entity) throws Exception;
}
