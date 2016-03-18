package com.huotu.hotcms.admin.controller.decoration;

import com.huotu.hotcms.service.entity.CustomPages;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.service.CustomPagesService;
import com.huotu.hotcms.service.util.PageData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *     页面管理控制器
 * </p>
 * @since xhl
 *
 * @version 1.2
 */
@Controller
@RequestMapping("/page")
public class PagesController {
    private static final Log log = LogFactory.getLog(PagesController.class);

    @Autowired
    private CustomPagesService customPagesService;

    @RequestMapping("/list")
    public ModelAndView widgetTypeList(HttpServletRequest request, @RequestParam("customerid") Integer customerid) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/decoration/pages/list.html");
        return  modelAndView;
    }

    @RequestMapping(value = "/getPagesList")
    @ResponseBody
    public PageData<CustomPages> getPagesList(@RequestParam(name="siteId",required = false) Long siteId,
                                       @RequestParam(name="name",required = false) String name,
                                       @RequestParam(name="delete",required = true) boolean delete,
                                       @RequestParam(name = "page",required = true,defaultValue = "1") int page,
                                       @RequestParam(name = "pagesize",required = true,defaultValue = "20") int pageSize) {
        PageData<CustomPages> pageModel = null;
        try {
            pageModel = customPagesService.getPage(name,siteId,delete,page,pageSize);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return pageModel;
    }
}
