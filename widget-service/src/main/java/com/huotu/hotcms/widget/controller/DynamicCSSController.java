/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;

/**
 * 动态CSS服务
 */
@Controller
@RequestMapping("/css")
public interface DynamicCSSController {
    /**
     * 获取页面对应的css
     * @param pageId 页面ID
     */
    void getCss(long pageId);

    /**
     * 拖拽完成后，上传相应的less文件或者代码
     * @param lessFile less文件
     * @param lessString less代码
     */
    void uploadLess(File lessFile,String lessString) throws IOException, InterruptedException;
}
