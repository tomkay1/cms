/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.service.impl;

import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.widget.InstalledWidget;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetLocateService;
import com.huotu.hotcms.widget.entity.WidgetInfo;
import com.huotu.hotcms.widget.entity.support.WidgetIdentifier;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class WidgetFactoryServiceImpl implements WidgetFactoryService, WidgetLocateService {

    private static final Log log = LogFactory.getLog(CSSServiceImpl.class);

    private static final String PRIVATE_REPO = "http://repo.51flashmall.com:8081/nexus/content/groups/public/%s/%s/%s";
    private List<InstalledWidget> installedWidgets = new ArrayList<>();
    @Autowired
    private WidgetInfoRepository widgetInfoRepository;
    @Autowired
    private ResourceService resourceService;


    /**
     * 下载widget jar文件
     *
     * @param groupId  分组id,参考maven
     * @param version  版本
     * @param widgetId 控件id
     * @return 临时文件
     */
    private File downloadJar(String groupId, String widgetId, String version) throws IOException
            , ParserConfigurationException, SAXException {
        groupId = groupId.replace(".", "/");
        StringBuilder repoUrl = new StringBuilder(String.format(PRIVATE_REPO, groupId, widgetId, version));
        CloseableHttpResponse response = HttpClientUtil.getInstance().get(repoUrl + "/maven-metadata.xml", new HashMap<>());
        byte[] result = EntityUtils.toByteArray(response.getEntity());
        String timeBuild = "";
        Document doc = XMLUtils.xml2doc(new ByteArrayInputStream(result));
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
            HttpClientUtil.getInstance().webGet(repoUrl.toString(), outputStream);
        }

        return file;
    }

    @Override
    public void setupJarFile(WidgetInfo info, InputStream data) throws IOException {
        String path = "widget/" + info.getIdentifier().toString() + ".jar";
        if (data != null) {
            resourceService.uploadResource(path, data);
        } else {
            try {
                File file = downloadJar(info.getGroupId(), info.getWidgetId(), info.getVersion());
                try (FileInputStream inputStream = new FileInputStream(file)) {
                    resourceService.uploadResource(path, inputStream);
                }
                file.delete();
            } catch (ParserConfigurationException | SAXException e) {
                throw new IOException(e);
            }
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
    public List<InstalledWidget> widgetList(Owner owner) throws IOException, FormatException, IllegalAccessException
            , InstantiationException {
        List<InstalledWidget> result;
        List<WidgetInfo> widgetInfos;
        if (owner == null) {
            widgetInfos = widgetInfoRepository.findAll();
        } else {
            widgetInfos = widgetInfoRepository.findByOwner(owner);
        }
        if (widgetInfos != null && widgetInfos.size() > 0 && installedWidgets.size() <= 0) {
            result = new ArrayList<>();
            for (WidgetInfo widgetInfo : widgetInfos) {

                InstalledWidget installedWidget = getInstalledWidget(widgetInfo);
                installedWidgets.add(installedWidget);
                result.add(installedWidget);
            }
        } else {
            return installedWidgets;
        }
        return result;
    }

    @Override
    public void reloadWidgets() throws IOException, FormatException, InstantiationException, IllegalAccessException {
        installedWidgets.clear();
        widgetList(null);
    }

    @Override
    public void installWidget(String groupId, String widgetId, String version, String type, Owner owner) throws IOException
            , FormatException {
        try {
            WidgetInfo widgetInfo = widgetInfoRepository.findOne(new WidgetIdentifier(groupId, widgetId, version));
            if (widgetInfo == null) {
                log.debug("New Widget " + groupId + "," + widgetId + ":" + version);
                widgetInfo = new WidgetInfo();
                widgetInfo.setGroupId(groupId);
                widgetInfo.setWidgetId(widgetId);
                widgetInfo.setVersion(version);
            }
            widgetInfo.setType(type);
            widgetInfo.setOwner(owner);

            setupJarFile(widgetInfo, null);
            widgetInfoRepository.save(widgetInfo);

            List<Class> classes = ClassLoaderUtil.loadJarWidgetClasses(resourceService.getResource(widgetInfo.getPath()));
            if (classes != null) {
                for (Class clazz : classes) {
                    //加载jar
                    installWidget((Widget) clazz.newInstance(), type);
                }
            }
        } catch (InstantiationException
                | IllegalAccessException | FormatException e) {
            throw new FormatException(e.toString());
        }

    }

    public void installWidget(Widget widget, String type) {
        //持久化相应的信息
        WidgetInfo widgetInfo = new WidgetInfo();
        widgetInfo.setGroupId(widget.groupId());
        widgetInfo.setWidgetId(widget.widgetId());
        widgetInfo.setVersion(widget.version());
        widgetInfo.setType(type);
        widgetInfoRepository.save(widgetInfo);
    }


    @Override
    public void updateWidget(Widget widget, InputStream jarFile) throws IOException {
        //todo 检查每一个使用该控件的组件属性是否符合要求  假设控件widget符合要求
        //更新数据库信息
        updateWidget(widget);
        //todo 替换jar包
    }

    public void updateWidget(Widget widget) {
        throw new IllegalStateException("not support yet");
    }

    @Override
    public void updateWidget(String groupId, String widgetId, String version, String type) throws IOException
            , FormatException {
        throw new IllegalStateException("not support yet");
    }


    @Override
    public List<WidgetInfo> getWidgetByOwner(Owner owner) {
        return widgetInfoRepository.findByOwner(owner);
    }

    @Override
    public InstalledWidget findWidget(String groupId, String widgetId, String version) {
        WidgetIdentifier id = new WidgetIdentifier(groupId, widgetId, version);
        WidgetInfo widgetInfo = widgetInfoRepository.findOne(id);
        return getInstalledWidget(widgetInfo);
    }

    @Override
    public InstalledWidget findWidget(String identifier) {
        WidgetInfo widgetInfo = widgetInfoRepository.findOne(WidgetIdentifier.valueOf(identifier));
        return getInstalledWidget(widgetInfo);
    }


    /**
     * <p>根据安装控件信息获得InstalledWidget</p>
     *
     * @param widgetInfo
     * @return InstalledWidget
     */
    private InstalledWidget getInstalledWidget(WidgetInfo widgetInfo) {
        List<Class> classes;
        InstalledWidget installedWidget = null;
        try {
            classes = ClassLoaderUtil.loadJarWidgetClasses(resourceService.getResource(widgetInfo.getPath()));
            Widget widget;
            for (Class clazz : classes) {
                widget = (Widget) clazz.newInstance();
                if (widget.widgetId().equals(widgetInfo.getWidgetId())
                        && widget.version().equals(widgetInfo.getVersion())) {
                    installedWidget = new InstalledWidget(widget);
                    installedWidget.setType(widgetInfo.getType());
                    installedWidget.setOwnerId(widgetInfo.getOwner().getId());
                    installedWidget.setInstallWidgetId(widgetInfo.getWidgetId());
                }
            }
        } catch (IllegalAccessException | InstantiationException | FormatException | IOException ignored) {
        }
        return installedWidget;
    }
}
