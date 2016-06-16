/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller;

import com.huotu.cms.manage.util.web.CookieUser;
import com.huotu.hotcms.service.common.EnumUtils;
import com.huotu.hotcms.service.common.RouteType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Route;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.service.CategoryService;
import com.huotu.hotcms.service.service.SiteService;
import com.huotu.hotcms.service.service.impl.RouteServiceImpl;
import com.huotu.hotcms.service.util.PageData;
import com.huotu.hotcms.service.util.ResultOptionEnum;
import com.huotu.hotcms.service.util.ResultView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.plexus.util.StringUtils;
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
 * Created by Administrator xhl 2016/1/9.
 */
@Controller
@RequestMapping("/route")
public class RouteController {
    private static final Log log = LogFactory.getLog(RouteController.class);

    @Autowired
    private CookieUser cookieUser;

    @Autowired
    private SiteService siteService;

    @Autowired
    private RouteServiceImpl routeService;

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = "/routeList")
    public ModelAndView routeList(){
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/view/web/routeList.html");
        return  modelAndView;
    }

    @RequestMapping(value = "/addRoute")
    public ModelAndView addRoute(@RequestParam( value = "siteId") Long siteId){
        ModelAndView modelAndView=new ModelAndView();
        try {
            modelAndView.setViewName("/view/web/addRoute.html");
            Site site=siteService.getSite(siteId);
            modelAndView.addObject("site",site);
            modelAndView.addObject("routeTypes", RouteType.values());
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        return  modelAndView;
    }

    /**
     * 修改路由
     * @param routeId
     * @return
     */
    @RequestMapping(value = "/updateRoute")
    public ModelAndView updateRoute(@RequestParam( value = "id") Long routeId){
        ModelAndView modelAndView=new ModelAndView();
        try{
            modelAndView.setViewName("/view/web/updateRoute.html");
            Route route=routeService.getRoute(routeId);
            modelAndView.addObject("site",route.getSite());
            modelAndView.addObject("route",route);
            modelAndView.addObject("routeTypes", RouteType.values());
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        return modelAndView;
    }

    /**
     * 获得路由列表
     * */
    @RequestMapping(value = "/getRouteList",method = RequestMethod.POST)
    @ResponseBody
    public PageData<Route> getModelList(@RequestParam(name="siteId",required = false) Long siteId,
                                        @RequestParam(name="description",required = false) String description,
                                            @RequestParam(name = "page",required = true,defaultValue = "1") Integer page,
                                            @RequestParam(name = "pagesize",required = true,defaultValue = "20") Integer pageSize) {
        PageData<Route> pageModel = null;
        try {
            if(siteId>-1) {
                Site site = siteService.getSite(siteId);
                pageModel = routeService.getPage(site, description, page, pageSize);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return pageModel;
    }

    @RequestMapping(value = "/isExistsRouteBySiteAndRule",method = RequestMethod.POST)
    @ResponseBody
    public Boolean isExistsRouteBySiteAndRule(@RequestParam(value = "siteId",defaultValue = "0") Long siteId,
                                                 @RequestParam(value = "rule") String rule){
        try{
            if(!StringUtils.isEmpty(rule)){
                Site site=siteService.getSite(siteId);
                return !routeService.isPatterBySiteAndRule(site,rule);
            }
            return true;
        }catch (Exception ex){
            log.error(ex.getMessage());
           return true;
        }
    }

    @RequestMapping(value = "/isExistsRouteBySiteAndRuleIgnore",method = RequestMethod.POST)
    @ResponseBody
    public Boolean isExistsRouteBySiteAndRuleIgnore(@RequestParam(value = "siteId",defaultValue = "0") Long siteId,
                                              @RequestParam(value = "rule") String rule,
                                              @RequestParam(value = "noRule") String noRule){
        try{
            if(!StringUtils.isEmpty(rule)){
                Site site=siteService.getSite(siteId);
                return !routeService.isPatterBySiteAndRuleIgnore(site, rule, noRule);
            }
            return true;
        }catch (Exception ex){
            log.error(ex.getMessage());
            return true;
        }
    }

    @RequestMapping(value = "/saveRoute")
    @ResponseBody
    public ResultView saveRoute(@RequestParam(value = "siteId",defaultValue = "0") Long siteId,
                                @RequestParam(value = "routeName", required = true) String description,
                                @RequestParam(value = "routeRule",required = true) String routeRule,
                                @RequestParam(value = "template",required = true) String template,
                                @RequestParam(value = "routeType") Integer routeType){
        ResultView result=null;
        try{
            Site site=siteService.getSite(siteId);
            if(site!=null) {
                if(!routeService.isPatterBySiteAndRule(site, routeRule)) {
                    Route route = new Route();
                    route.setSite(site);
                    route.setDescription(description);
                    if (routeType >= 0) {
                        route.setRouteType(EnumUtils.valueOf(RouteType.class, routeType));
                    }
                    route.setTemplate(template);
                    route.setRule(routeRule);
                    route.setCreateTime(LocalDateTime.now());
                    route.setDeleted(false);
                    route.setOrderWeight(50);
                    route.setUpdateTime(LocalDateTime.now());
                    if(routeService.save(route)){
                        result = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
                    }else{
                        result = new ResultView(ResultOptionEnum.FAILE.getCode(), ResultOptionEnum.FAILE.getValue(), null);
                    }
                }else{
                    result = new ResultView(ResultOptionEnum.ROUTE_EXISTS.getCode(), ResultOptionEnum.ROUTE_EXISTS.getValue(), null);
                }
            }else{
                result=new ResultView(ResultOptionEnum.SITE_NOFIND.getCode(),ResultOptionEnum.SITE_NOFIND.getValue(),null);
            }
        }catch (Exception ex){
            log.error(ex.getMessage());
            result=new ResultView(ResultOptionEnum.SERVERFAILE.getCode(),ResultOptionEnum.SERVERFAILE.getValue(),null);
        }
        return result;
    }

    @RequestMapping(value = "/modifyRoute")
    @ResponseBody
    public ResultView modifyRoute(@RequestParam(value = "id",defaultValue = "0") Long id,
                                     @RequestParam(value = "routeName", required = true) String description,
                                     @RequestParam(value = "routeRule",required = true) String routeRule,
                                     @RequestParam(value = "template",required = true) String template,
                                     @RequestParam(value = "routeType") Integer routeType){
        ResultView result=null;
        try{
            Route route=routeService.getRoute(id);
            if(route!=null){
                route.setDescription(description);
                if (routeType >= 0) {
                    route.setRouteType(EnumUtils.valueOf(RouteType.class, routeType));
                }
                route.setTemplate(template);
                route.setRule(routeRule);
                if(routeService.save(route)){
                    result = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
                }else{
                    result = new ResultView(ResultOptionEnum.FAILE.getCode(), ResultOptionEnum.FAILE.getValue(), null);
                }
            }else{
                result=new ResultView(ResultOptionEnum.NOFIND.getCode(),ResultOptionEnum.NOFIND.getValue(),null);
            }
        }catch (Exception ex){
            log.error(ex.getMessage());
            result=new ResultView(ResultOptionEnum.SERVERFAILE.getCode(),ResultOptionEnum.SERVERFAILE.getValue(),null);
        }
        return result;
    }

    @RequestMapping(value = "deleteRoute")
    @ResponseBody
    public ResultView deleteRoute(@RequestParam(name = "id",required = true,defaultValue = "0") Long id,
                                     HttpServletRequest request) {
        ResultView result=null;
        try{
            if(cookieUser.isSupper(request)) {
                Route route = routeService.getRoute(id);
                if(route!=null) {
                    Category category = categoryService.getCategoryByRoute(route);
                    if(category!=null) {
                        result = new ResultView(ResultOptionEnum.EXISTS_RELATION.getCode(), ResultOptionEnum.EXISTS_RELATION.getValue(), null);
                    }else {
                        routeService.delete(route);
                        result = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
                    }
                }else{
                    result=new ResultView(ResultOptionEnum.NOFIND.getCode(),ResultOptionEnum.NOFIND.getValue(),null);
                }
            }
            else {
                result=new ResultView(ResultOptionEnum.NO_LIMITS.getCode(),ResultOptionEnum.NO_LIMITS.getValue(),null);
            }
        }
        catch (Exception ex)
        {
            log.error(ex.getMessage());
            result=new ResultView(ResultOptionEnum.SERVERFAILE.getCode(),ResultOptionEnum.SERVERFAILE.getValue(),null);
        }
        return  result;
    }
}
