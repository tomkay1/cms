/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.controller.impl;

import com.huotu.hotcms.widget.controller.PageController;
import com.huotu.hotcms.widget.page.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by hzbc on 2016/5/27.
 */
@Controller
public class PageControllerImpl implements PageController {

    @RequestMapping(value = "/owners/{ownerId}/pages",method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    @ResponseBody
    @Override
    public Page getPage(@PathVariable long ownerId){
        //TODO 其他逻辑

        Page page=new Page();
//        page.setModel(new Random().nextInt(100));
//        page.setTest(UUID.randomUUID().toString());
        return page;
    }

    @RequestMapping(value = "/pages/{pageId}",method = RequestMethod.PUT)
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    @Override
    public void savePage(@PathVariable long pageId){
        //TODO 其他逻辑
    }

    @RequestMapping(value = "/owners/{ownerId}/pages",method = RequestMethod.POST)
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    @Override
    public void addPage(@PathVariable long ownerId){
        //TODO 其他逻辑
    }

    @RequestMapping(value = "/pages/{pageId}",method = RequestMethod.DELETE)
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    @Override
    public void deletePage(@PathVariable long pageId){
        //TODO 其他逻辑
    }
}
