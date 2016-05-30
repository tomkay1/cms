package com.huotu.widget.controller.impl;

import com.huotu.widget.controller.DynamicCSSController;
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
