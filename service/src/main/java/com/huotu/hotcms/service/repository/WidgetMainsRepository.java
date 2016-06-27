/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.repository;

import com.huotu.hotcms.service.entity.WidgetMains;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WidgetMainsRepository extends JpaRepository<WidgetMains, Long>, JpaSpecificationExecutor<WidgetMains> {
    /**
     * <p>
     *     根据控件主体类型ID来查询控件主体列表
     * </p>
     * @param id  控件主体类型ID
     * @return WidgetMains列表
     * */
    List<WidgetMains> findWidgetMainsByWidgetTypeId(Long id);
}
