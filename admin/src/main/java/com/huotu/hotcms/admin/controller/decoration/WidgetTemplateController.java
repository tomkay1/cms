package com.huotu.hotcms.admin.controller.decoration;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huotu.hotcms.admin.common.StringUtil;
import com.huotu.hotcms.service.entity.WidgetMains;
import com.huotu.hotcms.service.model.widget.WidgetBase;
import com.huotu.hotcms.service.model.widget.WidgetProperty;
import com.huotu.hotcms.service.repository.WidgetMainsRepository;
import com.huotu.hotcms.service.util.ResultOptionEnum;
import com.huotu.hotcms.service.util.ResultView;
import com.huotu.hotcms.service.widget.service.PageResourceService;
import com.huotu.hotcms.service.widget.service.WidgetResolveService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import java.util.*;

/**
 * <p>
 * 组件模版控制器
 * </p>
 *
 * @version 1.2
 * @since xhl
 */
@Controller
@RequestMapping("/widgetTemplate")
public class WidgetTemplateController {
    private static final Log log = LogFactory.getLog(WidgetTemplateController.class);

    @Autowired
    private PageResourceService pageResourceService;

    @Autowired
    private WidgetMainsRepository widgetMainsRepository;

    @Autowired
    private WidgetResolveService widgetResolveService;

    @Autowired
    private ThymeleafViewResolver widgetViewResolver;

//    @Autowired
//    private XmlTestService xmlTestService;


    @RequestMapping(value = "/{id}",method = RequestMethod.POST)
    @ResponseBody
    public ResultView getWidgetTemplate(@PathVariable("id") Long id,String layoutId,String layoutPosition, String properties) {
        ResultView resultView = null;
        try {
            List<WidgetProperty> properties1=null;
            if(null!=properties){
                properties1=JSONArray.parseArray(properties,WidgetProperty.class);
//                map = objectMapper.readValue(properties,Map.class);
//                properties1=objectMapper.readValue(properties,WidgetProperty[].class);
//                properties1=WidgetResolveService.ConvertWidgetPropertyByMap(map);
            }
            WidgetMains widgetMains=widgetMainsRepository.findOne(id);
            if(widgetMains!=null&&null!=widgetMains.getResourceUri()) {
                WidgetBase widgetBase = new WidgetBase();
                widgetBase.setId(id);
                widgetBase.setLayoutId(layoutId);
                widgetBase.setLayoutPosition(layoutPosition);
                widgetBase.setWidgetUri(widgetMains.getResourceUri());
                widgetBase.setWidgetEditUri(widgetMains.getResourceEditUri());
                widgetBase.setProperty(properties1);
                widgetBase.setEdit(true);
                String html = pageResourceService.getWidgetTemplateResolveByWidgetBase(widgetBase);
                resultView = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), html);
            }else{
                resultView = new ResultView(ResultOptionEnum.NOFIND.getCode(), ResultOptionEnum.NOFIND.getValue(), null);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            resultView=new ResultView(ResultOptionEnum.SERVERFAILE.getCode(), ResultOptionEnum.SERVERFAILE.getValue(),null);
        }
        return resultView;
    }

    @RequestMapping("/edit/{id}")
    @ResponseBody
    public ResultView getWidgetEditTemplate(@PathVariable("id") Long id,String layoutId,String layoutPosition, String properties){
        ResultView resultView = null;
        try {
//            WidgetListProperty<WidgetProperty> widgetPropertyWidgetListProperty=null;
            List<WidgetProperty> widgetProperties=null;
            if(!StringUtil.isEmptyStr(properties)){
//                map = objectMapper.readValue(properties,Map.class);
                widgetProperties=JSONArray.parseArray(properties,WidgetProperty.class);
//                widgetPropertyWidgetListProperty=WidgetResolveService.ConvertWidgetPropertyListByMap(map);
//                widgetProperties=WidgetResolveService.ConvertWidgetPropertyByMap(map);
            }
            WidgetMains widgetMains=widgetMainsRepository.findOne(id);
            if(widgetMains!=null) {
                WidgetBase widgetBase = new WidgetBase();
                widgetBase.setId(id);
                widgetBase.setLayoutId(layoutId);
                widgetBase.setLayoutPosition(layoutPosition);
                widgetBase.setWidgetUri(widgetMains.getResourceUri());
                widgetBase.setWidgetEditUri(widgetMains.getResourceEditUri());
                widgetBase.setProperty(widgetProperties);
                String html=widgetResolveService.widgetEditView(widgetBase);
                resultView = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), html);
            }else{
                resultView = new ResultView(ResultOptionEnum.NOFIND.getCode(), ResultOptionEnum.NOFIND.getValue(), null);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            resultView=new ResultView(ResultOptionEnum.SERVERFAILE.getCode(), ResultOptionEnum.SERVERFAILE.getValue(),null);
        }
        return resultView;
    }
}
