/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.hotcms.service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GalleryItemModel {
    private Long id;
    private String thumbUri;
    private String name;
    private String size;
    private int orderWeight;
    /**
     * @since 1.3.4
     */
    private String relationalUrl;
}
