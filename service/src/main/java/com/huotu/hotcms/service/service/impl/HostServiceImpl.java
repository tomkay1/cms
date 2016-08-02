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
import java.util.Set;

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

    @Override
    public Boolean save(Host host) {
        hostRepository.save(host);
        return true;
    }

    @Override
    /**
     * <p>
     *     获得需要移除的Host列表
     * </p>
     * @param domains 新的域名列表
     * @param site 目标站点信息
     * @return
     * */
    public Set<Host> getRemoveHost(String[] domains, Site site) {
        throw new IllegalStateException("看不懂这是做什么");
//        Set<Host> hosts = new HashSet<>();
//        Set<Host> hostSet = site.getHosts();
//        for (Host host : hostSet) {
//            boolean isExists = false;
//            for (String domain : domains) {
//                if (domain.equals(host.getDomain())) {
//                    isExists = true;
//                }
//            }
//            if (!isExists) {
//                if (host.getSites() != null) {
//                    if (host.getSites().size() == 0) {
//                        hosts.add(host);
//                    } else if (host.getSites().size() == 1) {
//                        if (host.getSites().contains(site)) {
//                            hosts.add(host);
//                        }
//                    }
//                }
//            }
//        }
//        return hosts;
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


    // TODO  home的设计存在比较大的问题,home应该只是存在一个关联中
    private Host getHomeHost(Site site) {
        for (Host host : hookOn(site)) {
            if (host.isHome()) {
                return host;
            }
        }
        return null;
    }

    @Override
    public String getHomeDomain(Site site) {
        Host host = getHomeHost(site);
        if (host != null) {
            return host.getDomain();
        }
        return null;
    }
}
