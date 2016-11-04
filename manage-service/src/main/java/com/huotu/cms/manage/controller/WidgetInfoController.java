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
import com.huotu.hotcms.service.common.PageType;
import com.huotu.hotcms.service.entity.WidgetInfo;
import com.huotu.hotcms.service.entity.login.Login;
import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.service.entity.support.WidgetIdentifier;
import com.huotu.hotcms.service.repository.OwnerRepository;
import com.huotu.hotcms.widget.*;
import com.huotu.hotcms.widget.exception.FormatException;
import com.huotu.hotcms.widget.model.WidgetModel;
import com.huotu.hotcms.widget.model.WidgetStyleModel;
import com.huotu.hotcms.widget.repository.WidgetInfoRepository;
import com.huotu.hotcms.widget.service.WidgetFactoryService;
import me.jiangcai.lib.resource.service.ResourceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.NumberUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author CJ
 */
@Controller
@RequestMapping("/manage/widget")
@PreAuthorize("hasRole('ROOT')")
public class WidgetInfoController
        extends CRUDController<WidgetInfo, WidgetIdentifier, Void, Long> {

    private static final Log log = LogFactory.getLog(WidgetInfoController.class);

    @Autowired
    private Environment environment;
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private WidgetFactoryService widgetFactoryService;
    @Autowired
    private WidgetInfoRepository widgetInfoRepository;
    @Autowired
    private MultipartResolver multipartResolver;
    @Autowired
    private WidgetResolveService widgetResolveService;

    @Autowired(required = false)
    private ResourceService resourceService;
    @Autowired
    private ServletContext servletContext;

    @RequestMapping(value = "/{id}/install", method = RequestMethod.GET)
    @Transactional
    public String install(@PathVariable("id") WidgetIdentifier id, RedirectAttributes attributes) {
        WidgetInfo widgetInfo = widgetInfoRepository.getOne(id);
        try {
            widgetFactoryService.installWidgetInfo(widgetInfo);
            GritterUtils.AddSuccess("完成", attributes);
        } catch (IOException | FormatException e) {
            log.error("install widget", e);
            GritterUtils.AddFlashDanger(e.getLocalizedMessage(), attributes);
        }

        return redirectIndexViewName();
    }

    @RequestMapping(value = "/{id}/primary", method = RequestMethod.GET)
    @Transactional
    public String primary(@PathVariable("id") WidgetIdentifier id, boolean ignoreError, RedirectAttributes attributes) {
        WidgetInfo widgetInfo = widgetInfoRepository.getOne(id);
        try {
            widgetFactoryService.primary(widgetInfo, ignoreError);
            GritterUtils.AddFlashSuccess("设置完成", attributes);
        } catch (IOException | IllegalStateException e) {
            log.error("primary widget", e);
            GritterUtils.AddFlashDanger(e.getLocalizedMessage(), attributes);
        }

        return redirectIndexViewName();
    }

    @Override
    protected String indexViewName() {
        return "/view/widget/index.html";
    }

    @Override
    protected WidgetInfo preparePersist(HttpServletRequest request, Login login, WidgetInfo data, Void extra
            , RedirectAttributes attributes) throws RedirectException {

        if (!environment.acceptsProfiles("test") && !environment.acceptsProfiles("development")
                && data.getVersion().endsWith("SNAPSHOT")) {
            throw new RedirectException("/manage/widget", "无法在本系统挂载测试控件。");
        }

        if (widgetInfoRepository.findOne(data.getIdentifier()) != null)
            throw new RedirectException("/manage/widget", "这个控件已存在。");

        data.setCreateTime(LocalDateTime.now());
        data.setEnabled(true);

        data.setGroupId(data.getGroupId().trim());
        data.setArtifactId(data.getArtifactId().trim());
        data.setVersion(data.getVersion().trim());
        if (data.getType() != null) {
            data.setType(data.getType().trim());
        }

        final String ownerIdParameter = request.getParameter("ownerId");
        if (ownerIdParameter != null && ownerIdParameter.length() > 0) {
            Long ownerId = NumberUtils.parseNumber(ownerIdParameter, Long.class);
            data.setOwner(ownerRepository.getOne(ownerId));
        }
        try {
            MultipartFile jar;
            if (multipartResolver.isMultipart(request)) {
                MultipartHttpServletRequest multipartHttpServletRequest;
                if (request instanceof MultipartHttpServletRequest)
                    multipartHttpServletRequest = (MultipartHttpServletRequest) request;
                else
                    multipartHttpServletRequest = multipartResolver.resolveMultipart(request);
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
    protected void prepareUpdate(Login login, WidgetInfo entity, WidgetInfo data, Long extra
            , RedirectAttributes attributes, HttpServletRequest request) throws RedirectException {
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

    /**
     * 获取控件资源时参照 {@link WidgetFactoryService#installWidgetInfo(WidgetInfo)}
     *
     * @param locale 当前语言
     * @param login  当前身份
     * @return
     * @throws IOException
     * @throws URISyntaxException
     * @throws FormatException
     */
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROOT','" + Login.Role_Manage_Value + "')")
    @RequestMapping(value = "/widgets", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public List<WidgetModel> getWidgetInfo(Locale locale, @AuthenticationPrincipal Login login, PageType pageType) throws IOException
            , URISyntaxException, FormatException {
        Owner owner = CMSContext.RequestContext().getSite().getOwner();
        if (owner == null && !login.isRoot()) {
            throw new AccessDeniedException("");
        }
        List<InstalledWidget> installedWidgets = widgetFactoryService.widgetList(owner);
        List<WidgetModel> widgetModels = new ArrayList<>();
        List<InstalledWidget> widgets;
        if (pageType.equals(PageType.DataContent)) {
            widgets = installedWidgets.stream()
                    .filter(installedWidget -> PageType.DataContent.equals(installedWidget.getWidget().supportedPageType())
                            || installedWidget.getWidget().supportedPageType() == null)
                    .collect(Collectors.toList());
        } else if (pageType.equals(PageType.DataIndex)) {
            widgets = installedWidgets.stream()
                    .filter(installedWidget -> PageType.DataIndex.equals(installedWidget.getWidget().supportedPageType())
                            || installedWidget.getWidget().supportedPageType() == null)
                    .collect(Collectors.toList());
        } else if (pageType.equals(PageType.Ordinary)) {
            widgets = installedWidgets.stream()
                    .filter(installedWidget -> PageType.Ordinary.equals(installedWidget.getWidget().supportedPageType())
                            || installedWidget.getWidget().supportedPageType() == null)
                    .collect(Collectors.toList());
        } else {
            widgets = installedWidgets;
        }

        widgets.stream()
                .collect(Collectors.groupingBy(t -> new WidgetIdentifier(t.getWidget().groupId(), t.getWidget().widgetId(), ""),
                        Collectors.toList()))
                .forEach(((identifier, installedWidgets1) -> {
                    if (installedWidgets1.isEmpty())
                        return;
                    if (identifier == null) {
                        for (InstalledWidget installedWidget : installedWidgets1) {
//                            if (installedWidget.getWidget().disabled()) {
                                widgetModels.add(getWidgetModel(locale, installedWidget, false));
//                            }
                        }
                        return;
                    }
                    // 没有持久化的 不管!
                    if (installedWidgets1.size() > 1) {
                        // 找到他们最大的 其他的 都排除掉
                        InstalledWidget best = installedWidgets1.stream()
                                .sorted((o1, o2) -> new DefaultArtifactVersion(o2.getWidget().version())
                                        .compareTo(new DefaultArtifactVersion(o1.getWidget().version())))
                                .findFirst().orElse(null);
//                        if (best.getWidget().disabled()) {
                            widgetModels.add(getWidgetModel(locale, best, true));
//                        }
                        //设置最新的控件
                        //将源列表控件最新的删除  将不是最新的也添加进去
                        installedWidgets1.remove(best);
                        for (InstalledWidget installedWidget : installedWidgets1) {
//                            if (installedWidget.getWidget().disabled()) {
                                widgetModels.add(getWidgetModel(locale, installedWidget, false));
//                            }
                        }
                    } else {
                        //如果控件分组就一个，那这个控件就是最新的
                        for (InstalledWidget installedWidget : installedWidgets1) {
//                            if (installedWidget.getWidget().disabled()) {
                            widgetModels.add(getWidgetModel(locale, installedWidget, true));
//                            }
                        }
                    }
                }));

        return widgetModels;
    }

    /**
     * 获取一个控件model
     *
     * @param locale
     * @param installedWidget
     * @param flag            true最新的，否则不是最新的
     * @return
     */
    @NotNull
    private WidgetModel getWidgetModel(Locale locale, InstalledWidget installedWidget, Boolean flag) {
        try {
            WidgetModel widgetModel = new WidgetModel();
            Widget widget = installedWidget.getWidget();
            widgetModel.setLocallyName(widget.name(locale));
            widgetModel.setType(installedWidget.getType());
            WidgetStyle[] widgetStyles = widget.styles();
//            widgetModel.setEditorHTML(widgetResolveService.editorHTML(widget, CMSContext.RequestContext(), null));
            widgetModel.setIdentity(Widget.WidgetIdentity(widget));
            widgetModel.setScriptHref(servletContext.getContextPath() + Widget.widgetJsResourceURI(widget));
            widgetModel.setThumbnail(resourceService.getResource(Widget.thumbnailPath(widget)).httpUrl().toString());
            WidgetStyleModel[] widgetStyleModels = new WidgetStyleModel[widgetStyles.length];

            for (int i = 0; i < widgetStyles.length; i++) {
                WidgetStyleModel widgetStyleModel = new WidgetStyleModel();
                final WidgetStyle widgetStyle = widgetStyles[i];
                widgetStyleModel.setId(widgetStyle.id());
                widgetStyleModel.setThumbnail(resourceService.getResource(WidgetStyle.thumbnailPath(widget
                        , widgetStyle)).httpUrl().toString());
                widgetStyleModel.setLocallyName(widgetStyle.name(locale));
                try {
                    widgetModel.setDefaultProperties(widget.defaultProperties(resourceService));
                    widgetStyleModel.setPreviewHTML(widgetResolveService.previewHTML(widget, widgetStyle.id()
                            , CMSContext.RequestContext(), widget.defaultProperties(resourceService)));
                } catch (IllegalStateException e) {
                    widgetStyleModel.setPreviewFailed(e.getMessage());
                }
                widgetStyleModels[i] = widgetStyleModel;
            }
            widgetModel.setStyles(widgetStyleModels);
            widgetModel.setFlag(flag);
            return widgetModel;
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }

}
