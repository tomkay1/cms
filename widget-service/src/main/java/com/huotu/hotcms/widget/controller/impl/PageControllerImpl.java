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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by hzbc on 2016/5/27.
 */
@Controller
@RequestMapping("/pages")
public class PageControllerImpl implements PageController {

    @RequestMapping("/pageInfo")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    @ResponseBody
    @Override
    public Page getPage(long ownerId){
        //TODO 其他逻辑

        Page page=new Page();
//        page.setModel(new Random().nextInt(100));
//        page.setTest(UUID.randomUUID().toString());
        return page;
    }

    @RequestMapping(value = "save",method = RequestMethod.PUT)
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    @Override
    public void savePage(){
        //TODO 其他逻辑
    }

    @RequestMapping(value = "add",method = RequestMethod.POST)
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    @Override
    public void addPage(long ownerId){
        //TODO 其他逻辑
    }

    @RequestMapping(value = "delete",method = RequestMethod.DELETE)
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    @Override
    public void deletePage(long pageId){
        //TODO 其他逻辑
    }
}
