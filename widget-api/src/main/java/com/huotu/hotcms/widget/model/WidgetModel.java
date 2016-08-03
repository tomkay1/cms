/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.model;

import com.huotu.hotcms.widget.ComponentProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * WidgetInfo的模型类，用于页面获取已安装Widget列表时使用
 * <p>文档参考：https://quip.com/bU07AX3W15KE</p>
 *
 * @see com.huotu.hotcms.widget.Widget
 */
@Setter
@Getter
public class WidgetModel {

    private String identity;
    private String type;
    private String locallyName;
    private String thumbnail;
    /**
     * 编辑视图
     */
    private String editorHTML;
    private String scriptHref;
    private ComponentProperties defaultProperties;
    private WidgetStyleModel[] styles;

}
