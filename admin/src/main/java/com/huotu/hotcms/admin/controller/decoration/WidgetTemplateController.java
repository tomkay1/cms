package com.huotu.hotcms.admin.controller.decoration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huotu.hotcms.admin.common.StringUtil;
import com.huotu.hotcms.service.entity.WidgetMains;
import com.huotu.hotcms.service.model.widget.WidgetBase;
import com.huotu.hotcms.service.repository.WidgetMainsRepository;
import com.huotu.hotcms.service.util.ResultOptionEnum;
import com.huotu.hotcms.service.util.ResultView;
import com.huotu.hotcms.service.widget.XmlTestService;
import com.huotu.hotcms.service.widget.service.PageResourceService;
import com.huotu.hotcms.service.widget.service.WidgetResolveService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    private XmlTestService xmlTestService;


    @RequestMapping(value = "/{id}",method = RequestMethod.POST)
    @ResponseBody
    public ResultView getWidgetTemplate(@PathVariable("id") Long id,String layoutId,String layoutPosition, String properties) {
        ResultView resultView = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map map=null;
            if(null!=properties){
                map = objectMapper.readValue(properties,Map.class);
            }
            WidgetMains widgetMains=widgetMainsRepository.findOne(id);
            if(widgetMains!=null) {
                WidgetBase widgetBase = new WidgetBase();
                widgetBase.setId(id);
                widgetBase.setLayoutId(layoutId);
                widgetBase.setLayoutPosition(layoutPosition);
                widgetBase.setWidgetUri(widgetMains.getResourceUri());
                widgetBase.setWidgetEditUri(widgetMains.getResourceEditUri());
                String html = pageResourceService.getWidgetTemplateByWidgetBase(widgetBase);
                widgetBase.setProperty(map);
                html=widgetResolveService.widgetBriefView(html,widgetBase);
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
            ObjectMapper objectMapper = new ObjectMapper();
            Map map=null;
            if(!StringUtil.isEmptyStr(properties)){
                map = objectMapper.readValue(properties,Map.class);
            }
            WidgetMains widgetMains=widgetMainsRepository.findOne(id);
            if(widgetMains!=null) {
                WidgetBase widgetBase = new WidgetBase();
                widgetBase.setId(id);
                widgetBase.setLayoutId(layoutId);
                widgetBase.setLayoutPosition(layoutPosition);
                widgetBase.setWidgetUri(widgetMains.getResourceUri());
                widgetBase.setWidgetEditUri(widgetMains.getResourceEditUri());
                widgetBase.setProperty(map);
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
