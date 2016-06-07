/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.service.impl;

import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.Component;
import com.huotu.hotcms.widget.ComponentProperties;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by wenqi on 2016/6/2.
 */
@Service
public class WidgetFactoryServiceImpl implements WidgetFactoryService {

    private static final Log log = LogFactory.getLog(CSSServiceImpl.class);

    private static final String PRIVATE_REPO="http://repo.51flashmall.com:8081/nexus/content/groups/public/%s/%s/%s";



    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private WidgetRepository widgetRepository;



    @Override
    public List<InstalledWidget> widgetList() {

        List<InstalledWidget> result = new ArrayList<>();
        List<WidgetInfo> all = widgetRepository.findAll();

        for(WidgetInfo widgetInfo : all){
            InstalledWidget installedWidget = new InstalledWidget();
            installedWidget.setWidget(widgetInfo);
            installedWidget.setType(widgetInfo.getType());
        }
        return  result;
    }

    @Override
    public void installWidget(String groupId, String widgetId, String version, String type) throws IOException, FormatException {

        groupId=groupId.replace(".","/");
        StringBuilder repoUrl=new StringBuilder(String.format(PRIVATE_REPO,groupId,widgetId,version));

        String jarName = widgetId+"-"+version+".jar";

        repoUrl.append("/");
        repoUrl.append(jarName);

        String rootPath = webApplicationContext.getServletContext().getRealPath("");
        String realPath = rootPath+"/"+jarName;

        //下载jar
        HttpClientUtil.getInstance().downloadJar(repoUrl.toString(),realPath);

        //加载jar
        try {
            installWidget((Widget)ClassLoaderUtil.loadJarConfig(realPath).newInstance(),type);
        } catch (InstantiationException |IllegalAccessException |ClassNotFoundException e) {
            throw new FormatException(e.toString());
        }
    }

    public void installWidget(Widget widget, String type){

        //持久化相应的信息
        WidgetInfo widgetInfo = new WidgetInfo();
        widgetInfo.setGroupId(widget.groupId());
        widgetInfo.setWidgetId(widget.widgetId());
        widgetInfo.setVersion(widget.version());
        widgetInfo.setAuthor(widget.author());
        widgetInfo.setDependBuild(widget.dependBuild()+"");
        widgetInfo.setName(widget.name());
        widgetInfo.setType(type);

        widgetRepository.save(widgetInfo);
    }

    @Override
    public void updateWidget(Widget widget, InputStream jarFile) {

    }

    @Override
    public String previewHTML(Widget widget, String styleId, CMSContext context, ComponentProperties properties) {
        return null;
    }

    @Override
    public String editorHTML(Widget widget, CMSContext context) {
        return null;
    }

    @Override
    public String componentHTML(Component component, CMSContext context) {
        return null;
    }
}
