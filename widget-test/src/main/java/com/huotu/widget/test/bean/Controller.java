/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.widget.test.bean;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 将几个常用动作 写成单独文件
 *
 * @author CJ
 */
@org.springframework.stereotype.Controller
public class Controller {

    @RequestMapping(method = RequestMethod.GET, value = "/editor/{widgetName}")
    public String editor(@PathVariable("widgetName") String widgetName, Model model) {
        model.addAttribute("widgetId", widgetName);
        return "editor";
    }

}
