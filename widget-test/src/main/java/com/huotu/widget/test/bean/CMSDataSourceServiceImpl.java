/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.widget.test.bean;

import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Gallery;
import com.huotu.hotcms.service.entity.GalleryList;
import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.widget.page.Page;
import com.huotu.hotcms.widget.service.CMSDataSourceService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhx on 2016/6/28.
 */
public class CMSDataSourceServiceImpl implements CMSDataSourceService {

    @Override
    public List<Gallery> findByGallery() {
        List<Gallery> list = new ArrayList<>();
        Gallery gallery1 = new Gallery();
        gallery1.setTitle("新闻图片列表");
        gallery1.setId(1L);
        Gallery gallery2 = new Gallery();
        gallery2.setTitle("推荐图片列表");
        gallery2.setId(2L);
        list.add(gallery1);
        list.add(gallery2);
        return list;
    }

    @Override
    public List<GalleryList> findByGalleryList(Long galleryId) {
        Gallery gallery = new Gallery();
        gallery.setId(galleryId);
        List<GalleryList> list = new ArrayList<>();
        GalleryList galleryList1 = new GalleryList();
        galleryList1.setGallery(gallery);
        galleryList1.setId(1L);
        galleryList1.setThumbUri("http://placehold.it/106x82?text=galleryList1");
        GalleryList galleryList2 = new GalleryList();
        galleryList2.setGallery(gallery);
        galleryList2.setId(2L);
        galleryList2.setThumbUri("http://placehold.it/106x82?text=galleryList2");
        list.add(galleryList1);
        list.add(galleryList2);
        return list;
    }

    @Override
    public List<Category> findByLinkCategory() {
        Category category1 = new Category();
        category1.setId(1L);
        category1.setContentType(ContentType.Link);
        category1.setName("logo墙链接模型1");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setContentType(ContentType.Link);
        category2.setName("logo墙链接模型1");
        List<Category> list = new ArrayList<>();
        list.add(category1);
        list.add(category2);
        return list;
    }

    @Override
    public List<Link> findByLinks(Long categoryId) {
        List<Link> list = new ArrayList<>();
        Link link1 = new Link();
        link1.setId(1L);
        link1.setTitle("logo1");
        link1.setThumbUri("http://placehold.it/106x82?text=logo1");

        Link link2 = new Link();
        link2.setId(2L);
        link2.setTitle("logo1");
        link2.setThumbUri("http://placehold.it/106x82?text=logo2");

        list.add(link1);
        list.add(link2);
        return list;
    }

    @Override
    public List<Category> findByArticleCategorys() {
        Category category1 = new Category();
        category1.setId(1L);
        category1.setContentType(ContentType.Article);
        category1.setName("文章类型1");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setContentType(ContentType.Article);
        category2.setName("文章类型2");
        List<Category> list = new ArrayList<>();
        list.add(category1);
        list.add(category2);
        return list;
    }

    @Override
    public Iterable<Page> findWidgetPage() {
        Page page1 = new Page();
        page1.setTitle("首页");
//        page1.setPageIdentity("1");

        Page page2 = new Page();
        page2.setTitle("新闻");
//        page2.setPageIdentity("2");

        Page page3 = new Page();
        page3.setTitle("关于我们");
//        page3.setPageIdentity("3");

        List<Page> list = new ArrayList<>();
        list.add(page1);
        list.add(page2);
        list.add(page3);
        Iterable<Page> iterable = list::iterator;
        return iterable;
    }


}
