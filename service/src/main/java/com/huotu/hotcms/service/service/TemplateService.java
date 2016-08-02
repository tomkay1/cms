/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.Template;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * 模板站点的相关操作
 */
public interface TemplateService {
    /**
     * 点赞
     *
     * @param templateId 模板的ID
     * @param ownerId    当前登录用户的用户名  -用户名应该是唯一的
     * @param behavior   用户行为，1表示点赞，0表示取消赞
     * @return 是否点赞成功，存在网络异常，服务器异常等失误情况
     */
    @Transactional
    boolean laud(long templateId, long ownerId, int behavior);

    /**
     * 有人浏览了这个模板的首页，所以认为这是一次预览
     *
     * @param template 相关模板
     */
    @Transactional
    void preview(Template template);

    /**
     * 使用
     *
     * @param templateSiteID 模板站点ID
     * @param customerSiteId 商户站点ID
     * @param mode           模式<ul>
     *                       <li>0为追加模式 - 保持现有数据，再将模板站点下的Category,Page,Content复制到该站点中</li>
     *                       <li>1为替换模式 - 清空原有数据，然后进行复制</li>
     *                       </ul>
     */
    @Transactional
    void use(long templateSiteID, long customerSiteId, int mode) throws IOException;


    /**
     * 是否点赞
     *
     * @param ownerId    ownerId
     * @param templateId templateId 模板的ID
     * @return 是否已点赞
     */
    boolean isLauded(long templateId, long ownerId);
}
