package com.huotu.hotcms.web.controller;

import com.huotu.hotcms.service.common.PageErrorType;
import com.huotu.hotcms.service.entity.CustomPages;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.thymeleaf.model.RequestModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * PC官网、商城第三方授权绑定操作
 * </p>
 *
 * @since xhl
 */
@Controller
@RequestMapping(value = "/bind")
public class BindController {
    private static final Log log = LogFactory.getLog(ShopController.class);

    @RequestMapping("/callback")
    public ModelAndView callback(HttpServletRequest request){
        ModelAndView modelAndView=new ModelAndView();
        try{
            String code=request.getParameter("code");
        }catch (Exception ex){
            log.error(ex);
        }
        return modelAndView;
    }
}
