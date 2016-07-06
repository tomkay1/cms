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
import com.huotu.hotcms.service.entity.login.Login;
import com.huotu.hotcms.service.repository.OwnerRepository;
import com.huotu.hotcms.widget.entity.WidgetInfo;
import com.huotu.hotcms.widget.entity.support.WidgetIdentifier;
import com.huotu.hotcms.widget.repository.WidgetInfoRepository;
import com.huotu.hotcms.widget.service.WidgetFactoryService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author CJ
 */
@Controller
@RequestMapping("/manage/widget")
@PreAuthorize("hasRole('ROOT')")
public class WidgetInfoController
        extends CRUDController<WidgetInfo, WidgetIdentifier, WidgetInfoController.NewWidgetModel, Void> {

    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private WidgetFactoryService widgetFactoryService;
    @Autowired
    private WidgetInfoRepository widgetInfoRepository;

    @Override
    protected String indexViewName() {
        return "/view/widget/index.html";
    }

    @Override
    protected WidgetInfo preparePersist(Login login, WidgetInfo data, NewWidgetModel extra
            , RedirectAttributes attributes) throws RedirectException {

        if (widgetInfoRepository.findOne(data.getIdentifier()) != null)
            throw new RedirectException("/manage/widget", "这个控件已存在。");

        data.setCreateTime(LocalDateTime.now());
        data.setEnabled(true);
        if (extra.getOwnerId() != null) {
            data.setOwner(ownerRepository.getOne(extra.getOwnerId()));
        }
        try {
            if (extra.getJar() != null)
                widgetFactoryService.setupJarFile(data, extra.getJar().getInputStream());
            else
                widgetFactoryService.setupJarFile(data, null);
        } catch (IOException e) {
            throw new RedirectException("/manage/widget", "读写错误:" + e.getLocalizedMessage());
        }

        // 如果上传了jar包
        return data;
    }

    @Override
    protected void prepareSave(Login login, WidgetInfo entity, WidgetInfo data, Void extra
            , RedirectAttributes attributes) throws RedirectException {
        throw new NoSuchMethodError("not support yet");
    }

    @Override
    protected String openViewName() {
        throw new NoSuchMethodError("not support yet");
    }

    @Data
    static class NewWidgetModel {
        private Long ownerId;
        private MultipartFile jar;
    }
}
