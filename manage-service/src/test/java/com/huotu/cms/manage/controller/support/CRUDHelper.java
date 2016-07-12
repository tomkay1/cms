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

    public static <T> void flow(AbstractCRUDPage<T> page, EntityTest<T> entityTest) {
        // 当前数据
        Collection<T> currentList = entityTest.list();
        //先添加一个
        T randomValue = entityTest.randomValue();

        AbstractCRUDPage<T> page2 = page.addEntityAndSubmit(randomValue, entityTest.customAddFunction());

        assertThat(entityTest.list())
                .hasSize(currentList.size() + 1);

        // 数据测试
        List<WebElement> list = page2.listTableRows();
        assertThat(list)
                .hasSize(entityTest.list().size());

        for (T value : entityTest.list()) {
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
