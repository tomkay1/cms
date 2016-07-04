/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.AbstractContent;
import com.huotu.hotcms.service.entity.Site;
import org.springframework.data.domain.Pageable;

/**
 * Created by chendeyu on 2016/1/12.
 */
public interface ContentsService {

    /**
     * 查找所有符合要求的内容
     *
     * @param title    标题满足like,可选
     * @param site   所属站点,必选
     * @param category 所属数据源,可选
     * @param pageable 是否要分页搜索可选
     * @return 结果循环
     */
    Iterable<AbstractContent> list(String title, Site site, Long category, Pageable pageable);

    /**
     * 查找指定id的内容
     *
     * @param contentId
     * @return
     */
    AbstractContent findById(Long contentId);

//    PageData<Contents> getPage(String title,Long siteId,Long category, int page, int pageSize);
}
