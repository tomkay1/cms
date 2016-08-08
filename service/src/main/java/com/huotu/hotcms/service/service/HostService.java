/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.Host;
import com.huotu.hotcms.service.entity.Site;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface HostService {

    /**
     * 根据输入域名获取主机
     *
     * @param domain 输入域名
     * @return 主机实体
     */
    @Transactional(readOnly = true)
    Host getHost(String domain);

    /**
     * <p>不要在管这个站点,让这个站点处于无主机可路由的状态</p>
     *
     * @param site 要处理掉的站点
     */
    @Transactional
    void stopHookSite(Site site);

    /**
     * @param site 相关站点
     * @return 可以解析到站点的主机
     */
    @Transactional(readOnly = true)
    Collection<Host> hookOn(Site site);

}
