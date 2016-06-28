/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.repository;

import com.huotu.hotcms.service.entity.WidgetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WidgetTypeRepository extends JpaRepository<WidgetType, Long>, JpaSpecificationExecutor<WidgetType> {

    /**
     * <p>
     *     查找非指定范畴的控件主体类型列表
     * </p>
     * @param scopesType 控件主体范围类型
     * @return WidgetType列表对象
     * */
    @Query(value = "select * from cms_widgetType u where u.scenes<>?1",nativeQuery = true)
    List<WidgetType> findAllByScopesTypeNot(Integer scopesType);

}
