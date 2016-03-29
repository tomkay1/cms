package com.huotu.hotcms.admin.controller.decoration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huotu.hotcms.admin.util.web.CookieUser;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.CustomPages;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.Result;
import com.huotu.hotcms.service.model.widget.WidgetPage;
import com.huotu.hotcms.service.repository.SiteRepository;
import com.huotu.hotcms.service.service.CustomPagesService;
import com.huotu.hotcms.service.util.PageData;
import com.huotu.hotcms.service.util.ResultOptionEnum;
import com.huotu.hotcms.service.util.ResultView;
import com.huotu.hotcms.service.util.StringUtil;
import com.huotu.hotcms.service.widget.service.PageResolveService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
@RequestMapping("/page")
public class PagesController {
    private static final Log log = LogFactory.getLog(PagesController.class);

    @Autowired
    private PageResolveService pageResolveService;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private CustomPagesService customPagesService;

    @Autowired
    private CookieUser cookieUser;

    @RequestMapping("/list")
    public ModelAndView widgetTypeList(HttpServletRequest request, @RequestParam("customerid") Integer customerid) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        try {
            List<Site> siteList = siteRepository.findByCustomerIdAndDeletedAndPersonaliseOrderBySiteIdDesc(customerid, false, true);
            modelAndView.addObject("siteList", siteList);
            modelAndView.setViewName("/decoration/pages/list.html");
        } catch (Exception ex) {
            log.error(ex);
        }
        return modelAndView;
    }

    @RequestMapping(value = "/getPagesList")
    @ResponseBody
    public PageData<CustomPages> getPagesList(@RequestParam(name = "siteId", required = false) Long siteId,
                                              @RequestParam(name = "name", required = false) String name,
                                              @RequestParam(name = "delete", required = true) boolean delete,
                                              @RequestParam(name = "publish", required = true) boolean publish,
                                              @RequestParam(name = "page", required = true, defaultValue = "1") int page,
                                              @RequestParam(name = "pagesize", required = true, defaultValue = "20") int pageSize) {
        PageData<CustomPages> pageModel = null;
        try {
            pageModel = customPagesService.getPage(name, siteId, delete, publish, page, pageSize);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return pageModel;
    }

    @RequestMapping(value = "/defaults")
    public ModelAndView defaults(HttpServletRequest request, @RequestParam("customerid") Integer customerid) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            List<Site> siteList = siteRepository.findByCustomerIdAndDeletedAndPersonaliseOrderBySiteIdDesc(customerid, false, true);
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
                                 @RequestParam("customerId") Integer customerId,
                                 @RequestParam("siteId") Long siteId,
                                 @RequestParam("publish") boolean publish,
                                 @RequestParam(value = "config", required = false) String config) {
        ResultView resultView = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            WidgetPage widget = mapper.readValue(widgetPage, WidgetPage.class);
            boolean Flag = false;
            if (StringUtils.isEmpty(config)) {
                Flag = pageResolveService.createPageAndConfigByWidgetPage(widget, customerId, siteId, publish);
            } else {
                Flag = pageResolveService.createDefaultPageConfigByWidgetPage(widget, customerId, siteId, config);
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
                                 @RequestParam("customerId") Integer customerId,
                                 @RequestParam("publish") boolean publish,
                                 @RequestParam("id") Long id) {
        ResultView resultView = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            WidgetPage widget = mapper.readValue(widgetPage, WidgetPage.class);
            boolean Flag = pageResolveService.patchPageAndConfigByWidgetPage(widget, customerId, id, publish);
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

    @RequestMapping(value = "/delete")
    @ResponseBody
    public ResultView deletePage(HttpServletRequest request,
                                 @RequestParam("id") Long id,
                                 @RequestParam("publish") boolean publish) {
        ResultView resultView = null;
        try {
            CustomPages customPages = customPagesService.getCustomPages(id);
            if (customPages != null) {
                if (cookieUser.isCustomer(request, customPages.getCustomerId())) {
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
        ResultView resultView = null;
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
}
