package com.huotu.cms.manage.controller;

import com.huotu.cms.manage.util.web.CookieUser;
import com.huotu.hotcms.service.common.EnumUtils;
import com.huotu.hotcms.service.common.ModelType;
import com.huotu.hotcms.service.entity.DataModel;
import com.huotu.hotcms.service.repository.ModelRepository;
import com.huotu.hotcms.service.service.impl.ModelServiceImpl;
import com.huotu.hotcms.service.util.PageData;
import com.huotu.hotcms.service.util.ResultOptionEnum;
import com.huotu.hotcms.service.util.ResultView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @since 1.0.0
 * @author xhl
 * @time 2015/15/25
 */
@Controller
@RequestMapping("/model")
public class ModelController {
    private static final Log log = LogFactory.getLog(ModelController.class);
    @Autowired
    private CookieUser cookieUser;

    @Autowired
    private ModelRepository modelRepository;
    @Autowired
    private ModelServiceImpl modelService;

    /**
    * 系统模型列表视图
    * */
    @RequestMapping("/modelList")
    public ModelAndView modelList(HttpServletRequest request) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/view/system/modelList.html");
        return  modelAndView;
    }

    /**
     * 增加模型视图
     * */
    @RequestMapping("/addModel")
    public ModelAndView addModel(@RequestParam(value = "id", defaultValue = "0") Long id) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/view/system/addModel.html");
        return modelAndView;
    }

    /**
     * 修改模型视图
     * */
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

    /**
    * 更新系统模型
    * */
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
                    dataModel.setType(EnumUtils.valueOf(ModelType.class, type));
                    dataModel.setUpdateTime(LocalDateTime.now());
                    modelRepository.save(dataModel);
                }
            }
            else {
                dataModel.setDescription(description);
                dataModel.setName(name);
                dataModel.setOrderWeight(orderWeight);
                dataModel.setType(EnumUtils.valueOf(ModelType.class, type));
                dataModel.setCreateTime(LocalDateTime.now());
                modelRepository.save(dataModel);
            }
            result = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
        }
        catch (Exception ex)
        {
            log.error(ex.getMessage());
            result=new ResultView(ResultOptionEnum.FAILE.getCode(),ResultOptionEnum.FAILE.getValue(),null);
        }
        return  result;
    }

    /**
    * 获得模型列表
    * */
    @RequestMapping(value = "/getModelList",method = RequestMethod.POST)
    @ResponseBody
    public PageData<DataModel> getModelList(@RequestParam(name="name",required = false) String name,
                                   @RequestParam(name = "page",required = true,defaultValue = "1") Integer page,
                                   @RequestParam(name = "pagesize",required = true,defaultValue = "20") Integer pageSize) {
        PageData<DataModel> pageModel = null;
        try {
            pageModel = modelService.getPage(name, page, pageSize);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return pageModel;
    }

    /*
    * 删除模型
    * */
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
            log.error(ex.getMessage());
            result=new ResultView(ResultOptionEnum.FAILE.getCode(),ResultOptionEnum.FAILE.getValue(),null);
        }
        return  result;
    }
}
