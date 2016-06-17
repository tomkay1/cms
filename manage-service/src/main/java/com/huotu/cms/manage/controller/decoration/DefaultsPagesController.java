/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller.decoration;

import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.service.SiteService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 *     默认页面配置
 * </p>
 * @since xhl
 *
 * @version 1.2
 */
@Controller
@RequestMapping("/manage/defaultsPages")
public class DefaultsPagesController {
    private static final Log log = LogFactory.getLog(DefaultsPagesController.class);

    @Autowired
    private SiteService siteService;

    @RequestMapping("/{name}")
    public ModelAndView defaultsPages(@RequestParam("siteId") Long siteId,
                                       @PathVariable("name") String name){
        ModelAndView modelAndView=new ModelAndView();
        try{
            Site site=siteService.getSite(siteId);
            if(site!=null) {
                modelAndView.addObject("site", site);
            }
            modelAndView.setViewName(String.format("%s_%s.shtml",siteId,name));
        }catch (Exception ex){
            log.error(ex);
        }
        return  modelAndView;
    }

    @RequestMapping("/edit")
    public ModelAndView editMain(@RequestParam("ownerId") long ownerId,
                                 @RequestParam("siteId") String siteId, @RequestParam("url") String url) throws Exception {
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.addObject("url", url + "?ownerId=" + ownerId + "&siteId=" + siteId);
        modelAndView.setViewName("/decoration/edit/editMain.html");
        return  modelAndView;
    }
}
