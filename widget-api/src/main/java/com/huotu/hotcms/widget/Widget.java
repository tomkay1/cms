/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.Locale;

/**
 * 请参考<a href="https://huobanplus.quip.com/KngdAAGxtKSQ">控件技术标准</a>
 *
 * @author CJ
 */
public interface Widget {

    String groupId();

    String widgetId();

    /**
     * 接口提供了默认实现读取包信息中的实现作者
     *
     * @return 控件作者
     */
    default String author() {
        return getClass().getPackage().getImplementationVendor();
    }

    /**
     * 接口提供了默认实现读取头信息的API Implement.
     *
     * @return 控件版本
     */
    default String version() {
        return getClass().getPackage().getImplementationVersion();
    }

    /**
     * 接口提供了默认返货读取头信息中的产品信息
     *
     * @return 控件名称(无语言)
     */
    default String name() {
        return getClass().getPackage().getImplementationTitle();
    }

    String name(Locale locale);

    String description();

    String description(Locale locale);

    /**
     * 以int形式获取依赖widget-service的版本
     *
     * @return 依赖widget-service版本
     */
    int dependBuild();

    /**
     * @return 插件缩略图
     */
    default Resource thumbnail() {
        return new ClassPathResource("thumbnail.png", getClass().getClassLoader());
    }

    /**
     * @return 编辑器的模板资源
     */
    default Resource editorTemplate() {
        return new ClassPathResource("editor.html", getClass().getClassLoader());
    }


    /**
     * @return 编辑器的js资源路径
     */
    Resource widgetJsHref();


    /**
     * 第一个样式为默认样式
     * @return 有且至少有一个样式
     */
    WidgetStyle[] styles();

    /**
     * 检查以下的属性是否符合控件要求
     *
     * @param styleId
     * @param properties
     * @throws IllegalArgumentException 表示验证失败
     */
    void valid(String styleId, ComponentProperties properties) throws IllegalArgumentException;

    /**
     * 可以获取Spring额外配置信息
     * 如果该值非空,则控件在生成HTML的时候 应该使用其他的ApplicationContext而非默认的
     *
     * @return 可以为null
     */
    Class springConfigClass();


}
