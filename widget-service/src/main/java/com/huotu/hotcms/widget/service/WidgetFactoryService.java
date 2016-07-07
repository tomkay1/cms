/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.service;

import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.widget.InstalledWidget;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.exception.FormatException;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 控件工厂服务,可以在此管理控件。
 *
 * @author CJ
 */
public interface WidgetFactoryService {

    /**
     * 安装指定控件的jar包
     *
     * @param info 控件信息
     * @param data 可以未空表示需要自行下载
     * @throws IOException
     */
    void setupJarFile(WidgetInfo info, InputStream data) throws IOException;

    /**
     * @return 已安装控件列表
     * @param owner
     */
    @Transactional(readOnly = true)
    List<InstalledWidget> widgetList(Owner owner) throws FormatException, IOException, IllegalAccessException, InstantiationException;

    /**
     * 重新载入控件
     */
    @PostConstruct
    @Transactional(readOnly = true)
    void reloadWidgets() throws IOException, FormatException, InstantiationException, IllegalAccessException;

    /**
     * 安装新的控件
     * <p>
     * 从私有Maven仓库 http://repo.51flashmall.com:8081/nexus/content/groups/public 自动获取</p>
     *
     * @param groupId  分组id,参考maven
     * @param widgetId 控件id
     * @param version  版本
     * @param type     控件类型
     * @param owner    专属商户
     */
    @Transactional
    void installWidget(Owner owner, String groupId, String widgetId, String version, String type) throws IOException, FormatException;

    /**
     * 以实例方式直接进行安装
     *
     * @param widget 控件实例
     * @param type   控件类型
     * @param owner  用户
     */
    @Transactional
    void installWidget(Owner owner, Widget widget, String type);

    /**
     * 更新已安装的控件
     * <p>
     * 需要检查每一个使用该控件的组件属性是否符合要求。<p/>
     * <p>
     * 如果使用了缓存系统,包括组件缓存和页面缓存,更新以后都需要清理缓存。</p>
     * @param widget  原控件
     * @param jarFile 新的工程控件jar包
     */
    void updateWidget(Widget widget, InputStream jarFile) throws IOException;


    /**
     * 更新数据库已经安装后的控件
     *
     * @param widget 控件
     */
    void updateWidget(Widget widget);

    /**
     * 更新已安装的控件
     * <p>
     * 需要检查每一个使用该控件的组件属性是否符合要求。<p/>
     * <p>
     * 如果使用了缓存系统,包括组件缓存和页面缓存,更新以后都需要清理缓存。</p>
     *
     * @param groupId  分组id,参考maven
     * @param version  版本
     * @param widgetId 控件id
     * @param type     控件类型
     */
    void updateWidget(String groupId, String widgetId, String version, String type) throws IOException
            , FormatException;


    /**
     * 查询指定owner的安装列表
     *
     * @param owner
     * @return
     */
    List<WidgetInfo> getWidgetByOwner(Owner owner);


}
