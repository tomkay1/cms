/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller;

import com.huotu.cms.manage.bracket.GritterUtils;
import com.huotu.hotcms.service.entity.login.Login;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 超级管理员相关路由
 * </p>
 */
@Controller
@RequestMapping("/manage/supper")
public class SupperController {
    private static final Log log = LogFactory.getLog(SupperController.class);

    /**
     * 超级管理员首页
     *
     * @param login
     * @return
     * @throws Exception
     */
    @RequestMapping({"/", ""})
    @PreAuthorize("hasRole('ROOT')")
    public ModelAndView admin(@AuthenticationPrincipal Login login, Model model) throws Exception {
        login.updateOwnerId(null);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/view/supper/index.html");
        GritterUtils.AddInfo("欢迎回来", model);
        return modelAndView;
    }

    /**
     * 以某商户身份运行。
     *
     * @return 回到商户管理主页
     */
    @RequestMapping(value = "/as/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('ROOT','" + Login.Role_AS_Value + "')")
    public String as(@AuthenticationPrincipal Login login, @PathVariable(value = "id") Long id) {
        login.updateOwnerId(id);
        return "redirect:/manage/main";
    }

}
