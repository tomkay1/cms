package com.huotu.hotcms.service.widget.service;

/**
 * Created by fawzi on 2016/5/9.
 */

import com.huotu.hotcms.service.widget.model.WidgetTemplateType;

/**
 * 组件默认模板服务
 * <p>
 *     可以在此服务中初始化各种所需要的模板
 * </p>
 */
public interface WidgetTemplateService {

    void initDefaultWidgetTemplate(WidgetTemplateType widgetTemplateType);

}
