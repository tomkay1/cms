/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.page;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.huotu.hotcms.service.entity.Category;
import lombok.Data;

import java.io.Serializable;

/**
 * 页面
 * <p>
 * 包含布局,页面控件,SEO等信息
 * </p>
 *
 * @author CJ
 */
@Data
public class Page implements Serializable {
    /**
     * 该页面的唯一ID，字符串
     */
    private String pageIdentity;

    private String title;

    @JacksonXmlElementWrapper(useWrapping=false)
    private PageElement[] elements;
}
