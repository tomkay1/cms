/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.service;

import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.Component;
import com.huotu.hotcms.widget.ComponentProperties;
import com.huotu.hotcms.widget.InstalledWidget;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.exception.FormatException;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author CJ
 */
public interface WidgetFactoryService {

    /**
     * @return 已安装控件列表
     */
    @Transactional(readOnly = true)
    List<InstalledWidget> widgetList() throws FormatException, IOException;

    /**
     * 重新载入控件
     */
    @PostConstruct
    @Transactional(readOnly = true)
    void reloadWidgets() throws IOException, FormatException;

    /**
     * 安装新的控件
     * <p>
     * 从私有Maven仓库 http://repo.51flashmall.com:8081/nexus/content/groups/public 自动获取</p>
     * @param groupId  分组id,参考maven
     * @param version  版本
     * @param widgetId 控件id
     * @param type     控件类型
     */
    @Transactional
    void installWidget(String groupId, String widgetId, String version, String type) throws IOException, FormatException;

    /**
     * 以实例方式直接进行安装
     *
     * @param widget 控件实例
     * @param type   控件类型
     */
    @Transactional
    void installWidget(Widget widget, String type);

    /**
     * 更新已安装的控件
     * <p>
     * 需要检查每一个使用该控件的组件属性是否符合要求。<p/>
     * <p>
     * 如果使用了缓存系统,包括组件缓存和页面缓存,更新以后都需要清理缓存。</p>
     *
     * @param widget  原控件
     * @param jarFile 新的工程控件jar包
     */
    void updateWidget(Widget widget, InputStream jarFile) throws IOException;


    /**
     * 生成预览HTML代码
     *
     * @param widget     控件
     * @param styleId    控件样式id,可选
     * @param context    交互空间
     * @param properties 组件属性,可选
     * @return HTML Code
     */
    String previewHTML(Widget widget, String styleId, CMSContext context, ComponentProperties properties);

    /**
     * 生成编辑器HTML代码
     *
     * @param widget  控件
     * @param context 交互空间
     * @return HTML Code
     */
    String editorHTML(Widget widget, CMSContext context);

    /**
     * 生成一个组件的完整HTML代码
     * <p>
     * 页面的生成者应该是通过调用这个方法获取每一个控件的HTML,技术上我们限定生成一个页面绝对不可以超过0.5s,假定一个页面转载了200个控件
     * 也就是这个方法的响应时间不可以超过0.5/200s 也就是2.5ms</p>
     *
     * @param component 组件
     * @param context   上下文环境
     * @return HTML Code
     */
    String componentHTML(Component component, CMSContext context);

}
