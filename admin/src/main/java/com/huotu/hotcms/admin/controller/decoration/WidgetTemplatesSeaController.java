package com.huotu.hotcms.admin.controller.decoration;

import com.sun.javafx.sg.prism.NGShape;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by fawzi on 2016/5/9.
 */

/**
 * 提供模板库功能
 * @since v2.0
 */
@Controller
@RequestMapping("/widgetTemplateSea")
public class WidgetTemplatesSeaController {

    /**
     * 列表
     * @return
     */
    @RequestMapping("/list")
    public ModelAndView widgetTemplateList(){
        ModelAndView mv=new ModelAndView();
        mv.setViewName("decoration/pages/templates.html");
        return mv;
    }

    /**
     * 预览
     * @return
     */
    @RequestMapping(value = "/view")
    public String view(){
        return null;
    }

    /**
     * 使用
     * @return
     */
    @RequestMapping("/user")
    public String toUse(){
        return  null;
    }

    /**
     * 喜欢
     * @return
     */
    @RequestMapping("/like")
    public String like(){
        return null;
    }
}
