/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.repository;

import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.widget.entity.PageInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by hzbc on 2016/7/4.
 */
public interface PageInfoRepository extends JpaRepository<PageInfo,String>,JpaSpecificationExecutor<PageInfo> {
    public List<PageInfo> findBySite(Site site);
}
