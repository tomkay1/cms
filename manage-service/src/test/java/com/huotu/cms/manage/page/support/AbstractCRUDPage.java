/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.page.support;

import org.assertj.core.api.Condition;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 具备一个添加form和一个展示数据列表的div
 *
 * @param <T> 相关资源类型
 * @author CJ
 */
public abstract class AbstractCRUDPage<T> extends AbstractContentPage {

    private final String bodyId;
    private final String formId;

    /**
     * @param formId    form的id
     * @param webDriver driver
     */
    protected AbstractCRUDPage(String formId, WebDriver webDriver) {
        super(webDriver);
        this.bodyId = AnnotationUtils.findAnnotation(getClass(), BodyId.class).value();
        this.formId = formId;
    }

    @Override
    public void validatePage() {
        // 无法使用
//        normalValid();
        try {
            assertThat(getBody().isDisplayed())
                    .isTrue();
        } catch (Throwable ex) {
            printThisPage();
            throw ex;
        }
    }

    @Override
    public WebElement getBody() {
        beforeDriver();
        return webDriver.findElement(By.id(bodyId));
    }

    public WebElement getForm() {
        beforeDriver();
        return webDriver.findElement(By.id(formId));
    }

    private <X extends AbstractCRUDPage<T>> X doFormAndSubmit(T value, BiConsumer<AbstractCRUDPage<T>, T> otherDataSubmitter, Runnable runnable) {
        beforeDriver();

        openPanelBody();

        runnable.run();
        if (otherDataSubmitter != null) {
            otherDataSubmitter.accept(this, value);
        }
        getForm().findElement(By.className("btn-primary")).click();
        return (X) initPage(getClass());
    }

    /**
     * 先打开这个添加区域
     */
    protected void openPanelBody() {
        WebElement panel = getForm().findElement(By.className("panel-default"));
        WebElement body = panel.findElement(By.className("panel-body"));
        if (!body.isDisplayed()) {
            panel.findElement(By.cssSelector("a.maximize")).click();
        }
    }

    /**
     * 修改一个资源,并且提交表单
     *
     * @param data               原值
     * @param value              数值
     * @param otherDataSubmitter 作为额外输入字段的辅助,可以添加一个消耗者
     * @param <X>                返回的页面类型
     * @return 添加以后的页面
     */
    public <X extends AbstractCRUDPage<T>> X updateEntityAndSubmit(T data, T value
            , BiConsumer<AbstractCRUDPage<T>, T> otherDataSubmitter) {
        Runnable runnable = () -> fillValueToFormForUpdate(value);

        return doFormAndSubmit(value, otherDataSubmitter, runnable);
    }


    /**
     * 添加一个资源,并且提交表单
     *
     * @param value              数值
     * @param otherDataSubmitter 作为额外输入字段的辅助,可以添加一个消耗者
     * @param <X>                返回的页面类型
     * @return 添加以后的页面
     */
    public <X extends AbstractCRUDPage<T>> X addEntityAndSubmit(T value
            , BiConsumer<AbstractCRUDPage<T>, T> otherDataSubmitter) {
        Runnable runnable = () -> fillValueToForm(value);

        return doFormAndSubmit(value, otherDataSubmitter, runnable);
    }

    /**
     * 将数值表述在表单中
     *
     * @param value 资源值
     */
    protected abstract void fillValueToForm(T value);

    /**
     * 将数值表述在表单中 ,用于更新
     * 不覆盖实现的话,将直接使用{@link #fillValueToForm(Object)}
     *
     * @param value 资源值
     */
    protected void fillValueToFormForUpdate(T value) {
        fillValueToForm(value);
    }

    /**
     * 子类可以替换该实现。
     *
     * @return 所有数据row, 规则是一个资源必然跟一个row配对。
     */
    public List<WebElement> listTableRows() {
        beforeDriver();
        // //*[@id="DataTables_Table_0"]/tbody/tr[1]
        return getBody().findElements(By.cssSelector("tbody>tr"));
    }

    /**
     * 说了是寻找,其实是从{@link #listTableRows()}结果里面找一个符合参数的谓语
     *
     * @param value 目标资源
     * @return 可以判定元素描述的是目标资源的谓语
     */
    public abstract Predicate<? super WebElement> findRow(T value);

    /**
     * 校验这个来自{@link #listTableRows()}数据row完全符合value的数据
     *
     * @param value 目标资源
     * @return 可以判定袁术描述的是'完全'符合目标资源的谓语
     */
    protected abstract Predicate<WebElement> rowPredicate(T value);

    /**
     * 校验这个来自{@link #listTableRows()}数据row完全符合value的数据
     *
     * @param value 目标资源
     * @return 一个校验器
     */
    public Condition<? super WebElement> rowCondition(T value) {
        return new Condition<>(rowPredicate(value), "此元素不符合" + value);
    }

    /**
     * 打开这个element所指引的编辑网页
     *
     * @param webElement 元素row
     * @param <X>        当前类型
     * @return 新的页面
     */
    public final <X extends AbstractCRUDPage<T>> X openResource(WebElement webElement) {
        howToOpenResource(webElement);
        try {
            webDriver.switchTo().alert().accept();
        } catch (Throwable ignored) {
        }
        @SuppressWarnings("unchecked")
        Class<X> clazz = (Class<X>) getClass();
        return initPage(clazz);
    }

    /**
     * 删除操作
     *
     * @param webElement
     * @param <X>
     * @return
     */
    public final <X extends AbstractCRUDPage<T>> X deleteResource(WebElement webElement) {
        toDelete(webElement);
        try {
            webDriver.switchTo().alert().accept();
        } catch (Throwable ignored) {
        }
        @SuppressWarnings("unchecked")
        Class<X> clazz = (Class<X>) getClass();
        return initPage(clazz);
    }


    @SuppressWarnings("WeakerAccess")
    protected void howToOpenResource(WebElement webElement) {
        webElement.findElement(By.className("fa-pencil")).click();
    }

    protected void toDelete(WebElement webElement) {
        webElement.findElement(By.className("fa-trash-o")).click();
    }
}
