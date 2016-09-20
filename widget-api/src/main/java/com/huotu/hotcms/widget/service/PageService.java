/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.service;

import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.event.CopySiteEvent;
import com.huotu.hotcms.service.event.DeleteSiteEvent;
import com.huotu.hotcms.service.exception.PageNotFoundException;
import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.InstalledWidget;
import com.huotu.hotcms.widget.entity.PageInfo;
import com.huotu.hotcms.widget.page.Layout;
import com.huotu.hotcms.widget.page.PageLayout;
import com.huotu.hotcms.widget.page.PageModel;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * 页面服务
 *
 * @author CJ
 */
public interface PageService {

    @EventListener
    void siteDeleted(DeleteSiteEvent event) throws IOException;

    //    @TransactionalEventListener(CopySiteEvent.class)
    @EventListener
    void siteCopy(CopySiteEvent event) throws IOException;

    /**
     * 可使用的布局组
     *
     * @param layout 页面布局
     * @return 一个不可能为null的布局数组
     */
    Layout[] layoutsForUse(PageLayout layout);

    /**
     * 生成一个页面的html
     * 这个结果无需缓存,因为上下文的变化应该是比较大的。
     *
     * @param page 页面
     * @return html
     */
    String generateHTML(PageInfo page, CMSContext context);


    /**
     * 生成一个页面的html
     *
     * @param writer
     * @param page
     * @param context
     * @see #generateHTML(PageInfo, CMSContext)
     */
    void generateHTML(Writer writer, PageInfo page, CMSContext context) throws IOException;

    /**
     * 保存某站点下编辑的界面信息
     * 重新构造关于该页面的一切缓存,包括样式，脚本
     *
     * @param info    页面
     * @param model   可选的替换配置信息,可以为空表示仅仅重新加载页面
     * @param preview 预览模式的话并不会更新{@link PageInfo#resourceKey},仅仅更新资源
     * @throws IOException
     */
    void savePage(PageInfo info, PageModel model, boolean preview) throws IOException;

    /**
     * 获取页面
     *
     * @param pageId id
     * @throws PageNotFoundException 页面不存在
     */
    PageInfo getPage(Long pageId) throws PageNotFoundException;

    /**
     * 删除相关页面信息
     *
     * @param pageId 页面ID
     * @throws IOException 删除page失败
     */
    @Transactional
    void deletePage(Long pageId) throws IOException;

    /**
     * <p>返回当前站点下指定path的具体page</p>
     *
     * @param site     当前站点必须存在不能为空
     * @param pagePath pagePath必须存在不能为空
     * @return {@link PageInfo}
     * @throws IllegalStateException 解析错误
     * @throws PageNotFoundException 页面没找到
     */
    PageInfo findBySiteAndPagePath(Site site, String pagePath) throws IllegalStateException, PageNotFoundException;


    /**
     * 根绝站点读取Page列表
     *
     * @param site 站点
     * @return Page列表
     */
    List<PageInfo> getPageList(Site site);

    /**
     * <p>返回path对应的界面如果存在返回界面</p>
     * <p>path如果不存在,查找指定数据源下的最接近的界面</p>
     *
     * @param category 相关数据源
     * @param path     请求的路径
     * @return 最适用的内容页 永不为null
     * @throws PageNotFoundException 如果找不到页面
     */
    PageInfo getClosestContentPage(Category category, String path) throws PageNotFoundException;

    /**
     * 返回所有page
     *
     * @return 如果没有数据会返回一个size为0的List
     */
    List<PageInfo> findAll();

    /**
     * 更新page，并清除缓存
     *
     * @param page
     * @param installedWidget 更新的组件
     * @throws IllegalStateException
     */
    void updatePageComponent(PageInfo page, InstalledWidget installedWidget) throws IllegalStateException;

}
