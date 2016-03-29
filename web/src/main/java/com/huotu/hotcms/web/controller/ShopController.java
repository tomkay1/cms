package com.huotu.hotcms.web.controller;

import com.huotu.hotcms.service.entity.CustomPages;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.service.CustomPagesService;
import com.huotu.hotcms.service.thymeleaf.service.SiteResolveService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 商城入口Controller
 * </p>
 *
 * @since xhl
 */
@Controller
@RequestMapping(value = "/shop")
public class ShopController {
    private static final Log log = LogFactory.getLog(ShopController.class);

    @Autowired
    private CustomPagesService customPagesService;


    @Autowired
    SiteResolveService siteResolveService;

    /**
     * 商城首页/shop/
     * */
    @RequestMapping("/")
    public ModelAndView index(HttpServletRequest request){
        ModelAndView modelAndView=new ModelAndView();
        try{
            Site site = siteResolveService.getCurrentSite(request);
            CustomPages customPages=customPagesService.findHomePages();
            if(customPages!=null){
                modelAndView.setViewName(String.format("%s_%s.cshtml", site.getSiteId(),customPages.getId()));
            }else{
                //商城搭建中...
            }
        }catch (Exception ex){
            log.error(ex);
        }
        return modelAndView;
    }

    /**
     * 商城其他页面-->/shop/{id}/........
     * **/
    @RequestMapping("/{id}")
    public ModelAndView widgetTypeList(HttpServletRequest request, @PathVariable("id") Long id) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        try {
            Site site = siteResolveService.getCurrentSite(request);
            modelAndView.setViewName(String.format("%s_%s.cshtml", site.getSiteId(),id));
        } catch (Exception ex) {
            log.error(ex);
        }
        return modelAndView;
    }
}
