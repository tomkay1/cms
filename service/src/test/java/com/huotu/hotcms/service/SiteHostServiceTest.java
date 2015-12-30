/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service;

import com.huotu.hotcms.config.AdminTestConfig;
import com.huotu.hotcms.entity.Host;
import com.huotu.hotcms.entity.Site;
import com.huotu.hotcms.repository.HostRepository;
import com.huotu.hotcms.repository.SiteRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;

/**
 * Created by cwb on 2015/12/30.
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AdminTestConfig.class)
@WebAppConfiguration
@Transactional
public class SiteHostServiceTest {

    @Autowired
    private HostRepository hostRepository;

    @Autowired
    private SiteRepository siteRepository;

    /**
     * 测试多对多关系建立正确性
     *
     */
    @Test
    @Rollback
    public void addHostTest() {
        //新建站点新绑定域名，级联保存并建立关系
        Host host = new Host();
        host.setDomain("cms.51flashmall.com");
        host.setCustomerId(3447);
        Host host1 = new Host();
        host1.setDomain("localhost");
        host1.setCustomerId(3447);
        Site site = new Site();
        site.setCustomerId(3447);
        site.setTitle("火图科技");
        site.setDescription("杭州火图科技有限公司是一家专业的微信商城服务提供商，" +
                "专业/质优/高端微商城定制开发，为客户建立专属微信三级分销系统，并提供代运营、微商人才培训等服务");
        site.setCreateTime(LocalDateTime.now());
        site.addHost(host);
        site.addHost(host1);
        site = siteRepository.saveAndFlush(site);
        site = siteRepository.findOne(site.getSiteId());
        Assert.assertEquals(site.getHosts().size(),2);
        host = hostRepository.findByDomain("cms.51flashmall.com");
        host1 = hostRepository.findByDomain("localhost");
        Assert.assertEquals(1,host.getSites().size());
        Assert.assertEquals(1,host1.getSites().size());

        //新建站点绑定旧域名，建立关系
        Site s = new Site();
        s.setCustomerId(3447);
        s.setTitle("huobanplus");
        s.setDescription("Hangzhou fire science and Technology Co., Ltd. is a professional mall of micro channel service providers, " +
                "professional / quality / high-end micro mall custom development……");
        s.setCreateTime(LocalDateTime.now());
        s = siteRepository.saveAndFlush(s);
        s.addHost(hostRepository.findByDomain("cms.51flashmall.com"));
        s = siteRepository.saveAndFlush(s);
        s = siteRepository.findOne(s.getSiteId());
        Assert.assertEquals(s.getHosts().size(),1);

        //站点解除其中一个域名绑定关系
        site.removeHost(host);
        site = siteRepository.saveAndFlush(site);
        Assert.assertEquals(1,site.getHosts().size());
        host = hostRepository.findByDomain("cms.51flashmall.com");
        Assert.assertEquals(1,host.getSites().size());

        //域名解除其中一个站点
        host.removeSite(s);
        hostRepository.saveAndFlush(host);
        Assert.assertEquals(0,s.getHosts().size());

        //域名绑定一个站点
        host1.addSite(s);
        hostRepository.saveAndFlush(host1);
        Assert.assertEquals(1,s.getHosts().size());
        Assert.assertEquals("localhost",s.getHosts().iterator().next().getDomain());
    }

}
