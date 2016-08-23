/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.page.support;

import com.huotu.cms.manage.ManageTest;
import com.huotu.hotcms.service.ResourcesOwner;
import com.huotu.hotcms.service.entity.AbstractContent;
import com.huotu.hotcms.service.entity.Category;
import org.assertj.core.api.Condition;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

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

        commonUpdate(value, form);

        fillContentValue(value, false);
    }

    /**
     * @param value
     * @param form
     * @see com.huotu.hotcms.service.model.ContentExtra#tempPath
     */
    private void commonUpdate(T value, WebElement form) {
        inputText(form, "title", value.getTitle());
        // description

        ManageTest test = (ManageTest) getTestInstance();
        assertThat(test)
                .isNotNull();

        if (value instanceof ResourcesOwner) {
            // 目前就做了这个呀。  资源上传的还不知道怎么弄呢
            // 应该是复制一份资源 然后给它一个新名字
            ResourcesOwner resourcesOwner = (ResourcesOwner) value;

            for (int i = 0; i < resourcesOwner.getResourcePaths().length; i++) {
                if (i == 0) {
                    String path = resourcesOwner.getResourcePaths()[i];
                    if (!StringUtils.isEmpty(path)) {
                        // 复制
                        try {
                            test.uploadResource(this, "tempPath", test.getResourceService().getResource(path));
                        } catch (Exception e) {
                            throw new IllegalStateException(e);
                        }
                    }
                } else
                    throw new IllegalStateException("靠 怎么弄其他图片啊?");
            }
        }
    }

    @Override
    protected final void fillValueToFormForUpdate(T value) {
        WebElement form = getForm();
        commonUpdate(value, form);

        fillContentValue(value, true);
    }

    /**
     * 检查这条记录是否是描述这个资源的
     *
     * @param value 资源
     * @return 可以有茫茫多的检查条件, 但不可以返回null
     */
    protected abstract Iterable<Condition<WebElement>> rowPredicateConditions(T value);

    @Override
    protected Predicate<WebElement> rowPredicate(T value) {
        return row -> {
            try {
                // 找到名字 也就算了
                List<WebElement> tds = row.findElements(By.tagName("td"));
                assertThat(tds)
                        .haveAtLeastOne(new Condition<>(td
                                -> td.getText().contains(value.getTitle()), "需显示标题"));
                assertThat(tds)
                        .haveAtLeastOne(new Condition<>(td
                                -> td.getText().contains(value.getCategory().getName()), "需显示数据源"));
                //                assertThat(tds)
//                        .haveAtLeastOne(new Condition<>(td
//                                -> td.getText().contains(value.getCreateTime().toString()), "需显示时间"));

//                assertThat(tds)
//                        .haveAtLeastOne(new Condition<>(td
//                                -> td.getText().contains(value.getType()), "需显示类型"));

                rowPredicateConditions(value).forEach(assertThat(tds)::haveAtLeastOne);

            } catch (RuntimeException ex) {
                printThisPage();
                throw ex;
            }
            return true;
        };
    }

    /**
     * 将值填入表单中,公共部分和资源部分无需关注
     *
     * @param value  值
     * @param update true表示是更新一个资源否者就是意图新增
     */
    protected abstract void fillContentValue(T value, boolean update);

    /**
     * 鉴定当前打开的这个页面展示的是否是entity,同样无需关注公共和资源部分
     *
     * @param entity
     * @throws Exception
     */
    public abstract void assertResourcePage(T entity) throws Exception;
}
