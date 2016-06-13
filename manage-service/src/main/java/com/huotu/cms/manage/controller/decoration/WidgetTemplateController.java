package com.huotu.cms.manage.controller.decoration;

import com.alibaba.fastjson.JSONArray;
import com.huotu.cms.manage.common.StringUtil;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.WidgetMains;
import com.huotu.hotcms.service.model.widget.WidgetBase;
import com.huotu.hotcms.service.model.widget.WidgetPage;
import com.huotu.hotcms.service.model.widget.WidgetProperty;
import com.huotu.hotcms.service.repository.WidgetMainsRepository;
import com.huotu.hotcms.service.service.SiteService;
import com.huotu.hotcms.service.util.ResultOptionEnum;
import com.huotu.hotcms.service.util.ResultView;
import com.huotu.hotcms.service.widget.service.PageResolveService;
import com.huotu.hotcms.service.widget.service.PageResourceService;
import com.huotu.hotcms.service.widget.service.WidgetResolveService;
import com.huotu.hotcms.service.widget.service.WidgetResourceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.UUID;

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
    private WidgetResourceService widgetResourceService;

    @Autowired
    private PageResolveService pageResolveService;

    @Autowired
    private SiteService siteService;

    @RequestMapping(value = "/{id}",method = RequestMethod.POST)
    @ResponseBody
    public ResultView getWidgetTemplate(@PathVariable("id") Long id,String layoutId,String guid,String layoutPosition
            ,Long siteId, String properties) {
        ResultView resultView = null;
        try {
            List<WidgetProperty> properties1=null;
            if(null!=properties){
                properties1=JSONArray.parseArray(properties,WidgetProperty.class);
            }
            WidgetMains widgetMains=widgetMainsRepository.findOne(id);
            if(widgetMains!=null&&null!=widgetMains.getResourceUri()) {
                WidgetBase widgetBase = new WidgetBase();
                widgetBase.setId(id);
                if(guid==null||guid==""){
                    widgetBase.setGuid(UUID.randomUUID().toString());
                }else {
                    widgetBase.setGuid(guid);
                }
                widgetBase.setLayoutId(layoutId);
                widgetBase.setLayoutPosition(layoutPosition);
                widgetBase.setWidgetUri(widgetMains.getResourceUri());
                widgetBase.setWidgetEditUri(widgetMains.getResourceEditUri());
                widgetBase.setProperty(properties1);
                widgetBase.setEdit(true);
                Site site=siteService.getSite(siteId);
                String html = widgetResourceService.getWidgetTemplateResolveByWidgetBase(widgetBase,site);
                widgetBase.setHtml(html);
                resultView = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), widgetBase);
            }else{
                resultView = new ResultView(ResultOptionEnum.NOFIND.getCode(), ResultOptionEnum.NOFIND.getValue(), null);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            resultView=new ResultView(ResultOptionEnum.SERVERFAILE.getCode(), ResultOptionEnum.SERVERFAILE.getValue()
                    ,ex.getMessage());
        }
        return resultView;
    }

    @RequestMapping("/edit/{id}")
    @ResponseBody
    public ResultView getWidgetEditTemplate(@PathVariable("id") Long id,String layoutId,String guid
            ,String layoutPosition, String properties){
        ResultView resultView = null;
        try {
            List<WidgetProperty> widgetProperties=null;
            if(!StringUtil.isEmptyStr(properties)){
                widgetProperties=JSONArray.parseArray(properties,WidgetProperty.class);
            }
            WidgetMains widgetMains=widgetMainsRepository.findOne(id);
            if(widgetMains!=null) {
                WidgetBase widgetBase = new WidgetBase();
                widgetBase.setId(id);
                widgetBase.setLayoutId(layoutId);

                widgetBase.setGuid(UUID.randomUUID().toString());
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

    @RequestMapping("/common/head")
    @ResponseBody
    public ResultView getWidgetCommonHeadTemplate(Long siteId){
        ResultView resultView=null;
        try{
            Site site=siteService.getSite(siteId);
            WidgetPage widgetPage=pageResolveService.getWidgetPageByConfig("head.xml", site);
            if(widgetPage!=null){
                String htmlTemplate= pageResourceService.getHtmlTemplateByWidgetPage(widgetPage,false,site);
                resultView = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), htmlTemplate);
            }else{
                resultView = new ResultView(ResultOptionEnum.NOFIND.getCode(), ResultOptionEnum.NOFIND.getValue(), null);
            }
        }catch (Exception ex){
            log.error(ex.getMessage());
            resultView=new ResultView(ResultOptionEnum.SERVERFAILE.getCode(), ResultOptionEnum.SERVERFAILE.getValue(),null);
        }
        return resultView;
    }
}
