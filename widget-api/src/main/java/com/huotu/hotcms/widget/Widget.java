/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget;

import com.huotu.hotcms.service.entity.support.WidgetIdentifier;
import me.jiangcai.lib.resource.service.ResourceService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

/**
 * 请参考<a href="https://huobanplus.quip.com/KngdAAGxtKSQ">控件技术标准</a>
 *
 * @author CJ
 */
public interface Widget {

    MediaType Javascript = MediaType.valueOf("application/javascript");
    MediaType CSS = MediaType.valueOf("text/css");
    MediaType HTML = MediaType.valueOf("text/html");

    /**
     * 获得这个widget的唯一id
     *
     * @param widget 控件
     * @return 唯一id
     */
    static String WidgetIdentity(Widget widget) {
        return new WidgetIdentifier(widget.groupId(), widget.widgetId(), widget.version()).toString();
    }

    /**
     * 获得这个widget的唯一id,这个结果可以直接添加在URI中
     *
     * @param widget 控件
     * @return 唯一id
     */
    static String URIEncodedWidgetIdentity(Widget widget) {
        return new WidgetIdentifier(widget.groupId(), widget.widgetId(), widget.version()).toURIEncoded();
    }

    /**
     * @return 注意这是相对于context的, 应该注意要再加上 {@link ServletContext#getContextPath()}
     */
    static String widgetJsResourceURI(Widget widget) {
        StringBuilder stringBuilder = new StringBuilder("/widget/");
        return stringBuilder.append(
                URIEncodedWidgetIdentity(widget))
                .append(".js").toString();
    }

    static String thumbnailPath(Widget widget) {
        StringBuilder stringBuilder = new StringBuilder("widgets/thumbnail/");
        return stringBuilder.append(
                URIEncodedWidgetIdentity(widget))
                .append(".png").toString();
    }

    /**
     * @return 语意上接近maven的groupId
     */
    String groupId();

    /**
     * @return 控件id
     */
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
     * @return 默认语言控件名称
     */
    default String name() {
        return name(Locale.CHINA);
    }

    /**
     * @param locale 语言区域
     * @return 控件本地化名称
     */
    String name(Locale locale);


    /**
     * @return 默认语言控件描述
     */
    default String description() {
        return description(Locale.CHINA);
    }

    /**
     * @param locale 语言区域
     * @return 控件本地化描述
     */
    String description(Locale locale);

    /**
     * 控件服务系统会检查该项目以确定是否支持这个控件。
     *
     * @return 依赖widget-service版本
     */
    String dependVersion();

    /**
     * 这个控件所需要的公开静态资源
     * key为资源的名字,可以通过在thymeleaf的w:src或者s:href属性获取运行时准确URL
     * <p>声明以后还需要在html中引入，这也是和{@link #widgetDependencyContent(MediaType) 模板依赖资源}的另一个
     * 区别</p>
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
     * <ul>
     * <li>Javascript
     * 要么为空或者返回不存在的资源,要么必然是<code>CMSWidgets.initWidget....</code>
     * 内容上也必须严格符合<a href="https://huobanplus.quip.com/KngdAAGxtKSQ">标准</a>
     * </li>
     * <li>CSS 同样也要求为模板</li>
     * </ul>
     * <p>作为控件依赖的资源，并不需要控件作者显式使用，控件在应用中会强制加载，这也是和{@link #publicResources() 静态资源}的另一个
     * 区别</p>
     *
     * @param mediaType 正文类型 比如javascript或者css或者image
     * @return 资源模板 具体模板技术依赖不同的contentType
     * @see #Javascript
     * @see #CSS
     */
    Resource widgetDependencyContent(MediaType mediaType);


    /**
     * 第一个样式为默认样式
     *
     * @return 有且至少有一个样式
     */
    WidgetStyle[] styles();

    /**
     * 校验控件的属性是否符合要求，以确保控件使用该属性可以{@link WidgetStyle#previewTemplate() 预览}
     * 和{@link WidgetStyle#browseTemplate() 浏览}。
     *
     * @param styleId    语义等同{@link Component#styleId}
     * @param properties 当前控件属性
     * @throws IllegalArgumentException 表示验证失败
     */
    void valid(String styleId, ComponentProperties properties) throws IllegalArgumentException;

    /**
     * 该版本暂时用不到
     * 可以获取Spring额外配置信息
     * 如果该值非空,则控件在生成HTML的时候 应该使用其他的ApplicationContext而非默认的
     *
     * @return 可以为null
     */
    Class springConfigClass();

    /**
     * 获取控件默认参数
     *
     * @return 这个方法总是返回新建的实例而且从不为null, 即它的结果可以直接用于分发。
     */
    ComponentProperties defaultProperties(ResourceService resourceService) throws IOException;

}
