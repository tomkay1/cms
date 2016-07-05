/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.service;

import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.page.Page;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
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


    void savePage(Page page, Long pageId) throws IOException, URISyntaxException;

    /**
     * 解析保存了{@link com.huotu.hotcms.widget.page.Page}信息的XML
     *
     * @param pageId pageId
     * @return {@link com.huotu.hotcms.widget.page.Page}
     * @throws IOException 其他异常
     */
    Page getPage(Long pageId) throws IOException;

    /**
     * 删除相关页面信息
     *
     * @param pageId  页面ID
     * @throws IOException 其他异常
     */
    void deletePage(Long pageId) throws IOException;


    /**
     * <p>返回当前站点下指定path的具体page</p>
     *
     * @param site     当前站点必须存在不能为空
     * @param pagePath pagePath必须存在不能为空
     * @return {@link com.huotu.hotcms.widget.page.Page}
     * @throws IllegalStateException 未找到page
     */
    Page findBySiteAndPagePath(Site site, String pagePath) throws IllegalStateException;


    /**
     * 根绝站点读取Page列表
     * @param siteId 站点ID
     * @return Page列表
     */
    List<Page> getPageList(long siteId);

    /**
     *
     * @param category 相关数据源
     * @param path 请求的路径
     * @return 最适用的内容页
     */
    Page getClosetContentPage(Category category, String path);

}
