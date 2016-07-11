/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller;

import com.huotu.cms.manage.ManageTest;
import com.huotu.cms.manage.controller.common.ResourceController;
import com.huotu.cms.manage.page.AdminPage;
import com.huotu.cms.manage.page.ManageMainPage;
import com.huotu.cms.manage.page.SitePage;
import com.huotu.hotcms.service.common.SiteType;
import com.huotu.hotcms.service.entity.Host;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.service.repository.HostRepository;
import com.huotu.hotcms.service.repository.SiteRepository;
import me.jiangcai.lib.resource.service.ResourceService;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author CJ
 */
public class SiteControllerTest extends ManageTest {


    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private HostRepository hostRepository;
    @Autowired
    private ResourceController resourceController;
    @Autowired
    private ResourceService resourceService;

    @Test
    @Transactional
    public void flow() throws Exception {
        Owner owner = randomOwner();
        loginAsManage();
        AdminPage page = initPage(AdminPage.class);
        ManageMainPage mainPage = page.toMainPage(owner);

        addSite(owner, mainPage);
        addSite(owner, mainPage);
        addSite(owner, mainPage);


        SitePage sitePage = mainPage.toSite();
        // 1 是确保站点的数量 2 是确保站点的信息 所以必须逐个检查。
        Set<Site> siteSet = siteRepository.findByOwner_IdAndDeleted(owner.getId(), false);
        List<WebElement> list = sitePage.list();
        assertThat(list)
                .hasSize(siteSet.size());

        for (Site site : siteSet) {
            WebElement siteElement = list.stream()
                    .filter(sitePage.findSiteElement(site))
                    .findAny().orElseThrow(() -> {
                        sitePage.printThisPage();
                        return new IllegalStateException("应该显示该站点" + site.getSiteId());
                    });
            assertThat(siteElement)
                    .is(sitePage.siteElementCondition(site));
        }

    }

    private void addSite(Owner owner, ManageMainPage mainPage) throws IOException, InterruptedException {
        int count = siteRepository.findByOwner_IdAndDeleted(owner.getId(), false).size();
        SitePage sitePage = mainPage.toSite();

        // do something.
        String logo = random.nextBoolean()
                ? resourceController.uploadTempResource(new ClassPathResource("thumbnail.png").getInputStream())
                : null;

        String name = UUID.randomUUID().toString();
        String title = UUID.randomUUID().toString();
        String desc = UUID.randomUUID().toString();
        String[] stringArrays = randomDomains();
        String[] keywords = randomArray(stringArrays, 1);
        String[] domains = randomArray(stringArrays, 1);
        SiteType siteType = SiteType.values()[random.nextInt(SiteType.values().length)];
        String copyright = UUID.randomUUID().toString();

        sitePage.addSite(name, title, desc, keywords, logo, siteType.getValue().toString(), copyright, domains,
                domains[0]);

        Set<Site> siteSet = siteRepository.findByOwner_IdAndDeleted(owner.getId(), false);
        assertThat(siteSet)
                .hasSize(count + 1);
        // 这个Site数据检查
        Site site = siteSet.stream()
                .sorted((site1, site2) -> site2.getCreateTime().compareTo(site1.getCreateTime()))
                .findFirst().orElseThrow(() -> new IllegalStateException("缺少必要数据"));

        assertThat(site.getName())
                .isEqualTo(name);
        assertThat(site.getCopyright())
                .isEqualTo(copyright);
        assertThat(site.getDescription())
                .isEqualTo(desc);
        assertThat(site.getSiteType())
                .isEqualTo(siteType);
        assertThat(site.getTitle())
                .isEqualTo(title);

        //检查每一个Host必须包含这个Site
        for (String domain : domains) {
            Host host = hostRepository.findByDomain(domain);
            assertThat(host.getSites())
                    .containsValues(site);
        }

        if (logo != null) {
            BufferedImage image = ImageIO.read(resourceService.getResource(site.getLogoUri()).getInputStream());
            BufferedImage image1 = ImageIO.read(new ClassPathResource("thumbnail.png").getInputStream());
            assertThat(image.getWidth())
                    .isEqualTo(image1.getWidth());
        }
    }


}