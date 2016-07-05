/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.repository;

import com.huotu.hotcms.service.entity.PageInfo;
import com.huotu.hotcms.service.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lhx on 2016/7/5.
 */
@Repository
public interface PageInfoRepository extends JpaRepository<PageInfo, Long>, JpaSpecificationExecutor<PageInfo> {

    /**
     * 查询相应站点下的PageInfo的列表
     * @param site 站点
     * @return PageInfo的列表
     */
    List<PageInfo> findByCategory_Site(Site site);

    /**
     * 通过站点和路劲 查询page信息
     * @param site 站点
     * @param pagePath 路径
     * @return page信息
     */
    PageInfo findByCategory_SiteAndSite_PagePath(Site site, String pagePath);
}
