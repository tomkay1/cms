/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.entity.Host;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.repository.HostRepository;
import com.huotu.hotcms.service.repository.SiteRepository;
import com.huotu.hotcms.service.service.HostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by cwb on 2015/12/24.
 */
@Service
public class HostServiceImpl implements HostService {

    @Autowired
    private HostRepository hostRepository;

    @Autowired
    private SiteRepository siteRepository;

    @Override
    public Host getHost(String domain) {
        return hostRepository.findByDomain(domain);
    }

    /**
     * <p>解除这个站点所有的关联</p>
     *
     * @param site 要移除的Host列表
     */
    @Override
    public void stopHookSite(Site site) {
        for (Host host : hookOn(site)) {
            host.removeSite(site);
            hostRepository.saveAndFlush(host);
        }
        assert hookOn(site).isEmpty();
    }

    @Override
    public Collection<Host> hookOn(Site site) {
        return hostRepository.findBySites(site);
    }


}
