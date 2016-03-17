package com.huotu.hotcms.admin.controller.decoration;

import com.huotu.hotcms.admin.util.web.CookieUser;
import com.huotu.hotcms.service.entity.WidgetMains;
import com.huotu.hotcms.service.entity.WidgetType;
import com.huotu.hotcms.service.service.WidgetService;
import com.huotu.hotcms.service.util.PageData;
import com.huotu.hotcms.service.util.ResultOptionEnum;
import com.huotu.hotcms.service.util.ResultView;
import com.huotu.hotcms.service.widget.service.StaticResourceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by chendeyu on 2016/3/17.
 */
@Controller
@RequestMapping("/widget")
public class WidgetController {

    private static final Log log = LogFactory.getLog(WidgetController.class);

    @Autowired
    private CookieUser cookieUser;

    @Autowired
    private StaticResourceService resourceServer;

    @Autowired
    private WidgetService widgetService;



    @RequestMapping("/widgetTypeList")
    public ModelAndView widgetTypeList() throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/decoration/widget/widgetTypeList.html");
        return  modelAndView;
    }

    @RequestMapping("/widgetMainsList")
    public ModelAndView widgetMainsList() throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/decoration/widget/widgetMainsList.html");
        return  modelAndView;
    }


    @RequestMapping(value = "/addWidgetType")
    public ModelAndView addWidgetType() throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/decoration/widget/addWidgetType.html");
        return  modelAndView;
    }

    @RequestMapping(value = "/addWidgetMains")
    public ModelAndView addWidgetMains() throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        List<WidgetType> widgetTypes = widgetService.findAllWidgetType();
        modelAndView.setViewName("/decoration/widget/addWidgetMains.html");
        modelAndView.addObject("widgetTypes",widgetTypes);
        return  modelAndView;
    }


    @RequestMapping("/updateWidgetMains")
    public ModelAndView updateWidgetMains(@RequestParam(value = "id",defaultValue = "0") Long id) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        try{
            modelAndView.setViewName("/decoration/widget/updateWidgetMains.html");
            WidgetMains widgetMains= widgetService.findWidgetMainsById(id);
            List<WidgetType> widgetTypes = widgetService.findAllWidgetType();
            String logo_uri =null;
            if(!StringUtils.isEmpty(widgetMains.getImageUri())) {
                logo_uri = resourceServer.getResource(widgetMains.getImageUri()).toString();
            }
            modelAndView.addObject("widgetMains",widgetMains);
            modelAndView.addObject("logo_uri",logo_uri);
            modelAndView.addObject("widgetTypes",widgetTypes);
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        return modelAndView;
    }

    @RequestMapping("/updateWidgetType")
    public ModelAndView updateWidgetType(@RequestParam(value = "id",defaultValue = "0") Long id) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        try{
            modelAndView.setViewName("/decoration/widget/updateWidgetType.html");
            WidgetType widgetType= widgetService.findWidgetTypeById(id);
            modelAndView.addObject("widgetType",widgetType);
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        return modelAndView;
    }


    @RequestMapping(value = "/saveWidgetType",method = RequestMethod.POST)
    @Transactional(value = "transactionManager")
    @ResponseBody
    public ResultView saveWidgetType(WidgetType widgetType){
        ResultView result=null;
        try {
            if(widgetType.getId()==null){
                widgetType.setCreateTime(LocalDateTime.now());
            }
            else {
                widgetType.setCreateTime(widgetService.findWidgetTypeById(widgetType.getId()).getCreateTime());
            }
            widgetService.saveWidgetType(widgetType);
            result = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
        }
        catch (Exception ex)
        {
            log.error(ex.getMessage());
            result=new ResultView(ResultOptionEnum.FAILE.getCode(),ResultOptionEnum.FAILE.getValue(),null);
        }
        return  result;
    }

    @RequestMapping(value = "/saveWidgetMains",method = RequestMethod.POST)
    @Transactional(value = "transactionManager")
    @ResponseBody
    public ResultView saveWidgetMains(WidgetMains widgetMains,Long widgetTypeId){
        ResultView result=null;
        try {
            WidgetType widgetType = widgetService.findWidgetTypeById(widgetTypeId);
            if(widgetMains.getId()==null){
                widgetMains.setCreateTime(LocalDateTime.now());
            }
            else {
                widgetMains.setCreateTime(widgetService.findWidgetMainsById(widgetMains.getId()).getCreateTime());
            }
            widgetMains.setWidgetType(widgetType);
            widgetMains.setCreateTime(LocalDateTime.now());
            widgetService.saveWidgetMains(widgetMains);
            result = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
        }
        catch (Exception ex)
        {
            log.error(ex.getMessage());
            result=new ResultView(ResultOptionEnum.FAILE.getCode(),ResultOptionEnum.FAILE.getValue(),null);
        }
        return  result;
    }

    @RequestMapping(value = "/getWidgetTypeList",method = RequestMethod.POST)
    @ResponseBody
    public PageData<WidgetType> getWidgetTypeList(@RequestParam(name="name",required = false) String name,
                                            @RequestParam(name = "page",required = true,defaultValue = "1") Integer page,
                                            @RequestParam(name = "pagesize",required = true,defaultValue = "20") Integer pageSize) {
        PageData<WidgetType> pageModel = null;
        try {
            pageModel = widgetService.getWidgetTypePage(name, page, pageSize);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return pageModel;
    }

    @RequestMapping(value = "/getWidgetMainsList",method = RequestMethod.POST)
    @ResponseBody
    public PageData<WidgetMains> getWidgetMainsList(@RequestParam(name="name",required = false) String name,
                                                  @RequestParam(name = "page",required = true,defaultValue = "1") Integer page,
                                                  @RequestParam(name = "pagesize",required = true,defaultValue = "20") Integer pageSize) {
        PageData<WidgetMains> pageModel = null;
        try {
            pageModel = widgetService.getWidgetMainsPage(name, page, pageSize);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return pageModel;
    }


    @RequestMapping(value = "/deleteWidgetType",method = RequestMethod.POST)
    @ResponseBody
    public ResultView deleteWidgetType(@RequestParam(name = "id",required = true,defaultValue = "0") Long id,HttpServletRequest request) {
        ResultView result=null;
        try{
            if(cookieUser.isSupper(request)) {
                widgetService.delWidgetType(id);
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


    @RequestMapping(value = "/deleteWidgetMains",method = RequestMethod.POST)
    @ResponseBody
    public ResultView deleteWidgetMains(@RequestParam(name = "id",required = true,defaultValue = "0") Long id,HttpServletRequest request) {
        ResultView result=null;
        try{
            if(cookieUser.isSupper(request)) {
                widgetService.delWidgetMains(id);
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
