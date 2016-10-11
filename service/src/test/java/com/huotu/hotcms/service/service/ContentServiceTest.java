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
import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.Video;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.repository.ContentRepository;
import com.huotu.hotcms.service.repository.MallProductCategoryRepository;
import com.huotu.hotcms.service.repository.SiteRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author CJ
 */
@Transactional
public class ContentServiceTest extends TestBase {

    @Autowired
    private ContentService contentService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private ContentRepository contentRepository;
    @Autowired
    private MallProductCategoryRepository mallProductCategoryRepository;

    @Test
    public void list() throws Exception {
        Site site = new Site();
        site = siteRepository.save(site);

        Category category1 = new Category();
        category1.setSite(site);
        category1.setContentType(ContentType.Article);
        category1 = categoryRepository.save(category1);

        Category category2 = new Category();
        category2.setSite(site);
        category2.setContentType(ContentType.Video);
        category2 = categoryRepository.save(category2);

        Article article = new Article();
        article.setCategory(category1);
        article.setTitle(UUID.randomUUID().toString());
        article.setContent(UUID.randomUUID().toString());
        contentRepository.save(article);

        Video video = new Video();
        video.setCategory(category2);
        video.setTitle(UUID.randomUUID().toString());
        contentRepository.save(video);

        // 添加不同的模型 不同的category 再一起搜索 好吧 就这么干
        assertThat(contentService.list(null, site, null, null))
                .contains(article)
                .contains(video)
                .hasSize(2);

        assertThat(contentService.listBySite(site, null))
                .contains(article)
                .contains(video)
                .hasSize(2);

//        assertThat(contentService.countBySite(site))
//                .isEqualTo(2);
    }

    @Test
    public void testMallProductCategoryRepository() {
//        MallProductCategory mallProductCategory = new MallProductCategory();
//        mallProductCategory.setName("123");
//        mallProductCategory.setContentType(ContentType.MallProduct);
//        List<Long> brands = new ArrayList<>();
//        brands.add(1L);
//        brands.add(2L);
//        brands.add(3L);
//        mallProductCategoryRepository.saveAndFlush(mallProductCategory);
    }

}