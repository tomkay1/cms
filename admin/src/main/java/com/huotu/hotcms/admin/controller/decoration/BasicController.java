package com.huotu.hotcms.admin.controller.decoration;

import com.huotu.hotcms.service.model.widget.WidgetPage;
import com.huotu.hotcms.service.util.ResultOptionEnum;
import com.huotu.hotcms.service.util.ResultView;
import com.huotu.hotcms.service.widget.service.PageResolveService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
@RequestMapping("/basic")
public class BasicController {
    private static final Log log = LogFactory.getLog(BasicController.class);

    @Autowired
    private PageResolveService pageResolveService;

    @RequestMapping("/{name}")
    public ModelAndView widgetTypeList(HttpServletRequest request, @RequestParam("customerid") Integer customerid,@RequestParam("siteId") String siteId, @PathVariable("name") String name) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        String[] goods = {"dd","ss","zz"};
        modelAndView.addObject("goods",goods);
        modelAndView.setViewName(String.format("%s_%s.shtml",siteId,name));
        return  modelAndView;
    }

    @RequestMapping("/edit")
    public ModelAndView editMain(HttpServletRequest request, @RequestParam("customerid") Integer customerid,@RequestParam("siteId") String siteId,@RequestParam("url") String url) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.addObject("url",url+"?customerid="+customerid+"&siteId="+siteId);
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

}
