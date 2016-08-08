/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller.support;

import com.huotu.cms.manage.page.support.AbstractCRUDPage;
import com.huotu.hotcms.service.Auditable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.assertj.core.api.Condition;
import org.openqa.selenium.WebElement;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 帮忙测试一些CRUD的东西
 *
 * @author CJ
 */
public class CRUDHelper {

    private static final Log log = LogFactory.getLog(CRUDHelper.class);

    public static <T> void flow(AbstractCRUDPage<T> page, CRUDTest<T> testInstance) throws Exception {
        page.assertNoDanger();
        //先添加一个
        T randomValue = testInstance.randomValue();
        // 当前数据
        Collection<T> currentList = testInstance.list();

        page.refresh();
        page.assertNoDanger();

        AbstractCRUDPage<T> page2 = page.addEntityAndSubmit(randomValue, testInstance.customAddFunction());
        String error = testInstance.errorMessageAfterAdd(randomValue);
        if (error == null)
            page2.assertNoDanger();
        else {
            try {
                page2.assertDanger().haveAtLeastOne(new Condition<>((input) -> input.contains(error), "应该拥有这个错误。"));
            } finally {
                //noinspection ThrowFromFinallyBlock
                page2.closeDanger();
            }
            assertThat(testInstance.list().size())
                    .isEqualTo(currentList.size());
            return;
        }

        assertThat(testInstance.list().size())
                .isGreaterThan(currentList.size());

        // 数据测试
        List<WebElement> list = page2.listTableRows();
        assertThat(list)
                .hasSize(testInstance.list().size());

        for (T value : testInstance.list()) {
            WebElement rowElement = list.stream()
                    .filter(page2.findRow(value))
                    .findAny().orElseThrow(() -> {
                        page2.printThisPage();
                        return new IllegalStateException("应该显示该资源" + value);
                    });
            assertThat(rowElement)
                    .is(page2.rowCondition(value));
        }


        Collection<T> allList = testInstance.list();
        allList.removeAll(currentList);
        // 剩下的应该就是新增的元素了 如果存在多个 就表示这个单体数据测试无法进行
        if (allList.size() == 1) {
            log.info("only one Entity added, run assertCreation");
            T entity = allList.iterator().next();
            if (entity instanceof Auditable) {
                assertThat(((Auditable) entity).getCreateTime())
                        .isNotNull();
            }
            testInstance.assertCreation(entity, randomValue);

            if (testInstance.open() || testInstance.modify()) {
                log.debug("测试打开资源");
                WebElement row = list.stream()
                        .filter(page2.findRow(entity))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("新增的资源没有在列表中被发现。"));

                AbstractCRUDPage<T> editPage = page2.openResource(row);
                editPage.assertNoDanger();

                // 检查这个数据row
                try {
                    testInstance.assertResourcePage(editPage, entity);
                } catch (Throwable ex) {
                    editPage.printThisPage();
                    throw ex;
                }

                log.debug("测试编辑资源");
                if (testInstance.modify()) {

                    // 允许修改的字段
                    PropertyDescriptor[] allPropertyDescriptors = BeanUtils.getPropertyDescriptors(entity.getClass());
                    allPropertyDescriptors = Arrays.stream(allPropertyDescriptors)
                            .filter(propertyDescriptor -> propertyDescriptor.getWriteMethod() != null && propertyDescriptor.getReadMethod() != null)
                            .toArray(PropertyDescriptor[]::new);

                    T toModify = (T) entity.getClass().newInstance();
                    for (PropertyDescriptor propertyDescriptor : allPropertyDescriptors) {
                        Object obj = propertyDescriptor.getReadMethod().invoke(entity);
                        propertyDescriptor.getWriteMethod().invoke(toModify, obj);
                    }

                    // 应用随机值
                    PropertyDescriptor[] fields = Arrays.stream(allPropertyDescriptors)
                            .filter(testInstance.editableProperty())
                            .toArray(PropertyDescriptor[]::new);
                    T anotherValue = testInstance.randomValue();

                    for (PropertyDescriptor propertyDescriptor : fields) {
                        Object obj = propertyDescriptor.getReadMethod().invoke(anotherValue);
                        propertyDescriptor.getWriteMethod().invoke(toModify, obj);
                    }

                    //准备提交toModify
                    AbstractCRUDPage<T> afterModifyPage = editPage.addEntityAndSubmit(toModify, testInstance.customAddFunction());
                    afterModifyPage.assertNoDanger();

                    if (entity instanceof Auditable) {
                        assertThat(((Auditable) entity).getUpdateTime())
                                .isNotNull();
                    }
                    testInstance.assertCreation(entity, toModify);

                }
            }

        } else if (testInstance.modify()) {
            throw new IllegalStateException("需要进行修改测试,但没法确定新增的资源。f");
        }
    }

}
