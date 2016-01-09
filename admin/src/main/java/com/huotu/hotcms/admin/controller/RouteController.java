package com.huotu.hotcms.admin.controller;

import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.service.SiteService;
import com.huotu.hotcms.service.service.impl.RouteServiceImpl;
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

/**
 * Created by Administrator xhl 2016/1/9.
 */
@Controller
@RequestMapping("/route")
public class RouteController {
    private static final Log log = LogFactory.getLog(RouteController.class);

    @Autowired
    private SiteService siteService;

    @Autowired
    private RouteServiceImpl routeService;

    @RequestMapping(value = "/isExistsRouteBySiteAndRule",method = RequestMethod.POST)
    @ResponseBody
    public Boolean isExistsRouteBySiteAndRule(@RequestParam(value = "siteId",defaultValue = "0") Long siteId,
                                                 @RequestParam(value = "rule") String rule){
        try{
            if(!StringUtils.isEmpty(rule)){
                Site site=siteService.getSite(siteId);
                return !routeService.isExistsBySiteAndRule(site,rule);
            }
            return true;
        }catch (Exception ex){
            log.error(ex.getMessage());
           return true;
        }
    }
}
