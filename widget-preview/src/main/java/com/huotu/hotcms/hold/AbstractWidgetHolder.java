/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.hotcms.hold;

import com.huotu.hotcms.bean.WidgetHolder;
import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.service.entity.support.WidgetIdentifier;
import com.huotu.hotcms.service.util.ImageHelper;
import com.huotu.hotcms.widget.InstalledWidget;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetStyle;
import me.jiangcai.lib.resource.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author CJ
 */
public class AbstractWidgetHolder implements WidgetHolder {

    private final Set<Widget> widgetSet;
    @Autowired
    private ResourceService resourceService;

    public AbstractWidgetHolder(Resource resource, WidgetLoader loader) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Properties properties = new Properties();
        try (InputStream inputStream = resource.getInputStream()) {
            properties.load(inputStream);
        }

        String classes = properties.getProperty("widgetClasses");
        assert classes != null;

        HashSet<Widget> widgetArrayList = new HashSet<>();
        for (String clazz : classes.split(",")) {
            Widget widget = loader.toWidget(clazz.trim());
            widgetArrayList.add(widget);
        }

        widgetSet = Collections.unmodifiableSet(widgetArrayList);
    }

    public InstalledWidget installWidget(Owner owner, Widget widget, String type) throws IOException {
        //持久化相应的信息
        InstalledWidget installedWidget = new InstalledWidget(widget);
        installedWidget.setType(type);
        if (owner != null) {
            installedWidget.setOwnerId(owner.getId());
        }
//        installedWidgets.add(installedWidget);

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
        ImageHelper.storeAsImage(resourceService, widget.thumbnail().getInputStream()
                , Widget.thumbnailPath(widget));
        for (WidgetStyle style : widget.styles()) {
            ImageHelper.storeAsImage(resourceService, style.thumbnail().getInputStream()
                    , WidgetStyle.thumbnailPath(widget, style));
        }

        return installedWidget;
    }

    @PostConstruct
    public void init() throws IOException {
        for (Widget widget : getWidgetSet()) {
            installWidget(null, widget, "normal");
        }
    }

    public Set<Widget> getWidgetSet() {
        return widgetSet;
    }

    @Override
    public InstalledWidget findWidget(String groupId, String widgetId, String version) {
//        return null;
        return findWidget(new WidgetIdentifier(groupId, widgetId, version).toString());
    }

    @Override
    public InstalledWidget findWidget(String identifier) {
        for (Widget widget : widgetSet) {
            if (Widget.WidgetIdentity(widget).equals(identifier)) {
                InstalledWidget widget1 = new InstalledWidget(widget);
                widget1.setType("normal");
                widget1.setIdentifier(WidgetIdentifier.valueOf(Widget.WidgetIdentity(widget)));
                return widget1;
            }
        }
        return null;
    }
}
