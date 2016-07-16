/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget;

import org.apache.http.entity.ContentType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.Locale;
import java.util.Map;

/**
 * 请参考<a href="https://huobanplus.quip.com/KngdAAGxtKSQ">控件技术标准</a>
 *
 * @author CJ
 */
public interface Widget {

    /**
     * 获得这个widget的唯一id
     * 这个还不稳定,需要及早确定唯一表达式
     *
     * @param widget 控件
     * @return 唯一id
     */
    static String WidgetIdentity(Widget widget) {
        return widget.groupId() + "-" + widget.widgetId() + ":" + widget.version();
    }

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
        return this.getClass().getPackage().getImplementationVersion();
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
     * 这个控件所需要的公开静态资源
     * key为资源的名字,可以通过在thymeleaf的w:src或者s:href属性获取运行时准确URL
     *
     * @return 没有的话可以为null
     */
    Map<String, Resource> publicResources();

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
     * 组装这个控件所依赖的content
     *
     * @param contentType 正文类型 比如javascript或者css或者image
     * @return 资源模板 具体模板技术自行实现。freemarker velocity
     */
    Resource widgetDependencyContent(ContentType contentType);

    /**
     * @return 页面的js资源
     * @deprecated instead by {@link #widgetDependencyContent(ContentType)}
     */
    Resource widgetJs();


    /**
     * 第一个样式为默认样式
     *
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
