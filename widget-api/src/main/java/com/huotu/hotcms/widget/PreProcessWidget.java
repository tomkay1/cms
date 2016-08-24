package com.huotu.hotcms.widget;

import java.util.Map;

/**
 * 具备预先处理Context的控件
 * Created by lhx on 2016/8/24.
 */
public interface PreProcessWidget {

    /**
     * 在准备使用这个控件生成的组件执行模板行为的时候运行
     *
     * @param style
     * @param properties
     * @param variables
     * @see CMSContext
     */
    void prepareContext(WidgetStyle style, ComponentProperties properties, Map<String, Object> variables);
}
