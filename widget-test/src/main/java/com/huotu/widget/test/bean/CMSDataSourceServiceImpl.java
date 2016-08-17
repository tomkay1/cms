/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.widget.test.bean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.model.BaseModel;
import com.huotu.hotcms.service.model.LinkModel;
import com.huotu.hotcms.service.model.NavbarPageInfoModel;
import com.huotu.hotcms.service.model.widget.VideoModel;
import com.huotu.hotcms.widget.entity.PageInfo;
import com.huotu.hotcms.widget.service.CMSDataSourceService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhx on 2016/6/28.
 */
public class CMSDataSourceServiceImpl implements CMSDataSourceService {


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
    public List<LinkModel> findLinkContent(Long categoryId) {
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
        if (list != null && list.size() > 0) {
            List<LinkModel> linkModels = new ArrayList<>();
            for (Link link : list) {
                linkModels.add(Link.toLinkModel(link));
            }
            return linkModels;
        }
        return null;
    }

    @Override
    public List<Category> findVideoCategory() {
        Category category = new Category();
        category.setSerial("456454564");
        category.setName("公司视频");
        category.setContentType(ContentType.Video);
        List<Category> categories = new ArrayList<>();
        categories.add(category);
        return categories;
    }

    @Override
    public List<VideoModel> findVideoContent(String serial) {
        VideoModel videoModel1 = new VideoModel();
        videoModel1.setVideoUrl("http://localhost:8080/video/123.mp4");
        videoModel1.setThumbUri("http://placehold.it/106x82?text=voide");
        videoModel1.setOutLinkUrl("http://player.video.qiyi.com/4e5bd13c2da9f53f1b43d7a202f3bca9/0/5543/v_19rrm04vks.swf-" +
                "albumId=522414000-tvId=522414000-isPurchase=0-cnId=6");
        videoModel1.setTitle("视频1");
        videoModel1.setId(1L);
        VideoModel videoModel2 = new VideoModel();
        videoModel2.setVideoUrl("http://localhost:8080/video/123.mp4");
        videoModel2.setThumbUri("http://placehold.it/106x82?text=voide");
        videoModel2.setOutLinkUrl("http://player.video.qiyi.com/4e5bd13c2da9f53f1b43d7a202f3bca9/0/5543/v_19rrm04vks.swf-" +
                "albumId=522414000-tvId=522414000-isPurchase=0-cnId=6");
        videoModel2.setTitle("视频2");
        videoModel2.setId(2L);
        List<VideoModel> videoModels = new ArrayList<>();
        videoModels.add(videoModel1);
        videoModels.add(videoModel2);
        return videoModels;
    }

    @Override
    public List<Category> findArticleCategory() {
        Category category = new Category();
        category.setSerial("123456789");
        category.setName("新闻快讯");
        category.setContentType(ContentType.Article);
        List<Category> categories = new ArrayList<>();
        categories.add(category);
        return categories;
    }

    @Override
    public List<BaseModel> findArticleContent(String serial) {
        BaseModel baseModel = new BaseModel();
        baseModel.setId(1L);
        baseModel.setTitle("中共19大召开");
        baseModel.setCreateTime(LocalDateTime.now());
        BaseModel baseModel1 = new BaseModel();
        baseModel1.setId(2L);
        baseModel1.setTitle("中共192大召开");
        baseModel1.setCreateTime(LocalDateTime.now());
        List<BaseModel> list = new ArrayList<>();
        list.add(baseModel);
        list.add(baseModel1);
        return list;
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
            navbarPageInfoModel.setName(pageInfo.getTitle());
            navbarPageInfoModel.setId(pageInfo.getPageId());
            navbarPageInfoModel.setPagePath(pageInfo.getPagePath());
            navbarPageInfoModel.setPid(pageInfo.getParent() != null ? pageInfo.getParent().getPageId() : 0);
            navbarPageInfoModels.add(navbarPageInfoModel);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(navbarPageInfoModels);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    @Override
    public String findSiteNotParentPage() {
        PageInfo pageInfo1 = new PageInfo();
        pageInfo1.setTitle("首页");
        pageInfo1.setPagePath("");
        pageInfo1.setPageId(1L);

        PageInfo pageInfo2 = new PageInfo();
        pageInfo2.setTitle("新闻");
        pageInfo2.setPagePath("xw");
        pageInfo2.setPageId(2L);
        List<PageInfo> list = new ArrayList<>();
        list.add(pageInfo1);
        list.add(pageInfo2);

        List<NavbarPageInfoModel> navbarPageInfoModels = new ArrayList<>();
        for (PageInfo pageInfo : list) {
            NavbarPageInfoModel navbarPageInfoModel = new NavbarPageInfoModel();
            navbarPageInfoModel.setName(pageInfo.getTitle());
            navbarPageInfoModel.setId(pageInfo.getPageId());
            navbarPageInfoModel.setPagePath(pageInfo.getPagePath());
            navbarPageInfoModel.setPid(pageInfo.getParent() != null ? pageInfo.getParent().getPageId() : 0);
            navbarPageInfoModels.add(navbarPageInfoModel);
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(navbarPageInfoModels);
        } catch (JsonProcessingException e) {
            return "";
        }

    }

}
