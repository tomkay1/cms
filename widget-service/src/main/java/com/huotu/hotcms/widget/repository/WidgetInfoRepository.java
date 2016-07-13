/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.repository;

import com.huotu.hotcms.service.entity.WidgetInfo;
import com.huotu.hotcms.service.entity.support.WidgetIdentifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 持久化已安装的控件信息
 */
@Repository
public interface WidgetInfoRepository extends JpaRepository<WidgetInfo, WidgetIdentifier>
        , JpaSpecificationExecutor<WidgetInfo> {

    /**
     * @return 可用控件包
     */
    List<WidgetInfo> findByEnabledTrue();

    /**
     * 获取group,artifactId 相等的并且是可用的控件信息列表
     *
     * @param groupId
     * @param artifactId
     * @return
     */
    List<WidgetInfo> findByGroupIdAndArtifactIdAndEnabledTrue(String groupId, String artifactId);


}
