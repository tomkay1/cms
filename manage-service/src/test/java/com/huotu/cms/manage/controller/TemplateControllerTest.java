/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller;

import com.huotu.cms.manage.SiteManageTest;
import com.huotu.cms.manage.controller.support.CRUDHelper;
import com.huotu.cms.manage.controller.support.CRUDTest;
import com.huotu.cms.manage.page.CategoryPage;
import com.huotu.cms.manage.page.ManageMainPage;
import com.huotu.cms.manage.page.PageInfoPage;
import com.huotu.cms.manage.page.SitePage;
import com.huotu.cms.manage.page.TemplatePage;
import com.huotu.cms.manage.page.support.AbstractCRUDPage;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.Template;
import com.huotu.hotcms.service.repository.TemplateRepository;
import com.huotu.hotcms.service.service.TemplateService;
import com.huotu.hotcms.service.util.ImageHelper;
import com.huotu.hotcms.widget.entity.PageInfo;
import me.jiangcai.lib.resource.service.ResourceService;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;

import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author CJ
 */
public class TemplateControllerTest extends SiteManageTest {

    @Autowired
    private TemplateRepository templateRepository;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private TemplateService templateService;

    @Test
    @Transactional
    public void flow() throws Exception {
        TemplatePage page = loginAsManage().toPage(TemplatePage.class);

        CRUDHelper.flow(page, new TemplateCRUDTest(true));
        CRUDHelper.flow(page, new TemplateCRUDTest(false));

        //找一个条记录 并且打开编辑
        page.refresh();

        page = page.openResource(page.listTableRows().stream()
                .findFirst().orElseThrow(IllegalStateException::new));

        ManageMainPage forTemplatePage = page.manageTemplate();

        try {
//            forTemplatePage.printThisPage();
            forTemplatePage.toPage(SitePage.class);
            forTemplatePage.printThisPage();
            throw new AssertionError("应该是看不到站点管理的");
        } catch (Exception ignored) {
            //yes
        }
        forTemplatePage.toPage(PageInfoPage.class);
        forTemplatePage.toPage(CategoryPage.class);
    }

    /**
     * 在界面点赞
     *
     * @throws Exception
     */
    @Test
    @Transactional
    public void testLaud() throws Exception {

        workingWithTemplate(new DoForTemplate() {
            @Override
            public void templateWork(SitePage currentPage, Template template, WebElement templateRow, Site yourSite) throws Exception {
                //找一个点下赞
                int old = template.getLauds();


                currentPage.laud(templateRow);

                assertThat(template.getLauds())
                        .isGreaterThan(old);

                assertThat(templateService.isLauded(template.getSiteId(), yourSite.getOwner().getId()))
                        .isTrue();

                //再点一下
                currentPage.laud(templateRow);

                assertThat(template.getLauds())
                        .isEqualTo(old);

                assertThat(templateService.isLauded(template.getSiteId(), yourSite.getOwner().getId()))
                        .isFalse();
            }
        });
    }

    @Test
    @Transactional
    public void testPreview() throws Exception {
        workingWithTemplate(new DoForTemplate() {
            @Override
            public void templateWork(SitePage currentPage, Template template, WebElement templateRow, Site yourSite)
                    throws Exception {
                // 添加一个首页吧
                PageInfo pageInfo = randomPageInfo(template);
                pageInfo.setPagePath("");
                pageInfo.setTitle(randomEmailAddress());
                // 点下预览
                int old = template.getScans();

                currentPage.preview(templateRow, pageInfo.getTitle());
                assertThat(template.getScans())
                        .isGreaterThan(old);

                old = template.getScans();
                currentPage.preview(templateRow, pageInfo.getTitle());
                assertThat(template.getScans())
                        .isGreaterThan(old);

            }
        });
//        Template template = randomTemplate();
//        Owner owner = randomOwner();
//        Site site = randomSiteAndData(owner);
//        loginAsOwner(owner);
//        String mode = String.valueOf(random.nextInt(2));//随机0或者1,0代表追加模式，1代表替换模式
//        mockMvc.perform(post("/manage/template/use/{templateSiteID}/{customerSiteId}", template.getSiteId(), site.getSiteId())
//                .param("mode", mode)
//                .session(session))
//                .andExpect(status().isOk())
//                .andReturn();

    }


    private void workingWithTemplate(DoForTemplate action) throws Exception {
        TemplatePage page = loginAsManage().toPage(TemplatePage.class);

        CRUDHelper.flow(page, new TemplateCRUDTest());

        Site site = loginAsOwnerReturnSite();

        SitePage sitePage = initPage(ManageMainPage.class).toPage(SitePage.class);

        WebElement editButton = sitePage.listTableRows()
                .stream()
                // 找到site这个站点
                .filter(sitePage.findRow(site))
                .findAny()
                .orElseThrow(IllegalStateException::new)
                // 找到一个叫做编辑的按钮
                .findElements(By.tagName("button"))
                .stream()
                .filter((button) -> button.isDisplayed() && button.getText().contains("编辑"))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("找不到编辑按钮"));
//找到一个编辑的按钮按下去
        editButton.click();
        SitePage oneSitePage = initPage(SitePage.class);

        // 我们总得找一个模板做实验嘛
        Template template = templateRepository
                .findByEnabledTrue()
                .stream()
                .findAny()
                .orElseThrow(IllegalStateException::new);


        WebElement templateRow = oneSitePage.listTemplateRows()
                .stream()
                .filter(oneSitePage.findRow(template))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("找不到预设的模板"));

        action.templateWork(oneSitePage, template, templateRow, site);
    }

    interface DoForTemplate {

        void templateWork(SitePage currentPage, Template template, WebElement templateRow, Site yourSite) throws Exception;
    }

    private class TemplateCRUDTest implements CRUDTest<Template> {
        private final boolean resource;
        Resource logoResource;

        TemplateCRUDTest(boolean resource) {
            this.resource = resource;
        }

        public TemplateCRUDTest() {
            this(random.nextBoolean());
        }

        @Override
        public Collection<Template> list() {
            return templateRepository.findAll();
        }

        @Override
        public Template randomValue() {
            logoResource = !resource ? null : new ClassPathResource("thumbnail.png");
            Template template = new Template();
            template.setName(UUID.randomUUID().toString());
            return template;
        }

        @Override
        public BiConsumer<AbstractCRUDPage<Template>, Template> customAddFunction() throws Exception {
            return (page, template) -> {
                if (logoResource != null)
                    try {
                        uploadResource(page, "extra", logoResource);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
            };
        }

        @Override
        public boolean modify() {
            return true;
        }

        @Override
        public Predicate<? super PropertyDescriptor> editableProperty() throws Exception {
            return (p) -> false;
        }

        @Override
        public void assertCreation(Template entity, Template data) {
            assertThat(entity.getCreateTime()).isNotNull();
            assertThat(entity.getName())
                    .isEqualTo(data.getName());
            if (logoResource != null) {
                assertThat(entity.getLogoUri())
                        .as("缩略图需存在")
                        .isNotNull();
                ImageHelper.assertSame(resourceService.getResource(entity.getLogoUri()), logoResource);
            }
        }
    }
}