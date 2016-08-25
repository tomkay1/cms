/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.page;

import com.huotu.hotcms.widget.entity.PageInfo;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * 系统和外部(比如我们的拖拽工具)页面的交互格式
 *
 * @author CJ
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Setter
@Getter
public class PageModel extends PageLayout {
    /**
     * 该页面的唯一ID 与PageInfo的pageID保持一致
     * 如果未null表示该页面尚未持久化
     *
     * @see PageInfo#id
     */
//    @XmlAttribute(name = "pageIdentity")
    private Long pageIdentity;

    //    @XmlAttribute(name = "title")
    private String title;

}
