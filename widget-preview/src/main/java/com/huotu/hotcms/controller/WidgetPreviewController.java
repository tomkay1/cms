/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.controller;

import com.huotu.widget.test.bean.WidgetHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author CJ
 */
@Controller
public class WidgetPreviewController {

    @Autowired
    private WidgetHolder widgetHolder;

    @RequestMapping(method = RequestMethod.GET, value = "/index")
    public String index(Model model) {
        model.addAttribute("widgets", widgetHolder.getWidgetSet());
        return "index";
    }

}
