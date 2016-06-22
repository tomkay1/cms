package com.huotu.hotcms.widget.service.impl;

import com.huotu.hotcms.widget.InstalledWidget;
import com.huotu.hotcms.widget.WidgetLocateService;
import org.springframework.stereotype.Service;

/**
 * Created by lhx on 2016/6/22.
 */
@Service
public class WidgetLocateServiceImpl implements WidgetLocateService {

    @Override
    public InstalledWidget findWidget(String groupId, String widgetId, String version) {
        return null;
    }

    @Override
    public InstalledWidget findWidget(String identifier) {
        return null;
    }
}
