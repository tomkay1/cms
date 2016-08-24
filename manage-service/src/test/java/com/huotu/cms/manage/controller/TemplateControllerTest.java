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
import com.huotu.hotcms.service.entity.AbstractContent;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.Template;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.repository.ContentRepository;
import com.huotu.hotcms.service.repository.TemplateRepository;
import com.huotu.hotcms.service.service.CategoryService;
import com.huotu.hotcms.service.service.ContentService;
import com.huotu.hotcms.service.service.SiteService;
import com.huotu.hotcms.service.service.TemplateService;
import com.huotu.hotcms.service.util.ImageHelper;
import com.huotu.hotcms.widget.entity.PageInfo;
import com.huotu.hotcms.widget.repository.PageInfoRepository;
import me.jiangcai.lib.resource.service.ResourceService;
import org.assertj.core.util.Arrays;
import org.assertj.core.util.IterableUtil;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;

import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.List;
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
    @Autowired
    private PageInfoRepository pageInfoRepository;
    @Autowired
    private ContentRepository contentRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ContentService contentService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SiteService siteService;

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

        workingWithTemplate((currentPage, template, templateRow, yourSite) -> {
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
        });
    }

    @Test
    @Transactional
    public void testUse() throws Exception {
        workingWithTemplate(new SiteUseTemplate(true));
        workingWithTemplate(new SiteUseTemplate(false));
    }

    @Test
    @Transactional
    public void testPreview() throws Exception {
        workingWithTemplate((currentPage, template, templateRow, yourSite) -> {
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

    private class SiteUseTemplate implements DoForTemplate {
        private final boolean append;

        /**
         * @param append 追加模式
         */
        SiteUseTemplate(boolean append) {
            this.append = append;
        }

        @Override
        public void templateWork(SitePage currentPage, Template template, WebElement templateRow, Site yourSite)
                throws Exception {
            // 包括正文 数据源 和 页面
            randomSiteData(template);
            randomSiteData(yourSite);
            // 点下预览
            int old = template.getUseNumber();

            // 页面当时的数据
            List<PageInfo> sitePages = pageInfoRepository.findBySite(yourSite);
            Iterable<AbstractContent> siteContents = contentService.listBySite(yourSite, null);
            List<Category> siteCategories = categoryRepository.findBySite(yourSite);

            // 模板当时的数据
            List<PageInfo> templatePages = pageInfoRepository.findBySite(template);
            Iterable<AbstractContent> templateContents = contentService.listBySite(template, null);
            List<Category> templateCategories = categoryRepository.findBySite(template);

            // 使用
            currentPage.use(templateRow, append);

            assertThat(template.getUseNumber())
                    .isGreaterThan(old);
            // 检查资源是否符合需求
            if (append) {
                //原资源还存在F
                assertThat(pageInfoRepository.findBySite(yourSite)).containsAll(sitePages);
                assertThat(contentService.listBySite(yourSite, null)).containsAll(siteContents);
                assertThat(categoryRepository.findBySite(yourSite)).containsAll(siteCategories);
            } else {
                //原资源已经没了
                sitePages.stream().forEach(page -> assertThat(pageInfoRepository.findOne(page.getId())).isNull());
                siteContents.forEach(page -> assertThat(contentService.findById(page.getId(), page.getClass())).isNull());
                siteCategories.stream().forEach(page -> assertThat(categoryRepository.findOne(page.getId())).isNull());
            }

            // 模板资源都还在而且还可用
            Runnable templateResourceChecker = () -> {
                assertThat(pageInfoRepository.findBySite(template)).containsOnlyElementsOf(templatePages);
                assertThat(contentService.listBySite(template, null)).containsOnlyElementsOf(templateContents);
                assertThat(categoryRepository.findBySite(template)).containsOnlyElementsOf(templateCategories);

                templatePages.forEach((obj) -> assertResourcesExisting(obj, false));
                templateContents.forEach((obj) -> assertResourcesExisting(obj, false));
                templateCategories.forEach((obj) -> assertResourcesExisting(obj, false));
            };

            templateResourceChecker.run();

            //复制过来的效果，遇到重名的可能改名字了 所以名字使用contain即可

            for (Category templateCategory : templateCategories) {
                Category siteCategory = categoryRepository.findBySerialAndSite(templateCategory.getSerial(), yourSite);
                String append = "";
                while (siteCategories.contains(siteCategory)) {
                    append += TemplateService.DuplicateAppend;
                    siteCategory = categoryRepository.findBySerialAndSite(templateCategory.getSerial() + append, yourSite);
                }
                // 资源可用 无
                // 数据唯一
                assertThat(siteCategory)
                        .isNotIn(siteCategories)
                        .isNotIn(templateCategories);
                // 数据一致
                assertThat(siteCategory.getName())
                        .contains(templateCategory.getName());
                assertThat(siteCategory.getContentType())
                        .isEqualByComparingTo(templateCategory.getContentType());
                // 数据关联
                if (templateCategory.getParent() == null) {
                    assertThat(siteCategory.getParent())
                            .isNull();
                } else {
                    assertThat(siteCategory.getParent())
                            .isNotNull()
                            .isNotIn(siteCategories)
                            .isNotIn(templateCategories);
                    assertThat(siteCategory.getParent().getName())
                            .contains(templateCategory.getParent().getName());
                    assertThat(siteCategory.getParent().getContentType())
                            .isEqualByComparingTo(templateCategory.getParent().getContentType());
                }
            }
            for (AbstractContent content : templateContents) {
                AbstractContent siteContent = contentService.getContent(yourSite, content.getSerial());
                String append = "";
                while (Arrays.nonNullElementsIn(IterableUtil.toArray(siteContents)).contains(siteContent)) {
                    append += TemplateService.DuplicateAppend;
                    siteContent = contentService.getContent(yourSite, content.getSerial() + append);
                }
                // 资源可用
                assertResourcesExisting(siteContent, false);

                // 数据唯一
                assertThat(siteContent.getCategory())
                        .isNotNull()
                        .isNotIn(siteCategories)
                        .isNotIn(templateCategories);
                // 数据一致
                assertThat(siteContent.getCategory().getSite())
                        .isEqualTo(yourSite);
                assertThat(siteContent.getTitle())
                        .isEqualTo(content.getTitle());
                assertThat(siteContent.getDescription())
                        .isEqualTo(content.getDescription());
                // 数据关联
            }
            for (PageInfo templatePage : templatePages) {
                PageInfo sitePage = pageInfoRepository.findBySiteAndPagePath(yourSite, templatePage.getPagePath());
                //有可能是原来的
                String append = "";
                while (sitePages.contains(sitePage)) {
                    append += TemplateService.DuplicateAppend;
                    sitePage = pageInfoRepository.findBySiteAndPagePath(yourSite, templatePage.getPagePath() + append);
                }
                // 资源可用
                assertResourcesExisting(sitePage, true);
                // 数据唯一
                assertThat(sitePage)
                        .isNotIn(sitePages)
                        .isNotIn(templatePages);
                // 数据一致
                assertThat(sitePage.getTitle())
                        .isEqualTo(templatePage.getTitle());
                // 数据关联
                if (templatePage.getCategory() != null) {
                    assertThat(sitePage.getCategory())
                            .isNotNull();
                    assertThat(sitePage.getCategory().getName())
                            .contains(templatePage.getCategory().getName());
                }
            }

            siteService.deleteData(yourSite);

            templateResourceChecker.run();
        }
    }

}