/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service;

/**
 * Created by wenqi on 2016/7/16.
 */

import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;

/**
 * 可复制的
 * @param <T> 可复制的数据类型
 */
public interface Copyable<T> {
    /**
     * 简单粗暴的复制
     * @return T
     */
    T copy();

    /**
     * <p>在很多内容的复制的时候，需要用到商户站点和复制后的数据源
     * 如果用不到该方法,请直接使用该接口下面的copy()方法</p>
     * @param site 商户站点 如果没有，则为null
     * @param category 复制后的数据源 如果没有，则为null
     * @return 复制后的数据
     * @see #copy()
     */
    T copy(Site site,Category category);
}
