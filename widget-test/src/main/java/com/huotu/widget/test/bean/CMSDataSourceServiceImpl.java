/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.widget.test.bean;

import com.alibaba.fastjson.JSONObject;
import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Gallery;
import com.huotu.hotcms.service.entity.GalleryItem;
import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.model.CollapseArtcleCategory;
import com.huotu.hotcms.service.model.NavbarPageInfoModel;
import com.huotu.hotcms.widget.entity.PageInfo;
import com.huotu.hotcms.widget.service.CMSDataSourceService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhx on 2016/6/28.
 */
public class CMSDataSourceServiceImpl implements CMSDataSourceService {

    @Override
    public List<Gallery> findGallery() {
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
    public List<GalleryItem> findGalleryItem(Long galleryId) {
        Gallery gallery = new Gallery();
        gallery.setId(galleryId);
        List<GalleryItem> list = new ArrayList<>();
        GalleryItem galleryItem1 = new GalleryItem();
        galleryItem1.setGallery(gallery);
        galleryItem1.setId(1L);
        galleryItem1.setThumbUri("http://placehold.it/106x82?text=galleryItem1");
        GalleryItem galleryItem2 = new GalleryItem();
        galleryItem2.setGallery(gallery);
        galleryItem2.setId(2L);
        galleryItem2.setThumbUri("http://placehold.it/106x82?text=galleryItem2");
        list.add(galleryItem1);
        list.add(galleryItem2);
        return list;
    }

    @Override
    public List<Category> findLinkCategory() {
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
    public List<Link> findLink(Long categoryId) {
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
    public List<Category> findParentArticleCategory() {
        Category category1 = new Category();
        category1.setId(666L);
        category1.setContentType(ContentType.Article);
        category1.setName("文章类型1");

        Category category2 = new Category();
        category2.setId(888L);
        category2.setContentType(ContentType.Article);
        category2.setName("文章类型2");
        List<Category> list = new ArrayList<>();
        list.add(category1);
        list.add(category2);
        return list;
    }

    @Override
    public String findChildrenArticleCategory(Long parentId) {
        Category parent = new Category();
        parent.setId(parentId);
        parent.setName("文章类型1");
        parent.setContentType(ContentType.Article);

        Category category1 = new Category();
        category1.setId(1L);
        category1.setContentType(ContentType.Article);
        category1.setName("文章类型1-1");
        category1.setParent(parent);
        Category category12 = new Category();
        category12.setId(12L);
        category12.setContentType(ContentType.Article);
        category12.setName("文章类型1-2");
        category12.setParent(category1);
        Category category13 = new Category();
        category13.setId(13L);
        category13.setContentType(ContentType.Article);
        category13.setName("文章类型1-3");
        category13.setParent(category12);

        Category category2 = new Category();
        category2.setId(2L);
        category2.setContentType(ContentType.Article);
        category2.setName("文章类型1-2-1");
        category2.setParent(parent);
        Category category21 = new Category();
        category21.setId(21L);
        category21.setContentType(ContentType.Article);
        category21.setName("文章类型1-2-2");
        category21.setParent(category2);

        Category category3 = new Category();
        category3.setId(3L);
        category3.setContentType(ContentType.Article);
        category3.setName("文章类型1-3-1");
        category3.setParent(parent);

        List<Category> list = new ArrayList<>();
        list.add(category1);
        list.add(category12);
        list.add(category13);
        list.add(category2);
        list.add(category21);
        list.add(category3);

        List<CollapseArtcleCategory> collapseArtcleCategories = new ArrayList<>();
        for (Category category : list) {
            CollapseArtcleCategory collapseArtcleCategory = new CollapseArtcleCategory();
            collapseArtcleCategory.setText(category.getName());
            collapseArtcleCategory.setHref(category.getSerial());
            collapseArtcleCategory.setCategoryId(category.getId());
            collapseArtcleCategory.setParentId(category.getParent() != null ? category.getParent().getId() : 0);
            collapseArtcleCategories.add(collapseArtcleCategory);
        }
        List<CollapseArtcleCategory> rootTrees = new ArrayList<>();
        for (CollapseArtcleCategory collapseArtcleCategory : collapseArtcleCategories) {
            if (collapseArtcleCategory.getParentId() == 0) {
                rootTrees.add(collapseArtcleCategory);
            }
            for (CollapseArtcleCategory t : collapseArtcleCategories) {
                if (t.getParentId() == collapseArtcleCategory.getCategoryId()) {
                    collapseArtcleCategory.getNodes().add(t);
                }
            }
        }
        return JSONObject.toJSONString(rootTrees);
    }

    @Override
    public String findSitePage() {
        PageInfo pageInfo1 = new PageInfo();
        pageInfo1.setTitle("首页");
        pageInfo1.setPagePath("");
        pageInfo1.setPageId(1L);

        PageInfo pageInfo2 = new PageInfo();
        pageInfo2.setTitle("新闻");
        pageInfo2.setPagePath("xw");
        pageInfo2.setPageId(2L);

        PageInfo gjxw = new PageInfo();
        gjxw.setTitle("国际新闻");
        gjxw.setPagePath("gjxw");
        gjxw.setPageId(22L);
        gjxw.setParent(pageInfo2);

        PageInfo gnxw = new PageInfo();
        gnxw.setTitle("国内新闻");
        gnxw.setParent(pageInfo2);
        gnxw.setPageId(23L);
        gnxw.setPagePath("gnxw");

        PageInfo zjxw = new PageInfo();
        zjxw.setTitle("浙江新闻");
        zjxw.setParent(gnxw);
        zjxw.setPageId(231L);
        zjxw.setPagePath("zjxw");

        PageInfo pageInfo3 = new PageInfo();
        pageInfo3.setTitle("关于我们");
        pageInfo3.setPagePath("guwm");
        pageInfo3.setPageId(3L);

        List<PageInfo> list = new ArrayList<>();
        list.add(pageInfo1);
        list.add(gjxw);
        list.add(pageInfo2);
        list.add(pageInfo3);
        list.add(gnxw);
        list.add(zjxw);

        List<NavbarPageInfoModel> navbarPageInfoModels = new ArrayList<>();
        for (PageInfo pageInfo : list) {
            NavbarPageInfoModel navbarPageInfoModel = new NavbarPageInfoModel();
            navbarPageInfoModel.setText(pageInfo.getTitle());
            navbarPageInfoModel.setHref(pageInfo.getPagePath());
            navbarPageInfoModel.setPageId(pageInfo.getPageId());
            navbarPageInfoModel.setParentId(pageInfo.getParent() != null ? pageInfo.getParent().getPageId() : 0);
            navbarPageInfoModels.add(navbarPageInfoModel);
        }
        List<NavbarPageInfoModel> rootTrees = new ArrayList<>();
        for (NavbarPageInfoModel navbarPageInfoModel : navbarPageInfoModels) {
            if (navbarPageInfoModel.getParentId() == 0) {
                rootTrees.add(navbarPageInfoModel);
            }
            for (NavbarPageInfoModel t : navbarPageInfoModels) {
                if (t.getParentId() == navbarPageInfoModel.getPageId()) {
                    navbarPageInfoModel.getNodes().add(t);
                }
            }
        }
        return JSONObject.toJSONString(rootTrees);
    }

}
