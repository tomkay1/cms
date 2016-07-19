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
import com.huotu.cms.manage.page.support.AbstractCRUDPage;
import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.service.CategoryService;
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
public class CategoryControllerTest extends SiteManageTest {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @Transactional
    public void index() throws Exception {
        Site site = loginAsOwnerReturnSite();

        ManageMainPage mainPage = initPage(ManageMainPage.class);

        try {
            mainPage.toPage(CategoryPage.class);
            throw new AssertionError("现在应该还看不到页面");
        } catch (Exception ignored) {
            mainPage.closeDanger();
        }
        // 试下使用{{}}
        mainPage.switchSite(site);
        mainPage.toPage(CategoryPage.class);
    }

    @Test
    @Transactional
    public void add() throws Exception {
        Site site = loginAsSite();
        ManageMainPage mainPage = initPage(ManageMainPage.class);


        CRUDHelper.flow(mainPage.toPage(CategoryPage.class), new CRUDTest<Category>() {
            @Override
            public Collection<Category> list() {
                return categoryService.getCategories(site);
            }

            @Override
            public Category randomValue() {
                return randomCategoryValue(site);
            }

            @Override
            public BiConsumer<AbstractCRUDPage<Category>, Category> customAddFunction() {
                return null;
            }

            @Override
            public void assertCreation(Category entity, Category data) {
                assertThat(entity.getParent())
                        .isEqualTo(data.getParent());
                assertThat(entity.getContentType())
                        .isEqualByComparingTo(data.getContentType());
                assertThat(entity.getName())
                        .isEqualTo(data.getName());
            }
        });

    }

    private Category randomCategoryValue(Site site) {
        Category category = new Category();
        category.setName(UUID.randomUUID().toString());
        category.setContentType(ContentType.values()[random.nextInt(ContentType.values().length)]);

        if (random.nextBoolean()) {
            Category parent = new Category();
            parent.setContentType(category.getContentType());
            parent.setSite(site);
            parent.setName(UUID.randomUUID().toString());
            parent = categoryRepository.saveAndFlush(parent);
            category.setParent(parent);
        }

        return category;
    }


}