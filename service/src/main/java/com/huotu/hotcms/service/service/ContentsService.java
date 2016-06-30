/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.BaseEntity;
import org.springframework.data.domain.Pageable;

/**
 * Created by chendeyu on 2016/1/12.
 */
public interface ContentsService {

    /**
     * 查找所有符合要求的内容
     *
     * @param title    标题满足like,可选
     * @param siteId   所属站点,必选
     * @param category 所属数据源,可选
     * @param pageable 是否要分页搜索可选
     * @return 结果循环
     */
    Iterable<BaseEntity> list(String title, Long siteId, Long category, Pageable pageable);

//    PageData<Contents> getPage(String title,Long siteId,Long category, int page, int pageSize);
}
