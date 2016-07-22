/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.repository;

import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.widget.entity.PageInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by wenqi on 2016/7/5.
 */
//@Repository
public interface PageInfoRepository extends JpaRepository<PageInfo, Long>, JpaSpecificationExecutor<PageInfo> {

    /**
     * 查询相应站点下的PageInfo的列表
     *
     * @param site 站点
     * @return PageInfo的列表
     */
    List<PageInfo> findBySite(Site site);

    /**
     * 删除某站点下所有页面数据
     *
     * @param site 站点
     * @return 受影响行数
     */
    Long deleteBySite(Site site);

    /**
     * 通过站点和路劲 查询page信息
     *
     * @param site     站点
     * @param pagePath 路径,是唯一的
     * @return page信息
     */
    PageInfo findBySiteAndPagePath(Site site, String pagePath);

    /**
     * 通过数据源查询pageInfo列表
     *
     * @param category 数据源
     * @return pageInfo列表
     */
    List<PageInfo> findByCategory(Category category);

    /**
     * 通过path查询pageInfo列表
     *
     * @param pagePath path
     * @return pageInfov
     */
    PageInfo findByPagePath(String pagePath);


}
