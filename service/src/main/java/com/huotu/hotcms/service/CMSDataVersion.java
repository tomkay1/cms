/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.hotcms.service;

/**
 * 数据库版本
 *
 * @author CJ
 */
public enum CMSDataVersion {
    init,
    siteRecommendDomain,
    /**
     * 1.1版本相关
     */
    version101000,
    /**
     * 增加页面布局
     */
    version101001,
    /**
     * 增加了一个URL
     */
    galleryItemUrl
}
