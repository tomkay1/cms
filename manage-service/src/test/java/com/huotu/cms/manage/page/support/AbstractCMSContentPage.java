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
import com.huotu.hotcms.service.ImagesOwner;
import com.huotu.hotcms.service.entity.AbstractContent;
import com.huotu.hotcms.service.entity.Category;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.util.StringUtils;

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

        if (value instanceof ImagesOwner) {
            // 目前就做了这个呀。  资源上传的还不知道怎么弄呢
            // 应该是复制一份资源 然后给它一个新名字
            ImagesOwner imagesOwner = (ImagesOwner) value;

            for (int i = 0; i < imagesOwner.getImagePaths().length; i++) {
                if (i == 0) {
                    String path = imagesOwner.getImagePaths()[i];
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

    protected abstract void fillContentValue(T value, boolean update);

    public abstract void assertResourcePage(T entity) throws Exception;
}
