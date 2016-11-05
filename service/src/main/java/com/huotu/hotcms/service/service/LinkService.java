/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.model.LinkCategory;
import com.huotu.hotcms.service.model.thymeleaf.foreach.NormalForeachParam;
import com.huotu.hotcms.service.util.PageData;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LinkService {

    /**
     * 保存一个链接
     *
     * @param link
     * @return 已保存的链接实体
     */
    @Transactional
    Link save(Link link);

    PageData<LinkCategory> getPage(long ownerId, String title, int page, int pageSize);

    @Deprecated
    @Transactional
    Boolean saveLink(Link link);
    Link findById(Long id);

//    List<Link> getLinkList(NormalForeachParam linkForeachParam);

    List<Link> getSpecifyLinks(String[] specifyids);

    /**
     * 根据NormalForeachParam 实体类来获得分页链接模型数据列表,
     * NormalForeachParam 该实体类是通过编写的参数标签或者当前http 上下文 request中获得参数实体
     *
     * @param normalForeachParam 该实体类是通过编写的参数标签或者当前http 上下文 request中获得参数实体
     * @return
     * @throws Exception
     */
    Page<Link> getLinkList(NormalForeachParam normalForeachParam) throws Exception;
}
