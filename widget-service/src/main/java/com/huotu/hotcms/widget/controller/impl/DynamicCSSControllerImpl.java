/*
 *
 *  版权所有:杭州火图科技有限公司
 *  地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 *  2013-2016. All rights reserved.
 *
 */

package com.huotu.hotcms.widget.controller.impl;

import com.huotu.hotcms.widget.controller.DynamicCSSController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by hzbc on 2016/5/30.
 */

@Controller
@RequestMapping("/css")
public class DynamicCSSControllerImpl implements DynamicCSSController {

    @RequestMapping("/custom/{pageId}.css")
    @Override
    public void getCss(@PathVariable("pageId") long pageId) {

        /*TODO  Problems:
        1.
         */
    }
}
