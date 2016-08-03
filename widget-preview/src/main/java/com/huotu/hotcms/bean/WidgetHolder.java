package com.huotu.hotcms.bean;

import com.huotu.hotcms.widget.InstalledWidget;
import com.huotu.hotcms.widget.WidgetLocateService;

/**
 * Created by lhx on 2016/8/3.
 */

public class WidgetHolder implements WidgetLocateService {
    @Override
    public InstalledWidget findWidget(String groupId, String widgetId, String version) {
        return null;
    }

    @Override
    public InstalledWidget findWidget(String identifier) {
        return null;
    }
}
