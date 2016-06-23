/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.repository;

import com.huotu.hotcms.widget.entity.WidgetInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by elvis on 2016/6/7.
 */
@Repository
public interface WidgetRepository extends JpaRepository<WidgetInfo, Long>,JpaSpecificationExecutor {

//    List<WidgetInfo> findByAuthor(String author);

    WidgetInfo findByWidgetIdAndVersion(String widgetId,String version);

    WidgetInfo findByWidgetIdAndGroupIdAndVersion(String widgetId, String groupId, String version);
}
