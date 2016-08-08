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
import com.huotu.cms.manage.controller.support.CRUDHelper;
import com.huotu.cms.manage.controller.support.CRUDTest;
import com.huotu.cms.manage.page.AdminPage;
import com.huotu.cms.manage.page.ManageMainPage;
import com.huotu.cms.manage.page.SitePage;
import com.huotu.cms.manage.page.support.AbstractCRUDPage;
import com.huotu.hotcms.service.entity.Host;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.service.repository.HostRepository;
import com.huotu.hotcms.service.repository.SiteRepository;
import com.huotu.hotcms.service.service.HostService;
import com.huotu.hotcms.service.util.ImageHelper;
import me.jiangcai.lib.resource.service.ResourceService;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private ResourceService resourceService;
    @Autowired
    private HostService hostService;

    @Test
    @Transactional
    public void flow() throws Exception {
        Owner owner = randomOwner();
        loginAsManage();
        AdminPage page = initPage(AdminPage.class);
        ManageMainPage mainPage = page.toMainPage(owner);

        addSite(owner, mainPage);
        addSite(owner, mainPage);
        addBadSite(owner, mainPage);


//        SitePage sitePage = mainPage.toSite();
//        // 1 是确保站点的数量 2 是确保站点的信息 所以必须逐个检查。
//        Set<Site> siteSet = siteRepository.findByOwner_IdAndDeleted(owner.getId(), false);
//        List<WebElement> list = sitePage.list();
//        assertThat(list)
//                .hasSize(siteSet.size());
//
//        for (Site site : siteSet) {
//            WebElement siteElement = list.stream()
//                    .filter(sitePage.findSiteElement(site))
//                    .findAny().orElseThrow(() -> {
//                        sitePage.printThisPage();
//                        return new IllegalStateException("应该显示该站点" + site.getSiteId());
//                    });
//            assertThat(siteElement)
//                    .is(sitePage.siteElementCondition(site));
//        }

    }

    private void addSite(Owner owner, ManageMainPage mainPage) throws Exception {
        CRUDHelper.flow(mainPage.toPage(SitePage.class), new SiteCRUDTest(owner));
    }

    private void addBadSite(Owner owner, ManageMainPage mainPage) throws Exception {
        CRUDHelper.flow(mainPage.toPage(SitePage.class), new SiteCRUDTest(owner) {
            @Override
            public Site randomValue() {
                Site site = super.randomValue();
                domains = new String[0];
                return site;
            }

            @Override
            public String errorMessageAfterAdd(Site data) {
                return "域名";
            }
        });
    }


    private class SiteCRUDTest implements CRUDTest<Site> {
        private final Owner owner;
        String[] domains;
        //            String logo;
        Resource logoResource;

        SiteCRUDTest(Owner owner) {
            this.owner = owner;
        }

        @Override
        public boolean open() {
            return true;
        }

        @Override
        public Predicate<? super PropertyDescriptor> editableProperty() throws Exception {
            return propertyDescriptor -> Arrays.asList("title", "description", "keywords", "copyright").contains(propertyDescriptor.getName());
        }

        @Override
        public boolean modify() {
            return true;
        }

        @Override
        public void assertResourcePage(AbstractCRUDPage<Site> page, Site entity) throws Exception {
            WebElement form = page.getForm();
            // 这里有2个tab
            page.assertInputText(form, "name", entity.getName());
            page.assertInputText(form, "title", entity.getTitle());
            page.assertInputTextarea(form, "description", entity.getDescription());
            page.assertInputText(form, "copyright", entity.getCopyright());
            if (entity.getKeywords() != null)
                page.assertInputTags(form, "keywords", Arrays.asList(entity.getKeywords().split(",")));
            else
                page.assertInputTags(form, "keywords", new ArrayList<>());

            final Stream<Host> hostStream = hostService.hookOn(entity).stream();
            page.assertInputTags(form, "domains", hostStream
                    .map(Host::getDomain)
                    .collect(Collectors.toList()));

            page.assertInputText(form, "homeDomain", entity.getRecommendDomain());
//            page.assertInputText(form, "description", entity.getDescription());
        }

        @Override
        public Collection<Site> list() {
            return siteRepository.findByOwner_IdAndDeleted(owner.getId(), false);
        }

        @Override
        public Site randomValue() {
            logoResource = random.nextBoolean() ? null : new ClassPathResource("thumbnail.png");
//                    logo = random.nextBoolean()
//                            ? resourceController.uploadTempResource(new ClassPathResource("thumbnail.png").getInputStream())
//                            : null;
            Site site = new Site();

            site.setName(UUID.randomUUID().toString());
            site.setTitle(UUID.randomUUID().toString());
            site.setDescription(UUID.randomUUID().toString());
            site.setCopyright(UUID.randomUUID().toString());
//                site.setSiteType(SiteType.values()[random.nextInt(SiteType.values().length)]);

            String[] stringArrays = randomDomains();
            String[] keywords = randomArray(stringArrays, 1);
            site.setKeywords(String.join(",", (CharSequence[]) keywords));
            domains = randomArray(stringArrays, 1);

            return site;
        }

        @Override
        public BiConsumer<AbstractCRUDPage<Site>, Site> customAddFunction() {
            return (page, site) -> {
                WebElement form = page.getForm();
                if (logoResource != null) {
                    try {
                        uploadResource(page, "tmpLogoPath", logoResource);
                    } catch (Exception ignored) {
                    }
                }
//                    if (logo != null)
//                        page.inputHidden(form, "tmpLogoPath", logo);
                page.inputTags(form, "domains", domains);
                if (domains.length > 0)
                    page.inputText(form, "homeDomain", domains[0]);
                else
                    page.inputText(form, "homeDomain", randomDomain());
                System.out.println("done");
            };
        }

        @Override
        public void assertCreation(Site entity, Site data) {
            assertThat(entity.getName())
                    .isEqualTo(data.getName());
            assertThat(entity.getTitle())
                    .isEqualTo(data.getTitle());
            assertThat(entity.getDescription())
                    .isEqualTo(data.getDescription());
            assertThat(entity.getCopyright())
                    .isEqualTo(data.getCopyright());
            assertThat(entity.getSiteType())
                    .isEqualTo(data.getSiteType());

            if (logoResource != null) {
                ImageHelper.assertSame(resourceService.getResource(entity.getLogoUri()), logoResource);
            }
//                if (logo != null) {
//                    try {
//                        BufferedImage image = ImageIO.read(resourceService.getResource(entity.getLogoUri()).getInputStream());
//                        BufferedImage image1 = ImageIO.read(new ClassPathResource("thumbnail.png").getInputStream());
//                        assertThat(image.getWidth())
//                                .isEqualTo(image1.getWidth());
//                    } catch (IOException ex) {
//                        throw new RuntimeException(ex);
//                    }
//                }

            for (String domain : domains) {
                Host host = hostRepository.findByDomain(domain);
                assertThat(host.getSites())
                        .containsValues(entity);
            }
            for (Host host : hostService.hookOn(entity)) {
                assertThat(host.getDomain()).isIn((Object[]) domains);
            }

        }
    }
}