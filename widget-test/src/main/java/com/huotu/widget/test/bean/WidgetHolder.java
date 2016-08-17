/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.widget.test.bean;

import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetLocateService;

import java.util.Set;

/**
 * @author CJ
 */
public interface WidgetHolder extends WidgetLocateService {

    /**
     * 控件集合
     *
     * @return
     */
    Set<Widget> getWidgetSet();

    interface WidgetLoader {
        /**
         * @param className 控件的class name
         * @return 控件实例
         */
        Widget toWidget(String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException;
    }
}
