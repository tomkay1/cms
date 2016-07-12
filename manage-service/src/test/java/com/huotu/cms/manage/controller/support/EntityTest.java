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

import java.util.Collection;
import java.util.function.BiConsumer;

/**
 * @author CJ
 */
public interface EntityTest<T> {

    Collection<T> list();

    T randomValue();

    BiConsumer<AbstractCRUDPage<T>, T> customAddFunction();
}
