/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget;

import java.util.Map;

/**
 * 具备预先处理Context的控件
 * Created by lhx on 2016/8/24.
 */
public interface PreProcessWidget {

    /**
     * 在准备使用这个控件生成的组件执行模板行为的时候运行
     *
     * @param style      样式
     * @param properties 控件参数
     * @param variables  属性
     * @param parameters 请求参数,虽然HTTP支持同名多参数,但CMS系统为了简化设计只支持不同名的参数;是一个可选参数
     * @see CMSContext
     */
    void prepareContext(WidgetStyle style, ComponentProperties properties, Map<String, Object> variables
            , Map<String, String> parameters);
}
