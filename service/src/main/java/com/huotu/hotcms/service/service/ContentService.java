/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.entity.AbstractContent;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * 内容服务
 */
public interface ContentService {

    /**
     * 寻找一个正文
     *
     * @param site
     * @param serial
     * @return
     */
    AbstractContent getContent(Site site, String serial);

    /**
     * 查找所有符合要求的内容
     *
     * @param title    标题满足like,可选
     * @param site     所属站点,必选
     * @param category 所属数据源,可选
     * @param pageable 是否要分页搜索可选
     * @return 结果循环
     */
    @Transactional(readOnly = true)
    Iterable<AbstractContent> list(String title, Site site, Long category, Pageable pageable);

    /**
     * @param site
     * @return
     */
    @Transactional(readOnly = true)
    Iterable<AbstractContent> listBySite(Site site, Pageable pageable);

//    @Transactional(readOnly = true)
//    long countBySite(Site site);

    /**
     * 查找指定id的内容
     * 显然一个id代表不了一切,因为id会重复的,还需要指明具体的类型
     *
     * @param contentId 内容id
     * @param type      内容类型
     * @return 唯一内容, null if not existing
     */
    @Transactional(readOnly = true)
    AbstractContent findById(long contentId, ContentType type);

    /**
     * 查找指定id的内容
     * 显然一个id代表不了一切,因为id会重复的,还需要指明具体的类型
     *
     * @param contentId 内容id
     * @param clazz     内容类型
     * @return 唯一内容, null if not existing
     */
    @Transactional(readOnly = true)
    AbstractContent findById(long contentId, Class<? extends AbstractContent> clazz);

    /**
     * 彻底删除,包括资源
     *
     * @param content 内容
     * @throws IOException 清理资源时发生的IO异常,但不会影响事务
     */
    @Transactional
    void delete(AbstractContent content) throws IOException;

    /**
     * 根据不同的内容类型创建内容
     *
     * @param type 类型
     * @return 一个尚未进入持久曾的内容(JO)
     */
    AbstractContent newContent(ContentType type);

    /**
     * 初始化一个正文
     *
     * @param content 正文
     */
    void init(AbstractContent content);

    /**
     * 复制所有src的正文到dist
     *
     * @param src  源数据源
     * @param dist 目标数据源
     */
    @Transactional
    void copyTo(Category src, Category dist) throws IOException;

//    PageData<Contents> getPage(String title,Long siteId,Long category, int page, int pageSize);
}
