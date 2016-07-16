/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.service;

import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.PageInfo;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.exception.PageNotFoundException;
import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.InstalledWidget;
import com.huotu.hotcms.widget.page.Page;

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
    String generateHTML(Page page, CMSContext context);


    /**生成一个页面的html
     * @param outputStream
     * @param page
     * @param context
     * @throws IOException
     * @see #generateHTML(Page, CMSContext)
     */
    void generateHTML(OutputStream outputStream, Page page, CMSContext context) throws IOException;


    /**
     * 保存某站点下编辑的界面信息
     * @param page  Page的配置信息
     * @param pageId 站点
     * @throws IOException jackson相关异常
     */
    void savePage(Page page,Long pageId) throws IOException;

    /**
     * 解析保存了{@link com.huotu.hotcms.widget.page.Page}信息的XML
     * <p>
     *     如果页面不存在，返回404
     * </p>
     *
     * @param pageId pageId
     * @return {@link com.huotu.hotcms.widget.page.Page}
     * @throws IOException 获取page失败
     * @throws PageNotFoundException 页面不存在
     */
    Page getPage(Long pageId) throws IOException,PageNotFoundException;

    /**
     * 删除相关页面信息
     *
     * @param pageId  页面ID
     * @throws IOException 删除page失败
     */
    void deletePage(Long pageId);


    /**
     * <p>返回当前站点下指定path的具体page</p>
     *
     * @param site     当前站点必须存在不能为空
     * @param pagePath pagePath必须存在不能为空
     * @return {@link com.huotu.hotcms.widget.page.Page}
     * @throws IllegalStateException 未找到page
     */
    Page findBySiteAndPagePath(Site site, String pagePath) throws IllegalStateException, PageNotFoundException;


    /**
     * 根绝站点读取Page列表
     * @param site 站点
     * @return Page列表
     */
    List<PageInfo> getPageList(Site site);

    /**
     * <p>返回path对应的界面如果存在返回界面</p>
     * <p>>path如果不存在,查找指定数据源下的最接近的界面</p>
     * @param category 相关数据源
     * @param path 请求的路径
     * @return 最适用的内容页
     * @throws IOException 获取界面xml错误
     */
    Page getClosestContentPage(Category category, String path) throws IOException, PageNotFoundException;

    /**
     * 返回所有page
     *
     * @return
     */
    List<Page> findAll() throws IOException, PageNotFoundException;

    /**
     * 更新page，并清除缓存
     *
     * @param page
     * @param installedWidget 更新的组件
     * @throws IllegalStateException
     */
    void updatePageComponent(Page page, InstalledWidget installedWidget) throws IllegalStateException;

}
