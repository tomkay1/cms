/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget;

import org.springframework.core.io.Resource;

import java.util.Locale;

/**
 * 控件样式
 *
 * @author CJ
 */
public interface WidgetStyle {

    /**
     * @return 这个样式的id, 将反应在 {@link Component#styleId}
     */
    String id();

    /**
     * @return 样式名称
     */
    default String name() {
        return name(Locale.CHINA);
    }

    /**
     * @param locale 区域
     * @return 本地化样式名称
     */
    String name(Locale locale);

    /**
     * @return 样式描述
     */
    default String description() {
        return description(Locale.CHINA);
    }

    /**
     * @param locale 区域
     * @return 本地化样式描述
     */
    String description(Locale locale);

    /**
     * @return 样式缩略图, 格式需符合106*82 不可为null
     */
    Resource thumbnail();

    /**
     * @return 预览模板, 既在编辑器中所见模板, 可以为null
     */
    Resource previewTemplate();

    /**
     * @return 浏览模板, 既最终模板, 必须存在不然爆炸
     */
    Resource browseTemplate();

}
