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
import org.openqa.selenium.WebElement;

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

    public static <T> void flow(AbstractCRUDPage<T> page, CRUDTest<T> testInstance) {
        //先添加一个
        T randomValue = testInstance.randomValue();
        // 当前数据
        Collection<T> currentList = testInstance.list();

        page.refresh();

        AbstractCRUDPage<T> page2 = page.addEntityAndSubmit(randomValue, testInstance.customAddFunction());

        assertThat(testInstance.list().size())
                .isGreaterThan(currentList.size());

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
        }

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
    }

}
