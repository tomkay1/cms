/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller;

import com.huotu.cms.manage.controller.support.CRUDController;
import com.huotu.cms.manage.exception.RedirectException;
import com.huotu.hotcms.service.entity.Template;
import com.huotu.hotcms.service.entity.login.Login;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

/**
 * @author CJ
 */
@Controller
@RequestMapping("/manage/template")
@PreAuthorize("hasRole('" + Login.Role_Template_Value + "')")
public class TemplateController extends CRUDController<Template, Long, String, String> {

    private static Log log = LogFactory.getLog(TemplateController.class);

    @Override
    protected String indexViewName() {
        return "/view/template/index.html";
    }

    @Override
    protected Template preparePersist(Login login, Template data, String extra, RedirectAttributes attributes)
            throws RedirectException {
        data.setEnabled(true);// 第一次添加的模板 总是有效的吧
        try {
            commonService.updateImageFromTmp(data, 0, extra);
        } catch (IOException e) {
            throw new RedirectException(rootUri(), e);
        }
        return data;
    }


    @Override
    protected void prepareUpdate(Login login, Template entity, Template data, String extra, RedirectAttributes attributes)
            throws RedirectException {
        try {
            commonService.updateImageFromTmp(data, 0, extra);
        } catch (IOException e) {
            throw new RedirectException(rootUri() + "/" + entity.getSiteId(), e);
        }
    }

    @Override
    protected String openViewName() {
        return "/view/template/template.html";
    }
}
