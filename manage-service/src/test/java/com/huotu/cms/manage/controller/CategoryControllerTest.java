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
import com.huotu.cms.manage.page.CategoryPage;
import com.huotu.cms.manage.page.ManageMainPage;
import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.service.CategoryService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

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
    public void index() throws Exception {
        Site site = loginAsOwnerReturnSite();

        ManageMainPage mainPage = initPage(ManageMainPage.class);

        CategoryPage page;
        try {
            page = mainPage.toCategory();
            throw new AssertionError("现在应该还看不到页面");
        } catch (Exception ignored) {
        }
        // 试下使用{{}}
        mainPage.switchSite(site);
        page = mainPage.toCategory();
    }

    @Test
    public void add() throws Exception {
        Site site = loginAsSite();

        addCategory(site);

        List<Category> categoryList = categoryService.getCategories(site);
        assertThat(categoryList)
                .isNotEmpty();
    }

    private CategoryPage addCategory(Site site) {
        Category category = randomCategoryValue(site);
        CategoryPage page = initPage(ManageMainPage.class).toCategory();
        if (category.getParent() != null)
            page.refresh();// 如果酱紫的话 需要可以选择父级
        page.addCategory(category);
        page.reloadPageInfo();
        return page;
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