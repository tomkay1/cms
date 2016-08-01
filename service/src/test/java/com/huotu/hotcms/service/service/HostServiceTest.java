/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.TestBase;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.service.repository.OwnerRepository;
import com.huotu.hotcms.service.repository.SiteRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Condition;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author CJ
 */
public class HostServiceTest extends TestBase {

    @Autowired
    private HostService hostService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private OwnerRepository ownerRepository;

    @Test
    @Transactional
    public void hookOn() throws Exception {
        Owner owner = new Owner();
        owner.setEnabled(true);
        owner = ownerRepository.saveAndFlush(owner);
        Site site = new Site();
        site.setEnabled(true);
        site.setOwner(owner);
        site = siteRepository.saveAndFlush(site);
        assertThat(hostService.hookOn(site))
                .isEmpty();
        String domain = RandomStringUtils.randomAlphabetic(10 + random.nextInt(4)) + ".com";

        siteService.newSite(new String[]{domain}, domain, site, Locale.CHINA);

        assertThat(hostService.hookOn(site))
                .hasSize(1)
                .haveExactly(1, new Condition<>(host -> host.getDomain().equals(domain), "必须是这个domain"));

        //换一个
        String anotherDomain = RandomStringUtils.randomAlphabetic(10 + random.nextInt(4)) + ".com";
        siteService.patchSite(new String[]{anotherDomain}, anotherDomain, site, Locale.CHINA);

        assertThat(hostService.hookOn(site))
                .hasSize(1)
                .haveExactly(1, new Condition<>(host -> host.getDomain().equals(anotherDomain), "必须是这个domain"));
        //再加一个
        String newDomain = RandomStringUtils.randomAlphabetic(10 + random.nextInt(4)) + ".com";

        siteService.patchSite(new String[]{anotherDomain, newDomain}, anotherDomain, site, Locale.CHINA);

        assertThat(hostService.hookOn(site))
                .hasSize(2)
                .haveExactly(1, new Condition<>(host -> host.getDomain().equals(anotherDomain), "必须是这个domain"))
                .haveExactly(1, new Condition<>(host -> host.getDomain().equals(newDomain), "必须是这个domain"));
    }

}