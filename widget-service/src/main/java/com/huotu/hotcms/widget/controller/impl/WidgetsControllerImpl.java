/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.controller.impl;

import com.alibaba.fastjson.JSONArray;
import com.huotu.hotcms.widget.controller.WidgetsController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by elvis on 2016/6/7.
 */
public class WidgetsControllerImpl  implements WidgetsController{

    @Override
    @RequestMapping("/widgets/{ownerId}")
    @ResponseBody
    public JSONArray getWidgets(@PathVariable("ownerId") String ownerId) {



        return null;
    }
}
