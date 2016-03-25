package com.huotu.hotcms.admin.controller.decoration;

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
 *     默认页面配置
 * </p>
 * @since xhl
 *
 * @version 1.2
 */
@Controller
@RequestMapping("/defaultsPages")
public class DefaultsPagesController {
    private static final Log log = LogFactory.getLog(DefaultsPagesController.class);

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
    public ModelAndView editMain(HttpServletRequest request,
                                 @RequestParam("customerid") Integer customerid,
                                 @RequestParam("siteId") String siteId,@RequestParam("url") String url) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.addObject("url",url+"?customerid="+customerid+"&siteId="+siteId);
        modelAndView.setViewName("/decoration/edit/editMain.html");
        return  modelAndView;
    }
}
