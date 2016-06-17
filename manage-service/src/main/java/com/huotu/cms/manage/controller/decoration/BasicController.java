/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller.decoration;

import com.huotu.hotcms.service.model.widget.WidgetPage;
import com.huotu.hotcms.service.widget.service.PageResolveService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *     基础页装修控制器
 * </p>
 * @since xhl
 *
 * @version 1.2
 */
@Controller
@RequestMapping("/manage/basic")
public class BasicController {
    private static final Log log = LogFactory.getLog(BasicController.class);

    @Autowired
    private PageResolveService pageResolveService;

    @RequestMapping("/{name}")
    public ModelAndView widgetTypeList(@RequestParam("siteId") String siteId,
                                       @PathVariable("name") String name) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        String[] goods = {"dd","ss","zz"};
        modelAndView.addObject("goods",goods);
        modelAndView.setViewName(String.format("%s_%s.shtml",siteId,name));
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



    @RequestMapping("/test")
    public ModelAndView test(HttpServletRequest request) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/decoration/test.html");

        return  modelAndView;
    }

    @RequestMapping("/test2")
    public ModelAndView jsTest(HttpServletRequest request) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/decoration/widgetPageTest.html");

        WidgetPage widgetPage=new WidgetPage();
        widgetPage.setPageBackAlign("center");
        widgetPage.setPageBackGround("img_test");
        widgetPage.setPageBackImage("imt_test2");
        widgetPage.setPageDescription("描述信息测试");
        widgetPage.setPageKeyWords("关键字测试");
        widgetPage.setPageName("名称测试");
        widgetPage.setUrl("http://www.baidu.com");
        Long siteId=new Long(1);
//        pageResolveService.createPageConfigByWidgetPage(widgetPage,)

        return  modelAndView;
    }

    @RequestMapping("/test3")
    public ModelAndView test3(){
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.addObject("pageBackGround","red");
        modelAndView.addObject("pageBackImage","http://img12.360buyimg.com/cms/jfs/t661/233/1242620315/188143/ed2e741b/54b86a17N9e9230df.jpg");
        modelAndView.addObject("pageBackRepeat","no-repeat");
        modelAndView.addObject("pageHorizontalDistance","10");
        modelAndView.addObject("pageHorizontalUnit","px");
        modelAndView.addObject("pageVerticalDistance","20");
        modelAndView.addObject("pageVerticalUnit","px");

        modelAndView.setViewName("/view/test2.html");
        return modelAndView;
    }

//    @Autowired
////    private XmlServiceTest
//
//    public ModelAndView test4(){
////        XmlServiceTest
//    }
}
