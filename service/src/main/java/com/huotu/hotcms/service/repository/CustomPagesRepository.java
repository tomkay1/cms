/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.repository;

import com.huotu.hotcms.service.entity.CustomPages;
import com.huotu.hotcms.service.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomPagesRepository extends JpaRepository<CustomPages, Long>, JpaSpecificationExecutor<CustomPages> {
    List<CustomPages> findByHomeAndSite(Boolean home,Site site);

    @Query(value = "update CustomPages set home=false where site=?1")
    void updateCustomerPageHome(Site site);

    CustomPages findBySiteAndSerial(Site site,String serial);

    /**
     * 拿到站点 下所有的界面
     * @param site 站点{@link com.huotu.hotcms.service.entity.Site}
     * @return 自定义界面 {@link com.huotu.hotcms.service.entity.CustomPages}列表 {@link java.util.List}
     */
    List<CustomPages> findBySite(Site site);
}
