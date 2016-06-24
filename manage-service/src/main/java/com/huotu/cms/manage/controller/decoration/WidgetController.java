/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller.decoration;

import com.alibaba.fastjson.JSONArray;
import com.huotu.cms.manage.common.StringUtil;
import com.huotu.cms.manage.util.web.CookieUser;
import com.huotu.hotcms.service.common.ConfigInfo;
import com.huotu.hotcms.service.common.EnumUtils;
import com.huotu.hotcms.service.common.ScopesType;
import com.huotu.hotcms.service.common.SiteType;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.WidgetMains;
import com.huotu.hotcms.service.entity.WidgetType;
import com.huotu.hotcms.service.model.WidgetList;
import com.huotu.hotcms.service.model.widget.WidgetProperty;
import com.huotu.hotcms.service.repository.WidgetTypeRepository;
import com.huotu.hotcms.service.service.SiteService;
import com.huotu.hotcms.service.service.WidgetService;
import com.huotu.hotcms.service.util.HttpUtils;
import com.huotu.hotcms.service.util.PageData;
import com.huotu.hotcms.service.util.ResultOptionEnum;
import com.huotu.hotcms.service.util.ResultView;
import me.jiangcai.lib.resource.service.ResourceService;
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
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by chendeyu on 2016/3/17.
 */
@Controller
@RequestMapping("/manage/widget")
public class WidgetController {

    private static final Log log = LogFactory.getLog(WidgetController.class);

    @Autowired
    private CookieUser cookieUser;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ConfigInfo configInfo;

    @Autowired
    private WidgetService widgetService;

    @Autowired
    private SiteService siteService;

    @Autowired
    private WidgetTypeRepository widgetTypeRepository;

    @RequestMapping("/widgetTypeList")
    public ModelAndView widgetTypeList() throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/decoration/control/widgetTypeList.html");
        return modelAndView;
    }

    @RequestMapping("/widgetMainsList")
    public ModelAndView widgetMainsList() throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        List<WidgetType> widgetTypes = widgetTypeRepository.findAll();
        modelAndView.addObject("widgetTypes", widgetTypes);
        modelAndView.setViewName("/decoration/control/widgetMainsList.html");
        return modelAndView;
    }


    @RequestMapping(value = "/addWidgetType")
    public ModelAndView addWidgetType() throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("scopeTypes", ScopesType.values());
        modelAndView.setViewName("/decoration/control/addWidgetType.html");
        return modelAndView;
    }

    @RequestMapping(value = "/addWidgetMains")
    public ModelAndView addWidgetMains() throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        List<WidgetType> widgetTypes = widgetService.findAllWidgetType();
        modelAndView.setViewName("/decoration/control/addWidgetMains.html");
        modelAndView.addObject("widgetTypes", widgetTypes);
        return modelAndView;
    }

    @RequestMapping(value = "/widgetUpLoadRead")
    public ModelAndView widgetUpLoadRead(@RequestParam(value = "id", defaultValue = "0") Long id) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/decoration/control/widgetRead.html");
        modelAndView.addObject("id", id);
        return modelAndView;
    }

    @RequestMapping(value = "/widgetUpLoadEdit")
    public ModelAndView widgetUpLoadEdit(@RequestParam(value = "id", defaultValue = "0") Long id) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/decoration/control/widgetEdit.html");
        modelAndView.addObject("id", id);
        return modelAndView;
    }


    @RequestMapping("/updateWidgetMains")
    public ModelAndView updateWidgetMains(@RequestParam(value = "id", defaultValue = "0") Long id) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        String logo_uri = null;
        String content = "";
        String editContent = "";
        try {
            modelAndView.setViewName("/decoration/control/updateWidgetMains.html");
            WidgetMains widgetMains = widgetService.findWidgetMainsById(id);
            if (!StringUtils.isEmpty(widgetMains.getImageUri())) {
                logo_uri = resourceService.getResource(widgetMains.getImageUri()).httpUrl().toString();
            }
            if (!StringUtils.isEmpty(widgetMains.getResourceUri())) {
                content = HttpUtils.getHtmlByUrl(resourceService.getResource(widgetMains.getResourceUri()).httpUrl());
            }
            if (!StringUtils.isEmpty(widgetMains.getResourceEditUri())) {
                editContent = HttpUtils.getHtmlByUrl(resourceService.getResource(widgetMains.getResourceEditUri()).httpUrl());
            }


        } catch (Exception ex) {
            log.error(ex.getMessage());
        } finally {
            WidgetMains widgetMains = widgetService.findWidgetMainsById(id);
            List<WidgetType> widgetTypes = widgetService.findAllWidgetType();
            modelAndView.addObject("widgetMains", widgetMains);
            modelAndView.addObject("logo_uri", logo_uri);
            modelAndView.addObject("content", content);
            modelAndView.addObject("editContent", editContent);
            modelAndView.addObject("widgetTypes", widgetTypes);
        }
        return modelAndView;
    }

    @RequestMapping("/updateWidgetType")
    public ModelAndView updateWidgetType(@RequestParam(value = "id", defaultValue = "0") Long id) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.setViewName("/decoration/control/updateWidgetType.html");
            WidgetType widgetType = widgetService.findWidgetTypeById(id);
            modelAndView.addObject("scopeTypes", ScopesType.values());
            modelAndView.addObject("widgetType", widgetType);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return modelAndView;
    }

    @RequestMapping("/widgetList")
    public ModelAndView getWidgetList(Long siteId) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            Site site = siteService.getSite(siteId);
            List<WidgetList> widgetLists = null;
            if (site != null) {
                if (site.getSiteType().equals(SiteType.SITE_PC_SHOP)) {
                    widgetLists = widgetService.findListByNoScopesType(ScopesType.PC_WEBSITE);
                } else if (site.getSiteType().equals(SiteType.SITE_PC_WEBSITE)) {
                    widgetLists = widgetService.findListByNoScopesType(ScopesType.PC_SHOP);
                }
            }
            modelAndView.setViewName("/assets/widget/select.html");
            modelAndView.addObject("widgetList", widgetLists);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return modelAndView;
    }

    @RequestMapping(value = "/saveWidgetType", method = RequestMethod.POST)
    @Transactional(value = "transactionManager")
    @ResponseBody
    public ResultView saveWidgetType(WidgetType widgetType, Integer widgetScopes) {
        ResultView result = null;
        try {
            widgetType.setScenes(EnumUtils.valueOf(ScopesType.class, widgetScopes));
            if (widgetType.getId() == null) {
                widgetType.setCreateTime(LocalDateTime.now());
            } else {
                widgetType.setCreateTime(widgetService.findWidgetTypeById(widgetType.getId()).getCreateTime());
            }
            widgetService.saveWidgetType(widgetType);
            result = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            result = new ResultView(ResultOptionEnum.FAILE.getCode(), ResultOptionEnum.FAILE.getValue(), null);
        }
        return result;
    }

    private boolean verificationDefaultProperty(String defaultProperty) {
        List<WidgetProperty> widgetProperties = null;
        try {
            if (!StringUtil.isEmptyStr(defaultProperty)) {
                widgetProperties = JSONArray.parseArray(defaultProperty, WidgetProperty.class);
                return true;
            }
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/saveWidgetMains", method = RequestMethod.POST)
    @Transactional(value = "transactionManager")
    @ResponseBody
    public ResultView saveWidgetMains(WidgetMains widgetMains, Long widgetTypeId, String template, String editTemplate) {
        ResultView result = null;
        try {
            if(!verificationDefaultProperty(widgetMains.getDefaultsProperty())){
                result = new ResultView(ResultOptionEnum.PARAMERROR.getCode(), ResultOptionEnum.PARAMERROR.getValue(), null);
                return result;
            }
            if (widgetMains.getResourceUri() != null) {
                InputStream inputStream = StringUtil.getInputStream(template);
                widgetMains.setResourceUri(configInfo.getResourcesWidget() + "/template_" + widgetMains.getId() + ".html");
                resourceService.uploadResource(widgetMains.getResourceUri(), inputStream);
            }
            if (widgetMains.getResourceEditUri() != null) {
                InputStream inputStream = StringUtil.getInputStream(editTemplate);
                widgetMains.setResourceEditUri(configInfo.getResourcesWidget() + "/edit/template_" + widgetMains.getId() + ".html");
                resourceService.uploadResource(widgetMains.getResourceEditUri(), inputStream);
            }
            WidgetType widgetType = widgetService.findWidgetTypeById(widgetTypeId);
            if (widgetMains.getId() == null) {//当是新增时
                widgetMains.setCreateTime(LocalDateTime.now());
            } else {
                widgetMains.setCreateTime(widgetService.findWidgetMainsById(widgetMains.getId()).getCreateTime());
            }
            widgetMains.setWidgetType(widgetType);
            widgetMains.setCreateTime(LocalDateTime.now());
            widgetService.saveWidgetMains(widgetMains);
            result = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            result = new ResultView(ResultOptionEnum.FAILE.getCode(), ResultOptionEnum.FAILE.getValue(), null);
        }
        return result;
    }

    @RequestMapping(value = "/getWidgetTypeList", method = RequestMethod.POST)
    @ResponseBody
    public PageData<WidgetType> getWidgetTypeList(@RequestParam(name = "name", required = false) String name,
                                                  @RequestParam(name = "page", required = true, defaultValue = "1") Integer page,
                                                  @RequestParam(name = "pagesize", required = true, defaultValue = "20") Integer pageSize) {
        PageData<WidgetType> pageModel = null;
        try {
            pageModel = widgetService.getWidgetTypePage(name, page, pageSize);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return pageModel;
    }

    @RequestMapping(value = "/getWidgetMainsList", method = RequestMethod.POST)
    @ResponseBody
    public PageData<WidgetMains> getWidgetMainsList(@RequestParam(name = "name", required = false) String name,
                                                    @RequestParam(name = "page", required = true, defaultValue = "1") Integer page,
                                                    @RequestParam(name = "pagesize", required = true, defaultValue = "20") Integer pageSize,
                                                    @RequestParam(name = "widgetTypeId", defaultValue = "0") Long widgetTypeId) {
        PageData<WidgetMains> pageModel = null;
        try {
            pageModel = widgetService.getWidgetMainsPage(name, page, pageSize, widgetTypeId);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return pageModel;
    }

    @RequestMapping(value = "/deleteWidgetType", method = RequestMethod.POST)
    @ResponseBody
    public ResultView deleteWidgetType(@RequestParam(name = "id", required = true, defaultValue = "0") Long id, HttpServletRequest request) {
        ResultView result = null;
        try {
            if (cookieUser.isSupper(request)) {
                widgetService.delWidgetType(id);
                result = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
            } else {
                result = new ResultView(ResultOptionEnum.NO_LIMITS.getCode(), ResultOptionEnum.NO_LIMITS.getValue(), null);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            result = new ResultView(ResultOptionEnum.FAILE.getCode(), ResultOptionEnum.FAILE.getValue(), null);
        }
        return result;
    }

    @RequestMapping(value = "/deleteWidgetMains", method = RequestMethod.POST)
    @ResponseBody
    public ResultView deleteWidgetMains(@RequestParam(name = "id", required = true, defaultValue = "0") Long id, HttpServletRequest request) {
        ResultView result = null;
        try {
            if (cookieUser.isSupper(request)) {
                widgetService.delWidgetMains(id);
                result = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
            } else {
                result = new ResultView(ResultOptionEnum.NO_LIMITS.getCode(), ResultOptionEnum.NO_LIMITS.getValue(), null);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            result = new ResultView(ResultOptionEnum.FAILE.getCode(), ResultOptionEnum.FAILE.getValue(), null);
        }
        return result;
    }

}
