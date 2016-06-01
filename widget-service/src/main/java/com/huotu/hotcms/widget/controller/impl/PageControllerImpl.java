package com.huotu.hotcms.widget.controller.impl;

import com.huotu.hotcms.widget.controller.PageController;
import com.huotu.hotcms.widget.model.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Random;
import java.util.UUID;

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
        page.setModel(new Random().nextInt(100));
        page.setTest(UUID.randomUUID().toString());
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
