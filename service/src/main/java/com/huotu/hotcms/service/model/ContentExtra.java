/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.model;

import lombok.Data;

/**
 * 用于内容在进行新增和修改时候的额外数据
 * Created by wenqi on 2016/7/21.
 */
@Data
public class ContentExtra {
    /**
     *  数据源ID
     */
    private String categoryName;
    /**
     * 父类数据源ID
     */
    private Long parentCategoryId;

    /**
     * 原先缩略图地址
     */
    private String oldResourcesUri;
}
