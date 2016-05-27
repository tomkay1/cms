package com.huotu.widget.controller.impl;

import com.huotu.widget.controller.PageController;
import com.huotu.widget.model.ResultModel;
import com.huotu.widget.model.ResultOptionsEnum;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by hzbc on 2016/5/27.
 */
@Controller
@RequestMapping("/pages")
public class PageControllerImpl implements PageController {

    @RequestMapping("/pageInfo")
    @ResponseBody
    @Override
    public ResultModel getPage(long customerId){
        //TODO 其他逻辑
        return new ResultModel(ResultOptionsEnum.SUCCESS.getCode(),ResultOptionsEnum.SUCCESS.getValue(),null);
    }

    @RequestMapping(value = "save",method = RequestMethod.PUT)
    @ResponseBody
    @Override
    public ResultModel savePage(){
        //TODO 其他逻辑
        return new ResultModel(ResultOptionsEnum.SUCCESS.getCode(),ResultOptionsEnum.SUCCESS.getValue(),null);
    }

    @RequestMapping(value = "add",method = RequestMethod.POST)
    @ResponseBody
    @Override
    public ResultModel addPage(long customerId){
        //TODO 其他逻辑
        return new ResultModel(ResultOptionsEnum.SUCCESS.getCode(),ResultOptionsEnum.SUCCESS.getValue(),null);
    }

    @RequestMapping(value = "delete",method = RequestMethod.DELETE)
    @ResponseBody
    @Override
    public ResultModel deletePage(long pageId){
        //TODO 其他逻辑
        return new ResultModel(ResultOptionsEnum.SUCCESS.getCode(),ResultOptionsEnum.SUCCESS.getValue(),null);
    }
}
