/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.config.ServiceTestConfig;
import com.huotu.hotcms.service.entity.Host;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.service.repository.HostRepository;
import com.huotu.hotcms.service.repository.OwnerRepository;
import com.huotu.hotcms.service.repository.SiteRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created by cwb on 2015/12/30.
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ServiceTestConfig.class)
@WebAppConfiguration
@Transactional
public class SiteHostServiceTest {

    private Host host1;
    private Host host2;
    private String domain1;
    private String domain2;

    @Autowired
    private HostRepository hostRepository;
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private OwnerRepository ownerRepository;
    private Owner owner;

    @Before
    public void setUp() {
        owner = new Owner();
        owner.setCustomerId(3447);
        owner = ownerRepository.save(owner);
        host1 = new Host();
        domain1 = UUID.randomUUID().toString();
        while (hostRepository.findByDomain(domain1)!=null) {
            domain1 = UUID.randomUUID().toString();
        }
        host1.setDomain(domain1);
        host1.setOwner(owner);
        host2 = new Host();
        domain2 = UUID.randomUUID().toString();
        while (hostRepository.findByDomain(domain2)!=null) {
            domain2 = UUID.randomUUID().toString();
        }
        host2.setDomain(domain2);
        host2.setOwner(owner);
    }

    /**
     * 测试多对多关系建立正确性
     *
     */
    @Test
    @Rollback
    public void addHostTest() {
        //新建站点新绑定域名，级联保存并建立关系
        Site site = new Site();
        site.setOwner(owner);
        site.setTitle("火图科技");
        site.setDescription("杭州火图科技有限公司是一家专业的微信商城服务提供商，" +
                "专业/质优/高端微商城定制开发，为客户建立专属微信三级分销系统，并提供代运营、微商人才培训等服务");
        site.setCreateTime(LocalDateTime.now());
        site.addHost(host1);
        site.addHost(host2);
        site = siteRepository.saveAndFlush(site);
        site = siteRepository.findOne(site.getSiteId());
        Assert.assertEquals(2,site.getHosts().size());
        host1 = hostRepository.findByDomain(domain1);
        host2 = hostRepository.findByDomain(domain2);
        Assert.assertEquals(1,host1.getSites().size());
        Assert.assertEquals(1, host2.getSites().size());

        //新建站点绑定旧域名，建立关系
        Site s = new Site();
        s.setOwner(owner);
        s.setTitle("huobanplus");
        s.setDescription("Hangzhou fire science and Technology Co., Ltd. is a professional mall of micro channel service providers, " +
                "professional / quality / high-end micro mall custom development……");
        s.setCreateTime(LocalDateTime.now());
        s = siteRepository.saveAndFlush(s);
        s.addHost(hostRepository.findByDomain(domain1));
        s = siteRepository.saveAndFlush(s);
        s = siteRepository.findOne(s.getSiteId());
        Assert.assertEquals(s.getHosts().size(),1);

        //站点解除其中一个域名绑定关系
        site.removeHost(host1);
        site = siteRepository.saveAndFlush(site);
        Assert.assertEquals(1,site.getHosts().size());
        host1 = hostRepository.findByDomain(domain1);
        Assert.assertEquals(1,host1.getSites().size());

        //域名解除其中一个站点
        host1.removeSite(s);
        hostRepository.saveAndFlush(host1);
        Assert.assertEquals(0,s.getHosts().size());

        //域名绑定一个站点
        host2.addSite(s);
        hostRepository.saveAndFlush(host2);
        Assert.assertEquals(1,s.getHosts().size());
        Assert.assertEquals(domain2, s.getHosts().iterator().next().getDomain());
    }

}
