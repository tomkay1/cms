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
import com.huotu.hotcms.widget.InstalledWidget;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetLocateService;
import com.huotu.hotcms.widget.exception.FormatException;
import com.huotu.hotcms.widget.repository.WidgetInfoRepository;
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
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * Created by lhx on 2016/6/2.
 */
@Service
public class WidgetFactoryServiceImpl implements WidgetFactoryService, WidgetLocateService {

    private static final Log log = LogFactory.getLog(WidgetFactoryServiceImpl.class);

    private static final String PRIVATE_REPO = "http://repo.51flashmall.com:8081/nexus/content/groups/public/%s/%s/%s";
    private final List<InstalledWidget> installedWidgets = new ArrayList<>();
    @Autowired
    private WidgetInfoRepository widgetInfoRepository;
    @Autowired(required = false)
    private ResourceService resourceService;

    /**
     * 下载widget jar文件
     *
     * @param groupId  分组id,参考maven
     * @param version  版本
     * @param widgetId 控件id
     * @return 临时文件
     */
    private File downloadJar(String groupId, String widgetId, String version) throws IOException {
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
            //noinspection ResultOfMethodCallIgnored
            file.delete();
        }
        info.setPath(path);
    }

    /**
     * 已安装控件列表
     *
     * @param owner
     * @return
     * @throws FormatException
     * @throws IOException
     */
    @Override
    public List<InstalledWidget> widgetList(Owner owner) {
        return installedWidgets.stream()
                // 过滤掉不要的控件
                .filter(widget -> owner == null || widget.getOwnerId() == null || widget.getOwnerId().equals(owner.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<InstalledWidget> installedStatus(WidgetInfo widgetInfo) {
        return installedStatus(widgetInfo.getIdentifier());
    }

    private List<InstalledWidget> installedStatus(WidgetIdentifier identifier) {
        return installedWidgets.stream()
                .filter(widget -> widget.getIdentifier().equals(identifier))
                .collect(Collectors.toList());
    }

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
                    //加载jar
                    installWidget(widgetInfo.getOwner(), (Widget) clazz.newInstance(), widgetInfo.getType())
                            .setIdentifier(widgetInfo.getIdentifier());
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
//        try {
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


    public void updateWidget(Widget widget) {
        throw new IllegalStateException("not support yet");
    }

    @Override
    public void primary(WidgetInfo widgetInfo) {
        throw new IllegalStateException("not support yet");
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
