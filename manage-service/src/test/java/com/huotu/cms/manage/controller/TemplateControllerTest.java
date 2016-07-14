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
import com.huotu.cms.manage.page.TemplatePage;
import com.huotu.cms.manage.page.support.AbstractCRUDPage;
import com.huotu.hotcms.service.entity.Template;
import com.huotu.hotcms.service.repository.TemplateRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.UUID;
import java.util.function.BiConsumer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author CJ
 */
public class TemplateControllerTest extends ManageTest {

    @Autowired
    private TemplateRepository templateRepository;

    @Test
    @Transactional
    public void flow() throws Exception {
        TemplatePage page = loginAsManage().toPage(TemplatePage.class);

        CRUDHelper.flow(page, new CRUDTest<Template>() {
            @Override
            public Collection<Template> list() {
                return templateRepository.findAll();
            }

            @Override
            public Template randomValue() {
                Template template = new Template();
                template.setName(UUID.randomUUID().toString());
                return template;
            }

            @Override
            public BiConsumer<AbstractCRUDPage<Template>, Template> customAddFunction() {
                return null;
            }

            @Override
            public void assertCreation(Template entity, Template data) {
                assertThat(entity.getCreateTime()).isNotNull();
                assertThat(entity.getName())
                        .isEqualTo(data.getName());
            }
        });

    }

}