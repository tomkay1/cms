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
import com.huotu.hotcms.service.util.ImageHelper;
import com.huotu.hotcms.widget.Component;
import com.huotu.hotcms.widget.InstalledWidget;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetLocateService;
import com.huotu.hotcms.widget.WidgetStyle;
import com.huotu.hotcms.widget.entity.PageInfo;
import com.huotu.hotcms.widget.exception.FormatException;
import com.huotu.hotcms.widget.page.Layout;
import com.huotu.hotcms.widget.page.PageElement;
import com.huotu.hotcms.widget.page.PageLayout;
import com.huotu.hotcms.widget.repository.WidgetInfoRepository;
import com.huotu.hotcms.widget.service.PageService;
import com.huotu.hotcms.widget.service.WidgetFactoryService;
import com.huotu.hotcms.widget.service.impl.http.DocumentResponseHandler;
import com.huotu.hotcms.widget.util.ClassLoaderUtil;
import me.jiangcai.lib.resource.service.ResourceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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

    public void downloadJar(String groupId, String widgetId, String version, OutputStream outputStream)
            throws IOException {
        groupId = groupId.replace(".", "/");
        StringBuilder repoUrl = new StringBuilder(String.format(PRIVATE_REPO, groupId, widgetId, version));

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            // 是否是 SNAPSHOT ?
            String jarUrl;
            if (version.endsWith("-SNAPSHOT")) {
                Document doc = httpClient.execute(new HttpGet(repoUrl + "/maven-metadata.xml")
                        , new DocumentResponseHandler());
                Node snapshot = doc.getElementsByTagName("snapshot").item(0);
                String timeBuild = nodeStream(snapshot.getChildNodes())
                        .filter((node) -> "timestamp".equalsIgnoreCase(node.getNodeName()))
                        .findAny()
                        .orElseThrow(IOException::new)
                        .getTextContent()
                        + "-"
                        + nodeStream(snapshot.getChildNodes())
                        .filter((node) -> "buildNumber".equalsIgnoreCase(node.getNodeName()))
                        .findAny()
                        .orElseThrow(IOException::new)
                        .getTextContent();

                // 从version中移除-SNAPSHOT
                String newVersion = version.substring(0, version.length() - "-SNAPSHOT".length());

                jarUrl = repoUrl
                        .append("/")
                        .append(widgetId)
                        .append("-")
                        .append(newVersion)
                        .append("-")
                        .append(timeBuild).append(".jar")
                        .toString();
            } else {
                jarUrl = repoUrl
                        .append("/")
                        .append(widgetId)
                        .append("-")
                        .append(version)
                        .append(".jar")
                        .toString();
            }

            log.debug("prepare to download " + jarUrl);

            try (CloseableHttpResponse response = httpClient.execute(new HttpGet(jarUrl))) {
                try (InputStream stream = response.getEntity().getContent()) {
                    StreamUtils.copy(stream, outputStream);
                }
            }

        }

    }

    /**
     * 这个其实可以公用 以后再重构
     *
     * @param nodeList
     * @return 可迭代的Node
     */
    private Iterable<Node> iterateNode(NodeList nodeList) {
        return () -> new Iterator<Node>() {
            private NodeList list = nodeList;
            private int index = 0;

            @Override
            public boolean hasNext() {
                return list.getLength() > index;
            }

            @Override
            public Node next() {
                return list.item(index++);
            }
        };
    }

    private Stream<Node> nodeStream(NodeList nodeList) {
        return StreamSupport.stream(iterateNode(nodeList).spliterator(), false);
    }

    @Override
    public void setupJarFile(WidgetInfo info, InputStream data) throws IOException {
        if (info.getPath() != null && resourceService.getResource(info.getPath()).exists()) {
            if (log.isDebugEnabled())
                log.debug("WidgetInfo " + info + "'s Package is existing.");
            if (resourceService.getResource(info.getPath()).contentLength() > 0)
                return;
        }
        String path = "widget/" + UUID.randomUUID().toString() + ".jar";
        if (data != null) {
            resourceService.uploadResource(path, data);
        } else {
            Path file = Files.createTempFile("cmsWidget", ".jar");
            try {
                try (OutputStream outputStream = Files.newOutputStream(file, StandardOpenOption.WRITE)) {
                    downloadJar(info.getGroupId(), info.getArtifactId(), info.getVersion(), outputStream);
                }
                try (InputStream inputStream = Files.newInputStream(file, StandardOpenOption.READ)) {
                    resourceService.uploadResource(path, inputStream);
                }
            } finally {
                //noinspection ResultOfMethodCallIgnored,ThrowFromFinallyBlock
                Files.deleteIfExists(file);
            }
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
        setupJarFile(widgetInfo, null);
        if (widgetInfo.getPath() == null)
            throw new IllegalStateException("无法获取控件包资源");
        try {
            widgetInfoRepository.save(widgetInfo);

            List<Class> classes = ClassLoaderUtil.loadJarWidgetClasses(resourceService.getResource(widgetInfo.getPath()));
            if (classes != null) {
                for (Class clazz : classes) {
                    Widget widget = (Widget) clazz.newInstance();
                    //加载jar
                    installWidget(widgetInfo.getOwner(), widget, widgetInfo.getType())
                            .setIdentifier(widgetInfo.getIdentifier());
                    // widget/js/base64(identifier).js

                    //上传控件资源
                    Map<String, Resource> publicResources = widget.publicResources();
                    WidgetIdentifier identifier = new WidgetIdentifier(widget.groupId(), widget.widgetId()
                            , widget.version());
                    if (publicResources != null) {
                        for (Map.Entry<String, Resource> entry : publicResources.entrySet()) {
                            resourceService.uploadResource("widget/" + identifier.toURIEncoded() + "/"
                                    + entry.getKey(), entry.getValue().getInputStream());
                        }
                    }

                    ImageHelper.storeAsImage("png", resourceService, widget.thumbnail().getInputStream()
                            , Widget.thumbnailPath(widget));

                    for (WidgetStyle style : widget.styles()) {
                        ImageHelper.storeAsImage("png", resourceService, style.thumbnail().getInputStream()
                                , WidgetStyle.thumbnailPath(widget, style));
                    }
                }
            }

        } catch (InstantiationException
                | IllegalAccessException | FormatException e) {
            throw new FormatException("Bad jar format", e);
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
                    Layout[] elements = PageLayout.NoNullLayout(page.getLayout());
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
                if (component.getInstalledWidget() != null) {
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
            for (PageElement element : layout.elements())
                primarUtil(element, installedWidget, notSupportComponent, supportPage, page);
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
