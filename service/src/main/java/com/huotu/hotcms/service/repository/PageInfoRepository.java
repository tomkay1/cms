/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.repository;

import com.huotu.hotcms.service.entity.PageInfo;
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
    List<PageInfo> findBySiteId(long siteId);

    @Query("select p from PageInfo p  where  p.pagePath = ?1 and p.category.site.siteId = ?2")
    PageInfo findByPagePath(String pagePath, Long siteId);
}
