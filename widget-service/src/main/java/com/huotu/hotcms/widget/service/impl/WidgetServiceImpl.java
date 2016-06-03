/*
 *
 *  版权所有:杭州火图科技有限公司
 *  地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 *  2013-2016. All rights reserved.
 *
 */

package com.huotu.hotcms.widget.service.impl;

import com.huotu.hotcms.widget.*;
import com.huotu.hotcms.widget.service.WidgetService;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;


/**
 * Created by wenqi on 2016/6/2.
 */
@Service
public class WidgetServiceImpl implements WidgetService {

    private static final String PRIVATE_REPO="http://repo.51flashmall.com:8081/nexus/content/groups/public/{0}/{1}/{2}";

    @Override
    public List<InstalledWidget> widgetList() {
        return null;
    }

    @Override
    public void installWidget(String groupId, String widgetId, String version, String type) {
        groupId=groupId.replace(".","/");
        String repoUrl=String.format(PRIVATE_REPO,groupId,widgetId,version);
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
    public String componentHTML(Component component) {
        return null;
    }
}
