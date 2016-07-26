/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.service.impl;

import com.huotu.hotcms.service.entity.WidgetInfo;
import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.service.entity.support.WidgetIdentifier;
import com.huotu.hotcms.service.exception.PageNotFoundException;
import com.huotu.hotcms.widget.Component;
import com.huotu.hotcms.widget.InstalledWidget;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetLocateService;
import com.huotu.hotcms.widget.entity.PageInfo;
import com.huotu.hotcms.widget.exception.FormatException;
import com.huotu.hotcms.widget.page.Layout;
import com.huotu.hotcms.widget.page.PageElement;
import com.huotu.hotcms.widget.repository.WidgetInfoRepository;
import com.huotu.hotcms.widget.service.PageService;
import com.huotu.hotcms.widget.service.WidgetFactoryService;
import com.huotu.hotcms.widget.util.ClassLoaderUtil;
import com.huotu.hotcms.widget.util.HttpClientUtil;
import me.jiangcai.lib.resource.service.ResourceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.luffy.libs.libseext.XMLUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * Created by lhx on 2016/6/2.
 */
@Service
public class WidgetFactoryServiceImpl implements WidgetFactoryService, WidgetLocateService {

    private static final Log log = LogFactory.getLog(WidgetFactoryServiceImpl.class);

    private static final String PRIVATE_REPO = "http://repo.51flashmall.com:8081/nexus/content/groups/public/%s/%s/%s";
    private final Set<InstalledWidget> installedWidgets = new HashSet<>();
    @Autowired
    private WidgetInfoRepository widgetInfoRepository;

    @Autowired(required = false)
    private ResourceService resourceService;

    @Autowired
    private PageService pageService;

    /**
     * 下载widget jar文件
     *
     * @param groupId  分组id,参考maven
     * @param version  版本
     * @param widgetId 控件id
     * @return 临时文件
     */
    public File downloadJar(String groupId, String widgetId, String version) throws IOException {
        groupId = groupId.replace(".", "/");
        StringBuilder repoUrl = new StringBuilder(String.format(PRIVATE_REPO, groupId, widgetId, version));
        CloseableHttpResponse response = HttpClientUtil.getInstance().get(repoUrl + "/maven-metadata.xml"
                , new HashMap<>());
        byte[] result = EntityUtils.toByteArray(response.getEntity());
        String timeBuild = "";
        Document doc;
        try {
            doc = XMLUtils.xml2doc(new ByteArrayInputStream(result));
        } catch (SAXException | ParserConfigurationException e) {
            throw new IOException(e);
        }
        NodeList nodeList = doc.getElementsByTagName("timestamp");
        NodeList buildNumber = doc.getElementsByTagName("buildNumber");
        if (nodeList != null) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                timeBuild = timeBuild + node.getTextContent();
            }
        }
        if (buildNumber != null) {
            for (int i = 0; i < buildNumber.getLength(); i++) {
                Node node = buildNumber.item(i);
                timeBuild = timeBuild + "-" + node.getTextContent();
            }
        }
        File file = File.createTempFile("CMSWidget", ".jar");
        file.deleteOnExit();
        //下载jar
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            HttpClientUtil.getInstance().webGet(repoUrl.toString() + "/" + widgetId + "-" + (version.split("-")[0])
                    + "-" + timeBuild + ".jar", outputStream);
        }
        return file;
    }

    @Override
    public void setupJarFile(WidgetInfo info, InputStream data) throws IOException {
        if (info.getPath() != null && resourceService.getResource(info.getPath()).exists()) {
            if (log.isDebugEnabled())
                log.debug("WidgetInfo " + info + "'s Package is existing.");
            return;
        }
        String path = "widget/" + UUID.randomUUID().toString() + ".jar";
        if (data != null) {
            resourceService.uploadResource(path, data);
        } else {
            File file = downloadJar(info.getGroupId(), info.getArtifactId(), info.getVersion());
            try (FileInputStream inputStream = new FileInputStream(file)) {
                resourceService.uploadResource(path, inputStream);
            }
            file.delete();
        }
        info.setPath(path);
    }

    /**
     * 已安装控件列表
     *
     * @param owner
     * @return
     */
    @Override
    public List<InstalledWidget> widgetList(Owner owner) {
        return installedWidgets.stream()
                // 过滤掉不要的控件
                .filter(widget -> owner == null || widget.getOwnerId() == null
                        || widget.getOwnerId().equals(owner.getId())).collect(Collectors.toList());
    }

    @Override
    public List<InstalledWidget> installedStatus(WidgetInfo widgetInfo) {
        return installedStatus(widgetInfo.getIdentifier());
    }

    private List<InstalledWidget> installedStatus(WidgetIdentifier identifier) {
        return installedWidgets.stream()
                .filter(widget -> widget.getIdentifier().toString().equals(identifier.toString()))
                .collect(Collectors.toList());
    }

    @PostConstruct
    @Transactional(readOnly = true)
    @Override
    public synchronized void reloadWidgets() throws IOException, FormatException {
        installedWidgets.clear();
        //载入控件
        for (WidgetInfo widgetInfo : widgetInfoRepository.findByEnabledTrue()) {
            installWidgetInfo(widgetInfo.getOwner(), widgetInfo.getGroupId(), widgetInfo.getArtifactId()
                    , widgetInfo.getVersion(), widgetInfo.getType());
        }
    }

    @Override
    public void installWidgetInfo(WidgetInfo widgetInfo) throws IOException, FormatException {
        setupJarFile(widgetInfo, new FileInputStream(downloadJar(widgetInfo.getGroupId(), widgetInfo.getArtifactId()
                , widgetInfo.getVersion())));
        widgetInfoRepository.save(widgetInfo);

        if (widgetInfo.getPath() == null)
            throw new IllegalStateException("无法获取控件包资源");
        try {
            List<Class> classes = ClassLoaderUtil.loadJarWidgetClasses(resourceService.getResource(widgetInfo.getPath()));
            if (classes != null) {
                for (Class clazz : classes) {
                    Widget widget = (Widget) clazz.newInstance();
                    //加载jar
                    installWidget(widgetInfo.getOwner(), widget, widgetInfo.getType())
                            .setIdentifier(widgetInfo.getIdentifier());
                    //上传控件资源
                    Map<String, Resource> publicResources = widget.publicResources();
                    if (publicResources != null) {
                        for (Map.Entry<String, Resource> entry : publicResources.entrySet())
                            resourceService.uploadResource("widget/" + widget.groupId() + "-" + widget.widgetId()
                                    + "-" + widget.version() + "/"
                                    + entry.getKey(), entry.getValue().getInputStream());
                    }
                }
            }

        } catch (InstantiationException
                | IllegalAccessException | FormatException e) {
            throw new FormatException(e.toString());
        }
    }

    @Override
    public void installWidgetInfo(Owner owner, String groupId, String artifactId, String version, String type)
            throws IOException, FormatException {
        WidgetInfo widgetInfo = widgetInfoRepository.findOne(new WidgetIdentifier(groupId, artifactId, version));
        if (widgetInfo == null) {
            log.debug("New Widget " + groupId + "," + artifactId + ":" + version);
            widgetInfo = new WidgetInfo();
            widgetInfo.setGroupId(groupId);
            widgetInfo.setArtifactId(artifactId);
            widgetInfo.setVersion(version);
            widgetInfo.setEnabled(true);
            widgetInfo.setCreateTime(LocalDateTime.now());
        }
        widgetInfo.setType(type);
        widgetInfo.setOwner(owner);

        installWidgetInfo(widgetInfo);
    }

    public InstalledWidget installWidget(Owner owner, Widget widget, String type) {
        //持久化相应的信息
        InstalledWidget installedWidget = new InstalledWidget(widget);
        installedWidget.setType(type);
        if (owner != null) {
            installedWidget.setOwnerId(owner.getId());
        }
        installedWidgets.add(installedWidget);
        return installedWidget;
    }

    @Override
    public void updateWidget(Widget widget) throws IOException, FormatException {
        //查找控件
        List<WidgetInfo> widgetInfoList = widgetInfoRepository.findByGroupIdAndArtifactIdAndEnabledTrue(widget.groupId()
                , widget.widgetId());
        //设置不等于改控件版本的为不可用状态
        for (WidgetInfo widgetInfo : widgetInfoList) {
            if (!widgetInfo.getVersion().equals(widget.version())) {
                widgetInfo.setEnabled(false);
                widgetInfoRepository.saveAndFlush(widgetInfo);
            }
        }
    }

    @Override
    public void primary(WidgetInfo widgetInfo, boolean ignoreError) throws IllegalStateException, IOException, PageNotFoundException {
        List<InstalledWidget> installedWidgetList = installedStatus(widgetInfo);
        if (installedWidgetList != null && installedWidgetList.size() > 0) {

            InstalledWidget installedWidget = findWidget(widgetInfo.getGroupId(), widgetInfo.getArtifactId()
                    , widgetInfo.getVersion());

            //不支持的界面，和具体组件
            Map<Long, PageInfo> notSupportPage = new HashMap<>();
            Map<Long, PageInfo> supportPage = new HashMap<>();
            List<PageInfo> pageList = pageService.findAll();
            if (pageList != null && pageList.size() > 0) {
                for (PageInfo page : pageList) {
                    //检查所有界面使用该组件的参数是否合法，如果不合法添加到不支持的界面中
                    PageElement[] elements;
                    if (page.getLayout() != null)
                        elements = page.getLayout().getElements();
                    else
                        elements = new PageElement[0];
                    Set<Component> notSupportComponent = new HashSet<>();
                    for (PageElement element : elements) {
                        primarUtil(element, installedWidget, notSupportComponent, supportPage, page);
                    }
                    if (notSupportComponent.size() > 0) {
                        if (ignoreError) {
                            notSupportPage.put(page.getPageId(), page);
                        } else {
                            throw new IllegalStateException("安装的控件不能满足旧版本控件的参数异常");
                        }
                    }
                }
            }

            if (pageList != null && pageList.size() > 0) {
                for (PageInfo page : pageList) {
                    if (notSupportPage.size() > 0 && notSupportPage.get(page.getPageId()) != null) {
                        break;
                    }
                    if (supportPage.get(page.getPageId()) != null) {
                        if (ignoreError || notSupportPage.size() == 0) {
                            //更新页面
                            pageService.updatePageComponent(page, installedWidget);
                        }
                        break;
                    }

                }
            }
            //更新控件
            try {
                updateWidget(installedWidget.getWidget());
            } catch (FormatException e) {
                throw new IllegalStateException("更新完成,重新加载控件列表失败");
            }
        }
    }

    /**
     * 检查primary控件包
     * <p>验证控件包是否支持组件属性参数</p>
     *
     * @param installedWidget     控件包的安装控件
     * @param notSupportComponent 不支持当前控件包已安装的控件
     * @param supportPage
     * @param page
     */
    private void primarUtil(PageElement pageElement, InstalledWidget installedWidget
            , Set<Component> notSupportComponent, Map<Long, PageInfo> supportPage, PageInfo page) {
        if (pageElement instanceof Component) {
            Component component = (Component) pageElement;
            component.setInstalledWidget(findWidget(component.getWidgetIdentity()));
            try {
                if ( component.getInstalledWidget()!=null) {
                    Widget widget1 = component.getInstalledWidget().getWidget();
                    Widget widget2 = installedWidget.getWidget();
                    //同一个控件不同版本才进行验证
                    if (widget1.groupId().equals(widget2.groupId()) && widget1.widgetId().equals(widget2.widgetId())
                            && !widget1.version().equals(widget2.version())) {
                        installedWidget.getWidget().valid(component.getStyleId(), component.getProperties());
                        supportPage.put(page.getPageId(), page);
                    }
                }
            } catch (IllegalArgumentException e) {
                log.info("不支持的页面组件" + component.getWidgetIdentity() + ":" + e.getMessage());
                notSupportComponent.add(component);
                supportPage.remove(page.getPageId());
            }
        } else if (pageElement instanceof Layout) {
            Layout layout = (Layout) pageElement;
            for (PageElement element : layout.getElements()) {
                primarUtil(element, installedWidget, notSupportComponent, supportPage, page);
            }
        }
    }


    @Override
    public InstalledWidget findWidget(String groupId, String widgetId, String version) {
        WidgetIdentifier id = new WidgetIdentifier(groupId, widgetId, version);
        return findWidget(id);
    }

    private InstalledWidget findWidget(WidgetIdentifier id) {
        return installedWidgets.stream()
                .filter(installedWidget -> {
                    Widget widget = installedWidget.getWidget();
                    return id.getGroupId().equals(widget.groupId()) && id.getArtifactId().equals(widget.widgetId())
                            && id.getVersion().equals(widget.version());
                })
                .findFirst()
                .orElse(null);
    }

    @Override
    public InstalledWidget findWidget(String identifier) {
        return findWidget(WidgetIdentifier.valueOf(identifier));
    }


}
