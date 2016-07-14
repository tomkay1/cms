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
import com.huotu.cms.manage.util.ImageHelper;
import com.huotu.hotcms.service.entity.Template;
import com.huotu.hotcms.service.entity.login.Login;
import me.jiangcai.lib.resource.Resource;
import me.jiangcai.lib.resource.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

/**
 * @author CJ
 */
@Controller
@RequestMapping("/manage/template")
@PreAuthorize("hasRole('ROOT')")
public class TemplateController extends CRUDController<Template, Long, String, String> {

    @Autowired
    private ResourceService resourceService;

    @Override
    protected String indexViewName() {
        return "/view/template/index.html";
    }

    @Override
    protected Template preparePersist(Login login, Template data, String extra, RedirectAttributes attributes)
            throws RedirectException {
        updateLogo(data, extra);
        return data;
    }

    private void updateLogo(Template data, String extra) throws RedirectException {
        if (!StringUtils.isEmpty(extra)) {
            Resource tmp = resourceService.getResource(extra);
            if (tmp.exists()) {
                try {
                    String newPath = ImageHelper.storeAsImage("png", resourceService, tmp.getInputStream());
                    resourceService.deleteResource(extra);
                    if (data.getLogoUri() != null) {
                        resourceService.deleteResource(data.getLogoUri());
                    }
                    data.setLogoUri(newPath);
                } catch (IOException e) {
                    throw new RedirectException("/manage/template", e.getMessage());
                }
            }
        }
    }

    @Override
    protected void prepareSave(Login login, Template entity, Template data, String extra, RedirectAttributes attributes)
            throws RedirectException {
        updateLogo(entity, extra);
    }

    @Override
    protected String openViewName() {
        return "/view/template/template.html";
    }
}
