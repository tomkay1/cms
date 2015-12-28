package com.huotu.hotcms.admin.controller;

import com.huotu.hotcms.admin.util.web.CookieHelper;
import com.huotu.hotcms.admin.util.web.CookieUser;
import com.huotu.hotcms.common.ModelType;
import com.huotu.hotcms.entity.DataModel;
import com.huotu.hotcms.repository.ModelRepository;
import com.huotu.hotcms.service.impl.ModelServiceImpl;
import com.huotu.hotcms.util.PageData;
import com.huotu.hotcms.util.ResultOptionEnum;
import com.huotu.hotcms.util.ResultView;
import org.eclipse.persistence.jpa.jpql.parser.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

/**
 * Created by chendeyu on 2015/12/24.
 */
@Controller
@RequestMapping("/model")
public class ModelController {
    @Autowired
    private  CookieUser cookieUser;

    @Autowired
    private ModelRepository modelRepository;
    @Autowired
    private ModelServiceImpl modelService;

    @RequestMapping("/modellist")
    public ModelAndView modelList(HttpServletRequest request) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/view/system/modelList.html");
        return  modelAndView;
    }

    @RequestMapping("/addModel")
    public ModelAndView addModel(@RequestParam(value = "id", defaultValue = "0") Long id) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/view/system/addModel.html");
        return modelAndView;
    }

    @RequestMapping("/updateModel")
    public ModelAndView updateModel(@RequestParam(value = "id",defaultValue = "0") Long id) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        if(id!=0) {
            DataModel model = modelRepository.findOne(id);
            if (model != null) {
                modelAndView.addObject("model", model);
            }
        }
        modelAndView.setViewName("/view/system/updateModel.html");
        return modelAndView;
    }

    @RequestMapping(value = "/saveModel",method = RequestMethod.POST)
    @ResponseBody
    public ResultView updateModel(@RequestParam(name ="id",required = false,defaultValue = "0") Long id,
                                  @RequestParam(name = "name",required = true) String name,
                                  @RequestParam(name="description",required = false) String description,
                                  @RequestParam(name = "type",required = true) Integer type,
                                  @RequestParam(name = "orderWeight",required = true,defaultValue ="50") Integer orderWeight){
        ResultView result=null;
        try {
            DataModel dataModel = new DataModel();
            if(id!=0)
            {
                dataModel= modelRepository.findOne(id);
                if (dataModel != null) {
                    dataModel.setDescription(description);
                    dataModel.setName(name);
                    dataModel.setOrderWeight(orderWeight);
                    dataModel.setType(ModelType.valueOf(type));
                    dataModel.setUpdateTime(LocalDateTime.now());
                    modelRepository.save(dataModel);
                }
            }
            else {
                dataModel.setDescription(description);
                dataModel.setName(name);
                dataModel.setOrderWeight(orderWeight);
                dataModel.setType(ModelType.valueOf(type));
                dataModel.setCreateTime(LocalDateTime.now());
                modelRepository.save(dataModel);
            }
            result = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
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

    @RequestMapping(value = "/deleteModel",method = RequestMethod.POST)
    @ResponseBody
    public ResultView deleteModel(@RequestParam(name = "id",required = true,defaultValue = "0") Long id,HttpServletRequest request) {
        ResultView result=null;
        try{
            if(cookieUser.isSupper(request)) {
                modelRepository.delete(id);
                result=new ResultView(ResultOptionEnum.OK.getCode(),ResultOptionEnum.OK.getValue(),null);
            }
            else {
                result=new ResultView(ResultOptionEnum.NO_LIMITS.getCode(),ResultOptionEnum.NO_LIMITS.getValue(),null);
            }
        }
        catch (Exception ex)
        {
            result=new ResultView(ResultOptionEnum.FAILE.getCode(),ResultOptionEnum.FAILE.getValue(),null);
        }
        return  result;
    }
}
