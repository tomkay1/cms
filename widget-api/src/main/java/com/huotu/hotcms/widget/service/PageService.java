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
import com.huotu.hotcms.service.exception.PageNotFoundException;
import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.InstalledWidget;
import com.huotu.hotcms.widget.entity.PageInfo;
import com.huotu.hotcms.widget.page.PageModel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * 页面服务
 *
 * @author CJ
 */
public interface PageService {

    /**
     * 生成一个页面的html
     * 这个结果无需缓存,因为上下文的变化应该是比较大的。
     *
     * @param page 页面
     * @return html
     * @throws IOException
     */
    String generateHTML(PageInfo page, CMSContext context);


    /**
     * 生成一个页面的html
     *
     * @param outputStream
     * @param page
     * @param context
     * @throws IOException
     * @see #generateHTML(PageInfo, CMSContext)
     */
    void generateHTML(OutputStream outputStream, PageInfo page, CMSContext context) throws IOException;


    /**
     * 保存某站点下编辑的界面信息
     * 重新构造关于该页面的一切缓存,包括样式，脚本
     *
     * @param page   Page的配置信息,可以为空表示仅仅重新加载页面
     * @param pageId 页面id
     * @throws IOException jackson相关异常
     */
    void savePage(PageModel page, Long pageId) throws IOException;

    /**
     * 获取页面
     *
     * @param pageId pageId
     * @throws PageNotFoundException 页面不存在
     */
    PageInfo getPage(Long pageId) throws PageNotFoundException;

    /**
     * 删除相关页面信息
     *
     * @param pageId 页面ID
     * @throws IOException 删除page失败
     */
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
     * <p>>path如果不存在,查找指定数据源下的最接近的界面</p>
     *
     * @param category 相关数据源
     * @param path     请求的路径
     * @return 最适用的内容页
     * @throws IOException 获取界面xml错误
     */
    PageInfo getClosestContentPage(Category category, String path) throws IOException, PageNotFoundException;

    /**
     * 返回所有page
     *
     * @return
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
