/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.repository;

import com.huotu.hotcms.service.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by cwb on 2015/12/24.
 */
public interface RegionRepository extends JpaRepository<Region,Long>,JpaSpecificationExecutor {

    Region findByRegionCodeIgnoreCase(String regionCode);

    Region findByLangCodeAndRegionCodeIgnoreCase(String langCode,String regionCode);

}
