/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.controller;

import com.huotu.hotcms.widget.page.Page;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by wenqi on 2016/5/27.
 */

/**
 *<b>页面管理服务</b>
 * <p>
 *     <em>响应码说明：</em>
 *     <ul>
 *         <li>202-成功接收客户端发来的请求</li>
 *         <li>502-出现异常，具体待定</li>
 *     </ul>
 * </p>
 *
 * @author wenqi
 * @since v2.0
 */
@Controller
public interface PageController {
    /**
     * <p>获取页面{@link Page}</p>
     * @param ownerId 拥有者id
     * @param pageId 页面ID
     * @return 拿到相应的界面
     * @see Page
     */
    Page getPage(long ownerId,String pageId) throws IOException;

    /**
     * <p>保存界面{@link Page}</p>
     * @param pageId 页面ID
     * @throws IOException 从request中读取请求体时异常
     */
    void savePage(long ownerId,String pageId,HttpServletRequest request) throws IOException, URISyntaxException;

    /**
     * <p>添加页面{@link Page}</p>
     * @param ownerId 拥有者id
     * @throws IOException 从request中读取请求体时异常
     */
    void addPage(long ownerId,HttpServletRequest request) throws IOException;

    /**
     * <p>删除界面{@link Page}</p>
     * @param pageId 页面ID
     */
    void deletePage(String pageId,long ownerId) throws IOException;

    /**
     * 保存页面部分属性
     * @param pageId 页面ID
     * @param propertyName 要保存的属性名
     */
    void savePagePartProperties(String pageId,String propertyName);
}
