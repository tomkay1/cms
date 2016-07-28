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
import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.InstalledWidget;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetResolveService;
import com.huotu.hotcms.widget.WidgetStyle;
import com.huotu.hotcms.widget.exception.FormatException;
import com.huotu.hotcms.widget.model.WidgetModel;
import com.huotu.hotcms.widget.model.WidgetStyleModel;
import com.huotu.hotcms.widget.repository.WidgetInfoRepository;
import com.huotu.hotcms.widget.service.WidgetFactoryService;
import com.sun.jndi.toolkit.url.Uri;
import me.jiangcai.lib.resource.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.NumberUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author CJ
 */
@Controller
@RequestMapping("/manage/widget")
@PreAuthorize("hasRole('ROOT')")
public class WidgetInfoController
        extends CRUDController<WidgetInfo, WidgetIdentifier, HttpServletRequest, Long> {

    @Autowired
    Environment environment;
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
    protected void prepareUpdate(Login login, WidgetInfo entity, WidgetInfo data, Long extra
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

    @ResponseBody
    @PreAuthorize("hasRole('" + Login.Role_Manage_Value + "')")
    @RequestMapping(value = "/widgets",method = RequestMethod.GET,produces = "application/json; charset=UTF-8")
    public List<WidgetModel> getWidgetInfo() throws IOException, URISyntaxException {
        List<InstalledWidget> installedWidgets=widgetFactoryService.widgetList(null);
        if (environment.acceptsProfiles("test")) {
            try {
                if(installedWidgets==null && installedWidgets.size()==0) {
                    widgetFactoryService.installWidgetInfo(null, "com.huotu.hotcms.widget.picCarousel", "picCarousel"
                            , "1.0-SNAPSHOT", "picCarousel");
                }
            } catch (FormatException e) {
                e.printStackTrace();
            }
        }
        installedWidgets = widgetFactoryService.widgetList(null);
        Widget widget;
        List<WidgetModel> widgetModels = new ArrayList<>();
        for (InstalledWidget installedWidget : installedWidgets) {
            WidgetModel widgetModel = new WidgetModel();
            widget = installedWidget.getWidget();
            widgetModel.setLocallyName(widget.name(Locale.CHINA));
            WidgetStyle[] widgetStyles= widget.styles();
            String styleModel="<div>\n" +
                    "            <h3>\n" +
                    "                <i class=\"icon-puzzle\"></i><b></b>选择组件样式\n" +
                    "            </h3>\n" +
                    "            <div class=\"styles\">\n" +
                    "              %s "+
                    "            </div>\n" +
                    "        </div>\n";

            String item="<div class=\"style-item\">\n" +
                    "<div class=\"item-box\">"+
                    "<img src=\"%s\">\n"+
                    "<p>默认</p>\n" +
                    "</div>\n" +
                    "</div>\n";
            String prefix="widget/" + widget.groupId() + "-" + widget.widgetId()
                    + "-" + widget.version() + "/";
            StringBuilder sb=new StringBuilder();
            for (WidgetStyle widgetStyle:widgetStyles){
                URI src=resourceService.getResource(prefix+((ClassPathResource)widgetStyle.thumbnail()).getPath()).httpUrl().toURI();
                sb.append(String.format(item,src));
            }
            styleModel=String.format(styleModel,sb.toString());
            widgetModel.setEditorHTML(styleModel+widgetResolveService.editorHTML(widget, CMSContext.RequestContext(), null));
            //<groupId>-<widgetId>:<version>
            widgetModel.setIdentity(widget.groupId()+"-"+widget.widgetId()+":"+widget.version());

            //获取js资源uri
            for (Map.Entry<String, Resource> entry : widget.publicResources().entrySet()) {
                if (entry.getKey().endsWith(".js")) {
                    widgetModel.setScriptHref(resourceService.getResource("widget/" + widget.groupId() + "-"
                                    + widget.widgetId() + "-" + widget.version() + "/" + entry.getKey())
                                    .httpUrl().toURI().toString() + ","
                    );
                }
            }
            if (widgetModel.getScriptHref() != null && widgetModel.getScriptHref().length() > 0) {
                widgetModel.setScriptHref(widgetModel.getScriptHref().substring(0
                        , widgetModel.getScriptHref().length() - 1));
            }//

            widgetModel.setThumbnail(widget.thumbnail().getURL().toString());
            WidgetStyleModel[] widgetStyleModels = new WidgetStyleModel[widgetStyles.length];

            for (int i = 0; i < widgetStyles.length; i++) {
                WidgetStyleModel widgetStyleModel = new WidgetStyleModel();
                widgetStyleModel.setThumbnail(widgetStyles[i].thumbnail().getURI().toString());
                widgetStyleModel.setLocallyName(widgetStyles[i].name());
                widgetStyleModel.setPreviewHTML(widgetResolveService.previewHTML(widget, widgetStyles[i].id()
                        , CMSContext.RequestContext(), widget.defaultProperties()));
                widgetStyleModels[i] = widgetStyleModel;
            }
            widgetModel.setStyles(widgetStyleModels);
            widgetModels.add(widgetModel);
        }
        return widgetModels;
    }

}
