package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.entity.WidgetMains;
import com.huotu.hotcms.service.entity.WidgetType;
import com.huotu.hotcms.service.service.WidgetService;
import com.huotu.hotcms.service.util.PageData;

/**
 * Created by chendeyu on 2016/3/17.
 */
public class WidgetServiceImpl implements WidgetService {
    @Override
    public PageData<WidgetType> getPage(String name, int page, int pageSize) {
        return null;
    }

    @Override
    public Boolean saveWidgetType(WidgetType widgetType) {
        return null;
    }

    @Override
    public Boolean saveWidgetMains(WidgetType widgetType) {
        return null;
    }

    @Override
    public WidgetType findWidgetTypeById(Long id) {
        return null;
    }

    @Override
    public WidgetMains findWidgetMainsTypeById(Long id) {
        return null;
    }
}
