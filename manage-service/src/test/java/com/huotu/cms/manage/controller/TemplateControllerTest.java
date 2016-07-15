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
import com.huotu.cms.manage.page.CategoryPage;
import com.huotu.cms.manage.page.ManageMainPage;
import com.huotu.cms.manage.page.PageInfoPage;
import com.huotu.cms.manage.page.SitePage;
import com.huotu.cms.manage.page.TemplatePage;
import com.huotu.cms.manage.page.support.AbstractCRUDPage;
import com.huotu.cms.manage.util.ImageHelper;
import com.huotu.hotcms.service.entity.Template;
import com.huotu.hotcms.service.repository.TemplateRepository;
import me.jiangcai.lib.resource.service.ResourceService;
import org.junit.Test;
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
public class TemplateControllerTest extends ManageTest {

    @Autowired
    private TemplateRepository templateRepository;
    @Autowired
    private ResourceService resourceService;

    @Test
    @Transactional
    public void flow() throws Exception {
        TemplatePage page = loginAsManage().toPage(TemplatePage.class);

        CRUDHelper.flow(page, new CRUDTest<Template>() {
            Resource logoResource;

            @Override
            public Collection<Template> list() {
                return templateRepository.findAll();
            }

            @Override
            public Template randomValue() {
                logoResource = random.nextBoolean() ? null : new ClassPathResource("thumbnail.png");
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
                    ImageHelper.assertSame(resourceService.getResource(entity.getLogoUri()), logoResource);
                }
            }
        });

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

}