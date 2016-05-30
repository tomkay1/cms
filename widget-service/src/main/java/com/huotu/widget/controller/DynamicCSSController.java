package com.huotu.widget.controller;

/**
 * Created by hzbc on 2016/5/30.
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
