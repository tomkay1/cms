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
import com.huotu.hotcms.service.entity.*;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.repository.MallProductCategoryRepository;
import com.huotu.hotcms.service.repository.OwnerRepository;
import com.huotu.hotcms.service.service.CategoryService;
import com.huotu.hotcms.service.service.GalleryService;
import com.huotu.hotcms.service.service.MallService;
import com.huotu.huobanplus.common.entity.Brand;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.function.BiConsumer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author CJ
 */
public class CategoryControllerTest extends SiteManageTest {

    @Autowired
    OwnerRepository ownerRepository;
    @Autowired
    MallService mallService;
    @Autowired
    MallProductCategoryRepository mallProductCategoryRepository;
    @Autowired
    GalleryService galleryService;
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
        site.getOwner().setCustomerId(null);
        CategoryPage page = mainPage.toPage(CategoryPage.class);

        page.assertMallDisabled();
        site.getOwner().setCustomerId(random.nextInt());
        page.refresh();
        page.assertMallEnable();
    }

    @Test
    @Transactional
    public void add() throws Exception {
        Site site = loginAsSite();
        if (site.getOwner().getCustomerId() == null) {
            site.getOwner().setCustomerId(3447);
            ownerRepository.save(site.getOwner());
        }
        ManageMainPage mainPage = initPage(ManageMainPage.class);
        CRUDHelper.flow(mainPage.toPage(CategoryPage.class), new NormalCategoryTest(site));
        CRUDHelper.flow(mainPage.toPage(CategoryPage.class), new MallProductCategoryTest(site));
        CRUDHelper.flow(mainPage.toPage(CategoryPage.class), new MallClassCategoryTest(site));

    }

    private Category randomCategoryValue(Site site) {
        Category category = new Category();
        category.setName(UUID.randomUUID().toString());
        category.setContentType(contentService.normalContentTypes()[random.nextInt(contentService.normalContentTypes().length)]);

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


    private class NormalCategoryTest implements CRUDTest<Category> {
        private final Site site;

        public NormalCategoryTest(Site site) {
            this.site = site;
        }

        @Override
        public boolean modify() {
            return true;
        }


        @Override
        public Collection<Category> list() {
            return categoryService.getCategories(site);
        }

        @Override
        public Category randomValue() throws IOException {
            return randomCategoryValue(site);
        }

        @Override
        public BiConsumer<AbstractCRUDPage<Category>, Category> customAddFunction() {
            return null;
        }

        @Override
        public BiConsumer<AbstractCRUDPage<Category>, Category> customUpdateFunction() {
            return null;
        }

        @Override
        public void assertCreation(Category entity, Category data) {
            assertThat(entity.getName())
                    .isEqualTo(data.getName());
            assertThat(entity.getParent())
                    .isEqualTo(data.getParent());
            assertThat(entity.getContentType())
                    .isEqualByComparingTo(data.getContentType());
            assertSupprot(entity, data);
        }

        public void assertSupprot(Category entity, Category data) {
            if (entity.getContentType() == ContentType.MallProduct) {
                if (entity instanceof MallProductCategory && data instanceof MallProductCategory) {
                    MallProductCategory mallProduct = (MallProductCategory) entity;
                    MallProductCategory dataProduct = (MallProductCategory) data;
                    assertThat(mallProduct.getGoodTitle())
                            .isEqualTo(dataProduct.getGoodTitle());
                    assertThat(mallProduct.getGallery())
                            .isEqualTo(dataProduct.getGallery());
                    assertThat(mallProduct.getSalesCount())
                            .isEqualTo(dataProduct.getSalesCount());
                    assertThat(mallProduct.getMinPrice())
                            .isEqualTo(dataProduct.getMinPrice());
                    assertThat(mallProduct.getMaxPrice())
                            .isEqualTo(dataProduct.getMaxPrice());

                    assertThat(mallProduct.getMallBrandId())
                            .isEqualTo(dataProduct.getMallBrandId());
                    assertThat(mallProduct.getMallCategoryId())
                            .isEqualTo(dataProduct.getMallCategoryId());
                }
            } else if (entity.getContentType() == ContentType.MallClass) {
                if (entity instanceof MallClassCategory && data instanceof MallClassCategory) {
                    MallClassCategory mallClass = (MallClassCategory) entity;
                    MallClassCategory dataClass = (MallClassCategory) data;
                    assertThat(mallClass.getRecommendCategory())
                            .isEqualTo(dataClass.getRecommendCategory());
                    if (dataClass.getCategories() != null)
                        assertThat(dataClass.getCategories().contains(mallClass.getCategories()));
                }
            }
        }

        @Override
        public void assertUpdate(Category entity, Category data) throws Exception {
            assertThat(entity.getName())
                    .isEqualTo(data.getName());
            assertSupprot(entity, data);
        }
    }

    private class MallProductCategoryTest extends NormalCategoryTest {
        public MallProductCategoryTest(Site site) {
            super(site);
        }

        @Override
        public Category randomValue() throws IOException {
            MallProductCategory category = new MallProductCategory();
            category.setName(UUID.randomUUID().toString());
            category.setSerial(UUID.randomUUID().toString().replace("-", ""));
            category.setContentType(ContentType.MallProduct);
            category.setSite(super.site);
            category.setGoodTitle(UUID.randomUUID().toString());
            category.setSalesCount(20);
            category.setMinPrice(10D);
            category.setMaxPrice(2000D);
            category.setParent(null);
            List<com.huotu.huobanplus.common.entity.Category> categorys = mallService.listCategories(
                    super.site.getOwner().getCustomerId());
            List<Brand> brands = mallService.listBrands(super.site.getOwner().getCustomerId());
            List<Long> categoryList = categorys == null || categorys.isEmpty() ? null : new ArrayList<>();
            List<Long> brandList = brands == null || brands.isEmpty() ? null : new ArrayList<>();
            for (com.huotu.huobanplus.common.entity.Category c : categorys) {
                categoryList.add(c.getId());
            }
            for (Brand b : brands) {
                brandList.add(b.getId());
            }
            List<Gallery> galleries = galleryService.listGalleries(super.site);
            if (!galleries.isEmpty()) {
                category.setGallery(galleries.get(0));
            }
            category.setMallBrandId(brandList);
            category.setMallCategoryId(categoryList);
            return category;
        }
    }

    private class MallClassCategoryTest extends NormalCategoryTest {
        public MallClassCategoryTest(Site site) {
            super(site);
        }


        @Override
        public Category randomValue() {
            MallClassCategory category = new MallClassCategory();
            category.setName(UUID.randomUUID().toString());
            category.setSerial(UUID.randomUUID().toString().replace("-", ""));
            category.setSite(super.site);
            category.setContentType(ContentType.MallClass);
            category.setParent(null);
            category.setCategories(mallProductCategoryRepository.findBySiteAndDeletedFalse(super.site));
            Iterator<Category> list = categoryService.getCategoriesForContentType(super.site, ContentType.Link).iterator();
            while (list.hasNext()) {
                category.setRecommendCategory(list.next());
                break;
            }
            return category;
        }
    }
}