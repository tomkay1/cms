package com.huotu.widget.controller;

import com.huotu.widget.model.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
@RequestMapping("/pages")
public interface PageController {
    /**
     * <p>获取页面{@link com.huotu.widget.model.Page}</p>
     * @param ownerId 拥有者id
     */
    Page getPage(long ownerId);

    /**
     * <p>保存界面{@link com.huotu.widget.model.Page}</p>
     */
    void savePage();

    /**
     * <p>添加页面{@link com.huotu.widget.model.Page}</p>
     * @param ownerId 拥有者id
     */
    void addPage(long ownerId);

    /**
     * <p>删除界面{@link com.huotu.widget.model.Page}</p>
     * @param pageId 页面ID
     */
    void deletePage(long pageId);
}
