package com.huotu.hotcms.admin.controller;

import com.huotu.hotcms.common.ModelType;
import com.huotu.hotcms.entity.DataModel;
import com.huotu.hotcms.repository.ModelRepository;
import com.huotu.hotcms.service.impl.ModelServiceImpl;
import com.huotu.hotcms.util.PageData;
import com.huotu.hotcms.util.ResultOptionEnum;
import com.huotu.hotcms.util.ResultView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * Created by chendeyu on 2015/12/24.
 */
@Controller
@RequestMapping("/model")
public class ModelController {

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private ModelServiceImpl modelService;

    @RequestMapping("/modellist")
    public ModelAndView modelList(HttpServletRequest request) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/View/system/modellist.html");
        return  modelAndView;
    }

    @RequestMapping("/addModel")
    public ModelAndView addModel(HttpServletRequest request) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/View/system/addModel.html");
        return modelAndView;
    }

    @RequestMapping(value = "/updateModel",method = RequestMethod.POST)
    @ResponseBody
    public ResultView updateModel(@RequestParam(name ="id",required = false) Integer id,
                                  @RequestParam(name = "name",required = true) String name,
                                  @RequestParam(name="description",required = false) String description,
                                  @RequestParam(name = "type",required = true) Integer type,
                                  @RequestParam(name = "orderWeight",required = true,defaultValue ="50") Integer orderWeight){
        ResultView result=null;
        try {
            DataModel dataModel = new DataModel();
            dataModel.setDescription(description);
            dataModel.setName(name);
            dataModel.setOrderWeight(orderWeight);
            dataModel.setType(ModelType.valueOf(type));
            dataModel.setCreateTime(LocalDateTime.now());
            modelRepository.save(dataModel);
            result=new ResultView(ResultOptionEnum.OK.getCode(),ResultOptionEnum.OK.getValue(),null);
        }
        catch (Exception ex)
        {
            result=new ResultView(ResultOptionEnum.FAILE.getCode(),ResultOptionEnum.FAILE.getValue(),null);
        }
        return  result;
    }

    @RequestMapping(value = "/getModelList",method = RequestMethod.POST)
    @ResponseBody
    public PageData<DataModel> getModelList(@RequestParam(name="name",required = false) String name,
                                   @RequestParam(name = "page",required = true,defaultValue = "1") Integer page,
                                   @RequestParam(name = "pagesize",required = true,defaultValue = "20") Integer pageSize){
       PageData<DataModel> pageModel=modelService.getPage(name, page, pageSize);
       return pageModel;
    }
}
