/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.service.impl;

import com.huotu.hotcms.widget.entity.WidgetInfo;
import com.huotu.hotcms.widget.exception.FormatException;
import com.huotu.hotcms.widget.InstalledWidget;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.repository.WidgetRepository;
import com.huotu.hotcms.widget.service.WidgetFactoryService;
import com.huotu.hotcms.widget.util.ClassLoaderUtil;
import com.huotu.hotcms.widget.util.HttpClientUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.luffy.libs.libseext.XMLUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


/**
 * Created by wenqi on 2016/6/2.
 */
@Service
public class WidgetFactoryServiceImpl implements WidgetFactoryService {

    private static final Log log = LogFactory.getLog(CSSServiceImpl.class);

    private static final String PRIVATE_REPO = "http://repo.51flashmall.com:8081/nexus/content/groups/public/%s/%s/%s";

    @Autowired
    private WebApplicationContext webApplicationContext;

    public List<InstalledWidget> installedWidgets = new ArrayList<>();


    /**
     * 得到jar 在本地存储的真实路径
     *
     * @return
     */
    public String getRealPath(String widgetId, String version) {
        String rootPath = webApplicationContext.getServletContext().getRealPath("");
        String jarName = getJarName(widgetId, version);
        String realPath = rootPath + "/" + jarName;
        return realPath;
    }

    /**
     * 得到jar 名称
     *
     * @param widgetId
     * @param version
     * @return
     */
    public String getJarName(String widgetId, String version) {
        return widgetId + "-" + version + ".jar";
    }

    @Override
    public String downloadJar(String groupId, String widgetId, String version) throws IOException, ParserConfigurationException, SAXException {
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
        String jarName = getJarName(widgetId, version.replace("SNAPSHOT", "") + timeBuild);
        repoUrl.append("/");
        repoUrl.append(jarName);
        String realPath = getRealPath(widgetId, version);
        //下载jar
        HttpClientUtil.getInstance().downloadJar(repoUrl.toString(), realPath);
        return realPath;
    }

    /**
     * 已安装控件列表
     *
     * @return
     * @throws FormatException
     * @throws IOException
     */
    @Override
    public List<InstalledWidget> widgetList() throws FormatException, IOException {
        List<InstalledWidget> result = null;
        List<WidgetInfo> all = widgetRepository.findAll();
        if (all != null && all.size() > 0 && installedWidgets.size() <= 0) {
            result = new ArrayList<>();
            for (WidgetInfo widgetInfo : all) {
                InstalledWidget installedWidget = new InstalledWidget();

                String realPath = getRealPath(widgetInfo.getWidgetId(), widgetInfo.getVersion());
                try {
                    Widget widget = (Widget) ClassLoaderUtil.loadJarConfig(realPath).newInstance();
                    installedWidget.setWidget(widget);
                    installedWidget.setType(widgetInfo.getType());
                    result.add(installedWidget);
                    installedWidgets.add(installedWidget);
                } catch (InstantiationException | IllegalAccessException | FormatException e) {
                    throw new FormatException(e.toString());
                }
            }
        } else {
            return installedWidgets;
        }
        return result;
    }

    @Override
    public void reloadWidgets() throws IOException, FormatException {
        installedWidgets.clear();
        widgetList();
    }

    @Override
    public void installWidget(String groupId, String widgetId, String version, String type) throws IOException, FormatException {
        try {
            String realPath = downloadJar(groupId,widgetId,version);
            List<Class> classes = ClassLoaderUtil.loadJarWidgetClasss(realPath);
            if (classes != null) {
                for (Class classz : classes) {
                    //加载jar
                    installWidget((Widget) classz.newInstance(), type);
                }
            }
        } catch (ParserConfigurationException | SAXException | InstantiationException
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
        widgetRepository.save(widgetInfo);

    }


    @Override
    public void updateWidget(Widget widget, InputStream jarFile) throws IOException {
        //todo 检查每一个使用该控件的组件属性是否符合要求  假设控件widget符合要求
        //更新数据库信息
        updateWidget(widget);
        //替换jar包
        if (jarFile != null) {
            BufferedOutputStream bw = null;
            try {
                String realPath = getRealPath(widget.widgetId(), widget.version());
                byte[] result = new byte[1024];
                // 写入文件
                File f = new File(realPath);
                // 创建文件路径
                if (!f.getParentFile().exists()) {
                    f.getParentFile().mkdirs();
                }
                bw = new BufferedOutputStream(new FileOutputStream(realPath));
                while (jarFile.read(result) != -1) {
                    bw.write(result);
                }
            } finally {
                if (bw != null) bw.close();
                if (jarFile != null) jarFile.close();
            }
        }
    }

    public void updateWidget(Widget widget) {
        WidgetInfo widgetInfo = widgetRepository.findByWidgetIdAndVersion(widget.widgetId(), widget.version());
        if (widgetInfo != null) {
            widgetInfo.setGroupId(widget.groupId());
            widgetInfo.setName(widget.name());
            widgetInfo.setDependBuild(widget.dependBuild() + "");
            widgetInfo.setAuthor(widget.author());
            widgetRepository.saveAndFlush(widgetInfo);
        }
    }

    @Override
    public void updateWidget(String groupId, String widgetId, String version, String type) throws IOException, FormatException {
        try {
            String realPath = downloadJar(groupId,widgetId,version);
            List<Class> classes = ClassLoaderUtil.loadJarWidgetClasss(realPath);
            if (classes != null) {
                for (Class classz : classes) {
                    updateWidget((Widget) classz.newInstance());
                }
            }
        } catch (ParserConfigurationException | SAXException | InstantiationException
                | IllegalAccessException | FormatException e) {
            throw new FormatException(e.toString());
        }
    }

    @Autowired
    private WidgetRepository widgetRepository;


    @Override
    public List<WidgetInfo> getWidgetByOwerId(String owerID) {
        return widgetRepository.findByAuthor(owerID);
    }
}
