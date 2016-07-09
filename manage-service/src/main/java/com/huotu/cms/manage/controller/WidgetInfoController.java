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
import com.huotu.cms.manage.controller.support.CRUDController;
import com.huotu.cms.manage.exception.RedirectException;
import com.huotu.hotcms.service.entity.WidgetInfo;
import com.huotu.hotcms.service.entity.login.Login;
import com.huotu.hotcms.service.entity.support.WidgetIdentifier;
import com.huotu.hotcms.service.repository.OwnerRepository;
import com.huotu.hotcms.widget.exception.FormatException;
import com.huotu.hotcms.widget.repository.WidgetInfoRepository;
import com.huotu.hotcms.widget.service.WidgetFactoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.NumberUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author CJ
 */
@Controller
@RequestMapping("/manage/widget")
@PreAuthorize("hasRole('ROOT')")
public class WidgetInfoController
        extends CRUDController<WidgetInfo, WidgetIdentifier, HttpServletRequest, Long> {

    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private WidgetFactoryService widgetFactoryService;
    @Autowired
    private WidgetInfoRepository widgetInfoRepository;
    @Autowired
    private MultipartResolver multipartResolver;

    @RequestMapping(value = "/{id}/install", method = RequestMethod.GET)
    @Transactional
    public String install(@PathVariable("id") WidgetIdentifier id, RedirectAttributes attributes) {
        WidgetInfo widgetInfo = widgetInfoRepository.getOne(id);
        try {
            widgetFactoryService.installWidgetInfo(widgetInfo);
            GritterUtils.AddSuccess("完成", attributes);
        } catch (IOException | FormatException e) {
            GritterUtils.AddFlashDanger(e.getLocalizedMessage(), attributes);
        }

        return redirectIndexViewName();
    }

    @Override
    protected String indexViewName() {
        return "/view/widget/index.html";
    }

    @Override
    protected WidgetInfo preparePersist(Login login, WidgetInfo data, HttpServletRequest extra
            , RedirectAttributes attributes) throws RedirectException {

        if (widgetInfoRepository.findOne(data.getIdentifier()) != null)
            throw new RedirectException("/manage/widget", "这个控件已存在。");

        data.setCreateTime(LocalDateTime.now());
        data.setEnabled(true);

        final String ownerIdParameter = extra.getParameter("ownerId");
        if (ownerIdParameter != null && ownerIdParameter.length() > 0) {
            Long ownerId = NumberUtils.parseNumber(ownerIdParameter, Long.class);
            data.setOwner(ownerRepository.getOne(ownerId));
        }
        try {
            MultipartFile jar;
            if (multipartResolver.isMultipart(extra)) {
                MultipartHttpServletRequest multipartHttpServletRequest;
                if (extra instanceof MultipartHttpServletRequest)
                    multipartHttpServletRequest = (MultipartHttpServletRequest) extra;
                else
                    multipartHttpServletRequest = multipartResolver.resolveMultipart(extra);
                jar = multipartHttpServletRequest.getFile("jar");
            } else
                jar = null;
            if (jar != null)
                widgetFactoryService.setupJarFile(data, jar.getInputStream());
            else
                widgetFactoryService.setupJarFile(data, null);
        } catch (IOException e) {
            throw new RedirectException("/manage/widget", "读写错误:" + e.getLocalizedMessage());
        }

        // 如果上传了jar包
        return data;
    }

    @Override
    protected void prepareSave(Login login, WidgetInfo entity, WidgetInfo data, Long extra
            , RedirectAttributes attributes) throws RedirectException {
        if (extra != null)
            entity.setOwner(ownerRepository.getOne(extra));
        else
            entity.setOwner(null);
        entity.setType(data.getType());
        entity.setEnabled(data.isEnabled());
    }

    @Override
    protected String openViewName() {
        return "/view/widget/widget.html";
    }

}
