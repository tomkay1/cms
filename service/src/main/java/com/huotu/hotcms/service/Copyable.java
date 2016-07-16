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
}
