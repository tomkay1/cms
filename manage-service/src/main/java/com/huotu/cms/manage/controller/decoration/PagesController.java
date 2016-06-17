/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller.decoration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huotu.cms.manage.util.web.CookieUser;
import com.huotu.hotcms.service.common.SiteType;
import com.huotu.hotcms.service.entity.CustomPages;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.widget.WidgetPage;
import com.huotu.hotcms.service.repository.SiteRepository;
import com.huotu.hotcms.service.service.CustomPagesService;
import com.huotu.hotcms.service.service.HostService;
import com.huotu.hotcms.service.service.SiteService;
import com.huotu.hotcms.service.util.PageData;
import com.huotu.hotcms.service.util.ResultOptionEnum;
import com.huotu.hotcms.service.util.ResultView;
import com.huotu.hotcms.service.widget.service.PageResolveService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 页面管理控制器
 * </p>
 *
 * @version 1.2
 * @since xhl
 */
@Controller
@RequestMapping("/manage/page")
public class PagesController {
    private static final Log log = LogFactory.getLog(PagesController.class);

    @Autowired
    private PageResolveService pageResolveService;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private CookieUser cookieUser;

    @Autowired
    private SiteService siteService;

    @Autowired
    private HostService hostService;

    @Autowired
    private CustomPagesService customPagesService;

    @RequestMapping("/list")
    public ModelAndView pageList(@RequestParam("ownerId") long ownerId, String scope) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        try {
            List<Site> siteList;
            if(scope.equals("shop")){
                siteList = siteRepository.findByOwner_IdAndDeletedAndPersonaliseAndSiteTypeOrderBySiteIdDesc(ownerId
                        , false, true, SiteType.SITE_PC_SHOP);
            }else{
                siteList = siteRepository.findByOwner_IdAndDeletedAndPersonaliseAndSiteTypeOrderBySiteIdDesc(ownerId
                        , false, true, SiteType.SITE_PC_WEBSITE);
            }
            modelAndView.addObject("siteList", siteList);
            modelAndView.setViewName("/decoration/pages/list.html");
        } catch (Exception ex) {
            log.error(ex);
        }
        return modelAndView;
    }


    @RequestMapping("/getHomePage")
    @ResponseBody
    public ResultView getHomePage(Long siteId){
        ResultView resultView=null;
        try{
            Site site=siteRepository.findOne(siteId);
            CustomPages customPages=customPagesService.findHomePages(site);
            if(customPages!=null){
                customPages.setSite(null);
                resultView = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), customPages);
            }else{
                resultView = new ResultView(ResultOptionEnum.FAILE.getCode(), ResultOptionEnum.FAILE.getValue(), null);
            }
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        return resultView;
    }

    @RequestMapping(value = "/getPagesList")
    @ResponseBody
    public PageData<CustomPages> getPagesList(@RequestParam(name = "siteId", required = false) Long siteId,
                                              @RequestParam(name = "name", required = false) String name,
                                              @RequestParam(name = "delete") boolean delete,
                                              @RequestParam(name = "publish") boolean publish,
                                              @RequestParam(name = "page", defaultValue = "1") int page,
                                              @RequestParam(name = "pagesize", defaultValue = "20") int pageSize) {
        PageData<CustomPages> pageModel = null;
        try {
            pageModel = customPagesService.getPage(name, siteId, delete, publish, page, pageSize);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return pageModel;
    }

    @RequestMapping(value = "/defaults")
    public ModelAndView defaults(@RequestParam("ownerId") long ownerId, String scope) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            List<Site> siteList = null;
            if(scope.equals("shop")){
                siteList = siteRepository.findByOwner_IdAndDeletedAndPersonaliseAndSiteTypeOrderBySiteIdDesc(ownerId
                        , false, true, SiteType.SITE_PC_SHOP);
            }else{
                siteList = siteRepository.findByOwner_IdAndDeletedAndPersonaliseAndSiteTypeOrderBySiteIdDesc(ownerId
                        , false, true, SiteType.SITE_PC_WEBSITE);
            }
            modelAndView.addObject("siteList", siteList);
            modelAndView.setViewName("/decoration/pages/defaults.html");
        } catch (Exception ex) {
            log.error(ex);
        }
        return modelAndView;
    }

    @RequestMapping(value = "/createPage", method = RequestMethod.POST)
    @ResponseBody
    public ResultView createPage(@RequestParam("widgetStr") String widgetPage,
                                 @RequestParam("siteId") Long siteId,
                                 @RequestParam("publish") boolean publish,
                                 @RequestParam(value = "config", required = false) String config) {
        ResultView resultView = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            WidgetPage widget = mapper.readValue(widgetPage, WidgetPage.class);
            boolean Flag = false;
            if (StringUtils.isEmpty(config)) {
                Flag = pageResolveService.createPageAndConfigByWidgetPage(widget, siteId, publish);
            } else {
                Flag = pageResolveService.createDefaultPageConfigByWidgetPage(widget, siteId, config);
            }
            if (Flag) {
                resultView = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
            } else {
                resultView = new ResultView(ResultOptionEnum.FAILE.getCode(), ResultOptionEnum.FAILE.getValue(), null);
            }
        } catch (Exception ex) {
            log.error(ex);
            resultView = new ResultView(ResultOptionEnum.SERVERFAILE.getCode(), ResultOptionEnum.SERVERFAILE.getValue(), null);
        }
        return resultView;
    }

    @RequestMapping(value = "/patch")
    @ResponseBody
    public ResultView updatePage(@RequestParam("widgetStr") String widgetPage,
                                 @RequestParam("ownerId") long ownerId,
                                 @RequestParam("publish") boolean publish,
                                 @RequestParam("id") Long id) {
        ResultView resultView = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            WidgetPage widget = mapper.readValue(widgetPage, WidgetPage.class);
            boolean Flag = pageResolveService.patchPageAndConfigByWidgetPage(widget, id, publish);
            if (Flag) {
                resultView = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
            } else {
                resultView = new ResultView(ResultOptionEnum.FAILE.getCode(), ResultOptionEnum.FAILE.getValue(), null);
            }
        } catch (Exception ex) {
            log.error(ex);
            resultView = new ResultView(ResultOptionEnum.SERVERFAILE.getCode(), ResultOptionEnum.SERVERFAILE.getValue(), null);
        }
        return resultView;
    }

    @RequestMapping(value = "/publish")
    @ResponseBody
    public ResultView publishPage(HttpServletRequest request,
                                 @RequestParam("id") Long id,
                                 @RequestParam("publish") boolean publish) {
        ResultView resultView = null;
        try {
            CustomPages customPages = customPagesService.getCustomPages(id);
            if (customPages != null) {
                if (cookieUser.isOwnerLogin(request, customPages.getSite().getOwner().getId())) {
                    customPages.setPublish(publish);
                    CustomPages customPages1 = customPagesService.save(customPages);
                    if (customPages1 != null) {
                        resultView = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
                    } else {
                        resultView = new ResultView(ResultOptionEnum.FAILE.getCode(), ResultOptionEnum.FAILE.getValue(), null);
                    }
                } else {
                    resultView = new ResultView(ResultOptionEnum.NO_LIMITS.getCode(), ResultOptionEnum.NO_LIMITS.getValue(), null);
                }
            } else {
                resultView = new ResultView(ResultOptionEnum.NOFIND.getCode(), ResultOptionEnum.NOFIND.getValue(), null);
            }
        } catch (Exception ex) {
            log.error(ex);
        }
        return resultView;
    }


    @RequestMapping(value = "/home")
    @ResponseBody
    public ResultView homePage(@RequestParam("id") Long id) {
        ResultView resultView;
        try {
            Boolean flag = customPagesService.setHomePages(id);
            if(flag){
                resultView = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
            }else{
                resultView = new ResultView(ResultOptionEnum.FAILE.getCode(), ResultOptionEnum.FAILE.getValue(), null);
            }
        } catch (Exception ex) {
            log.error(ex);
            resultView = new ResultView(ResultOptionEnum.SERVERFAILE.getCode(), ResultOptionEnum.SERVERFAILE.getValue(), null);
        }
        return resultView;
    }

    @RequestMapping("/root")
    @ResponseBody
    public ResultView getWebRoot(@RequestParam("siteId") Long siteId) {
        try {
            String url = "http://";
            Site site = siteService.getSite(siteId);
            if (site != null) {
                String domain = hostService.getHomeDomain(site);
                if(domain!=null){
                    url = url + domain;
                    return new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), url);
                }else{
                    return new ResultView(ResultOptionEnum.NOFIND.getCode(), ResultOptionEnum.NOFIND.getValue(), url);
                }
            } else {
                return new ResultView(ResultOptionEnum.NOFIND.getCode(), ResultOptionEnum.NOFIND.getValue(), url);
            }
        } catch (Exception ex) {
            log.error(ex);
            return new ResultView(ResultOptionEnum.SERVERFAILE.getCode(), ResultOptionEnum.SERVERFAILE.getValue(),null);
        }
    }

    @RequestMapping(value = "/delete")
    @ResponseBody
    public ResultView deletePage(HttpServletRequest request,
                                 @RequestParam("id") Long id) {
        ResultView resultView = null;
        try {
            CustomPages customPages = customPagesService.getCustomPages(id);
            if (customPages != null) {
                if (cookieUser.isOwnerLogin(request, customPages.getSite().getOwner().getId())) {
                    customPagesService.delete(customPages);
                    resultView = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
                } else {
                    resultView = new ResultView(ResultOptionEnum.NO_LIMITS.getCode(), ResultOptionEnum.NO_LIMITS.getValue(), null);
                }
            } else {
                resultView = new ResultView(ResultOptionEnum.NOFIND.getCode(), ResultOptionEnum.NOFIND.getValue(), null);
            }
        } catch (Exception ex) {
            log.error(ex);
        }
        return resultView;
    }

}
