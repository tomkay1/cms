/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.model;

import lombok.Getter;
import lombok.Setter;


/**
 * Created by chendeyu on 2016/1/5.
 */
@Setter
@Getter
public class LinkCategory extends BaseModel {

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 缩略图uri
     */
    private String thumbUri;

    /**
     * 链接地址
     */
    private String linkUrl;

    /**
     * 所属栏目
     */
    private String categoryName;
}
