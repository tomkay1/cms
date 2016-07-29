/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.controller;

import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Gallery;
import com.huotu.hotcms.service.entity.GalleryItem;
import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.repository.GalleryItemRepository;
import com.huotu.hotcms.service.repository.GalleryRepository;
import com.huotu.hotcms.service.repository.LinkRepository;
import com.huotu.hotcms.widget.test.TestBase;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * Created by lhx on 2016/7/29.
 */
@Transactional
public class CMSDataSourceControllerTest extends TestBase {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private GalleryRepository galleryRepository;

    @Autowired
    private GalleryItemRepository galleryItemRepository;

    @Autowired
    private LinkRepository linkRepository;

    @Test
    public void testFindGalleryItem() throws Exception {
        Category category = new Category();
        category.setContentType(ContentType.Gallery);
        category = categoryRepository.save(category);
        Gallery gallery = new Gallery();
        gallery.setId(1L);
        gallery.setCategory(category);
        gallery = galleryRepository.save(gallery);
        GalleryItem galleryItem = new GalleryItem();
        galleryItem.setGallery(gallery);
        galleryItem.setThumbUri("http://www.baidu.com");
        galleryItemRepository.save(galleryItem);

        int code = mockMvc.perform(get("/dataSource/findGalleryItem/{parentId}", "1")
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andReturn().getResponse().getStatus();
        assertThat(code).as("存在数据").isEqualTo(HttpStatus.SC_OK);
    }

    @Test
    public void testFindLink() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setContentType(ContentType.Link);
        category = categoryRepository.save(category);
        Link link = new Link();
        link.setCategory(category);
        linkRepository.save(link);
        int code = mockMvc.perform(get("/dataSource/findLink/{parentId}", "1")
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andReturn().getResponse().getStatus();
        assertThat(code).as("存在数据").isEqualTo(HttpStatus.SC_OK);
    }

    @Test
    public void testFindChildrenArticleCategory() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setContentType(ContentType.Article);
        category = categoryRepository.save(category);

        Category category1 = new Category();
        category1.setContentType(ContentType.Article);
        category1.setParent(category);
        category1 = categoryRepository.save(category);
        Category category2 = new Category();
        category2.setContentType(ContentType.Article);
        category2.setParent(category);
        category2 = categoryRepository.save(category2);

        int code = mockMvc.perform(get("/dataSource/findChildrenArticleCategory/{parentId}", "1")
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andReturn().getResponse().getStatus();
        assertThat(code).as("存在数据").isEqualTo(HttpStatus.SC_OK);
    }
}
