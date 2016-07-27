/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Download;
import com.huotu.hotcms.service.entity.Gallery;
import com.huotu.hotcms.service.entity.GalleryItem;
import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.entity.Notice;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.Template;
import com.huotu.hotcms.service.entity.Video;
import com.huotu.hotcms.service.repository.AbstractContentRepository;
import com.huotu.hotcms.service.repository.ArticleRepository;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.repository.DownloadRepository;
import com.huotu.hotcms.service.repository.GalleryItemRepository;
import com.huotu.hotcms.service.repository.GalleryRepository;
import com.huotu.hotcms.service.repository.LinkRepository;
import com.huotu.hotcms.service.repository.NoticeRepository;
import com.huotu.hotcms.service.repository.SiteRepository;
import com.huotu.hotcms.service.repository.TemplateRepository;
import com.huotu.hotcms.service.repository.VideoRepository;
import com.huotu.hotcms.service.service.TemplateService;
import me.jiangcai.lib.resource.Resource;
import me.jiangcai.lib.resource.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by wenqi on 2016/7/15.
 */
@Service
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private DownloadRepository downloadRepository;
    @Autowired
    private GalleryRepository galleryRepository;
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private NoticeRepository noticeRepository;
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private GalleryItemRepository galleryItemRepository;

    @Autowired
    private AbstractContentRepository abstractContentRepository;

    @Autowired
    private ResourceService resourceService;

    /*Map<siteId$customerId,laudNumber>*/
    private Map<String, Integer> laudMap = new HashMap<>();

    //使用Redis
    @Override
    public boolean laud(long siteId, long ownerId, int behavior) {
        //目前只是简单实现
        try { //点赞数据储存应该使用其他技术
            String key = siteId + "$" + ownerId;
            int laudNum = 10;
            if (1 == behavior) {//点赞
                laudMap.put(key, laudNum + 1);
            } else {
                laudMap.put(key, laudNum - 1);
            }
            return true;
        } catch (Exception e) {//其他异常
            return false;
        }
    }

    @Override
    public void use(long templateSiteID, long customerSiteId, int mode) throws IOException {
        Template templateSite = templateRepository.findOne(templateSiteID);
        Site customerSite = siteRepository.findOne(customerSiteId);
        if (1 == mode) {
            delete(customerSite);
        }
        copy(templateSite, customerSite);
        templateSite.setUseNumber(templateSite.getUseNumber() + 1);//使用数+1
        templateSite.setEnabled(true);
        templateRepository.save(templateSite);
    }

    @Override
    public boolean isLauded(long siteId, long ownerId) {
        //目前只是简单实现
        String key = siteId + "$" + ownerId;
        return laudMap.get(key) != null;
    }

    /**
     * 删掉要使用模板站点的商户站点下的数据
     *
     * @param customerSite 商户站点
     */
    private void delete(Site customerSite) throws IOException {
        List<Category> categories = categoryRepository.findBySite(customerSite);
        if (categories.isEmpty())
            return;
        //删除内容
        for (Category category : categories) {
            articleRepository.deleteByCategory(category);
            downloadRepository.deleteByCategory(category);
            linkRepository.deleteByCategory(category);
            noticeRepository.deleteByCategory(category);
            videoRepository.deleteByCategory(category);
            List<Gallery> galleries = galleryRepository.findByCategory(category);
            galleries.forEach(galleryItemRepository::deleteByGallery);
            galleryRepository.deleteByCategory(category);
            //静态资源删除
            deleteStaticResourceByCategory(category);
        }
        //删除数据源
        categoryRepository.deleteBySite(customerSite);
        //删除页面数据
        // TODO 嘿嘿 有麻烦了
//        pageInfoRepository.deleteBySite(customerSite);
    }

    /**
     * 删除的同时，如果有静态资源，也一并删除
     *
     * @param category 数据源
     */
    private void deleteStaticResourceByCategory(Category category) throws IOException {
        List<Article> articles = articleRepository.findByCategory(category);
        for (Article article : articles) {
            if (!StringUtils.isEmpty(article.getThumbUri())) {
                deleteStaticResourceByPath(article.getThumbUri());
            }
        }
        List<Download> downloads = downloadRepository.findByCategory(category);
        for (Download download : downloads) {
            if (!StringUtils.isEmpty(download.getDownloadUrl()))
                deleteStaticResourceByPath(download.getDownloadUrl());
        }
        List<Gallery> galleries = galleryRepository.findByCategory(category);
        List<GalleryItem> galleryItems;
        for (Gallery gallery : galleries) {
            if (!StringUtils.isEmpty(gallery.getThumbUri()))
                deleteStaticResourceByPath(gallery.getThumbUri());
            galleryItems = galleryItemRepository.findByGallery(gallery);
            for (GalleryItem galleryItem : galleryItems) {
                if (!StringUtils.isEmpty(galleryItem.getThumbUri()))
                    deleteStaticResourceByPath(galleryItem.getThumbUri());
            }
        }
        List<Link> links = linkRepository.findByCategory(category);
        for (Link link : links) {
            if (!StringUtils.isEmpty(link.getThumbUri())) {
                deleteStaticResourceByPath(link.getThumbUri());
            }
        }
        List<Video> videos = videoRepository.findByCategory(category);
        for (Video video : videos) {
            if (!StringUtils.isEmpty(video.getThumbUri()))
                deleteStaticResourceByPath(video.getThumbUri());
            if (!StringUtils.isEmpty(video.getVideoUrl()))
                deleteStaticResourceByPath(video.getVideoUrl());
        }
    }

    /**
     * 通过资源所在地址，删除资源
     *
     * @param resourcePath 资源地址
     */
    private void deleteStaticResourceByPath(String resourcePath) throws IOException {
        resourceService.deleteResource(resourcePath);
    }

    /**
     * 在复制的同时，对静态资源也做一份copy，并返回复制后资源的地址
     *
     * @param resourcePath 要复制的资源的地址
     * @return 复制后资源的地址
     */
    private String copyStaticResource(String resourcePath) throws IOException {
        int dotPosition = resourcePath.lastIndexOf(".");
        String suffix = resourcePath.substring(dotPosition);
        Resource resource = resourceService.getResource(resourcePath);
        InputStream is = resource.getInputStream();
        String path = "upload/" + UUID.randomUUID().toString() + suffix;//直接上传到新地址？
        resourceService.uploadResource(path, is);
        return path;
    }

    /**
     * 复制
     *
     * @param templateSite 模板站点
     * @param customerSite 商户站点
     */
    private void copy(Site templateSite, Site customerSite) throws IOException {
        List<Category> categories = categoryRepository.findBySite(templateSite);
        Category copyCategory;
        //数据源复制
        for (Category category : categories) {
            copyCategory = category.copy(customerSite, null);
            categoryRepository.save(copyCategory);
            //Page信息的复制
            copyPageInfo(category, copyCategory, customerSite);
            //对内容的复制
            copyContent(category, copyCategory, customerSite);
        }
    }

    /**
     * 把模板站点下的页面数据进行复制
     * <p>
     * TODO 页面跟数据源有何关系?</p>
     * 因为PageInfo被重构到了api所有这里目前是不靠谱了,设计一个接口让api可以介入即可
     *
     * @param templateCategory 模板站点的数据源
     * @param copyCategory     复制到商户站点的数据源
     * @param customerSite     商户站点
     */
    private void copyPageInfo(Category templateCategory, Category copyCategory, Site customerSite) {
//        List<PageInfo> pageInfoList = pageInfoRepository.findByCategory(templateCategory);
//        PageInfo copyPageInfo;
//        for (PageInfo pageInfo : pageInfoList) {
//            copyPageInfo = pageInfo.copy();
//            copyPageInfo.setSite(customerSite);
//            copyPageInfo.setCategory(copyCategory);
//            pageInfoRepository.save(copyPageInfo);
//        }
    }

    /**
     * 对模板站点的内容数据进行复制
     *
     * @param templateCategory 模板站点的数据源
     * @param copyCategory     复制到商户站点的数据源
     * @param customerSite     商户站点
     */
    private void copyContent(Category templateCategory, Category copyCategory, Site customerSite) throws IOException {
        //对Article的复制
        List<Article> articles = articleRepository.findByCategory(templateCategory);
        Article newArticle;
        for (Article article : articles) {
            newArticle = article.copy(customerSite, copyCategory);
            if (!StringUtils.isEmpty(newArticle.getThumbUri()))
                newArticle.setThumbUri(copyStaticResource(newArticle.getThumbUri()));
            abstractContentRepository.save(newArticle);
        }
        //下载模型复制
        List<Download> downloads = downloadRepository.findByCategory(templateCategory);
        Download d;
        for (Download download : downloads) {
            d = download.copy(customerSite, copyCategory);
            if (!StringUtils.isEmpty(download.getDownloadUrl())) {
                d.setDownloadUrl(copyStaticResource(download.getDownloadUrl()));
                abstractContentRepository.save(d);
            }
            //图库模型复制
            List<Gallery> galleries = galleryRepository.findByCategory(templateCategory);
            Gallery g;
            GalleryItem galleryItem;
            List<GalleryItem> galleryItems;
            for (Gallery gallery : galleries) {
                g = gallery.copy(customerSite, copyCategory);
                if (!StringUtils.isEmpty(gallery.getThumbUri()))
                    g.setThumbUri(copyStaticResource(gallery.getThumbUri()));
                g = abstractContentRepository.save(g);
                //图库集合复制
                galleryItems = galleryItemRepository.findByGallery(gallery);
                for (GalleryItem gl : galleryItems) {
                    galleryItem = gl.copy(customerSite, copyCategory);
                    galleryItem.setGallery(g);
                    if (!StringUtils.isEmpty(galleryItem.getThumbUri()))
                        galleryItem.setThumbUri(copyStaticResource(galleryItem.getThumbUri()));
                    galleryItemRepository.save(galleryItem);
                }
            }
            //链接模型复制
            List<Link> links = linkRepository.findByCategory(templateCategory);
            Link link1;
            for (Link link : links) {
                link1 = link.copy(customerSite, copyCategory);
                if (!StringUtils.isEmpty(link1.getThumbUri())) {
                    link1.setThumbUri(copyStaticResource(link1.getThumbUri()));
                }
                abstractContentRepository.save(link1);
            }
            //公告模型复制
            List<Notice> notices = noticeRepository.findByCategory(templateCategory);
            Notice notice1;
            for (Notice notice : notices) {
                notice1 = notice.copy(customerSite, copyCategory);
                abstractContentRepository.save(notice1);
            }
            //视频模型复制
            List<Video> videos = videoRepository.findByCategory(templateCategory);
            Video v;
            for (Video video : videos) {
                v = video.copy(customerSite, copyCategory);
                if (!StringUtils.isEmpty(v.getThumbUri()))
                    v.setThumbUri(copyStaticResource(v.getThumbUri()));
                if (!StringUtils.isEmpty(v.getVideoUrl()))
                    v.setVideoUrl(copyStaticResource(v.getVideoUrl()));
                abstractContentRepository.save(v);
            }
        }
    }
}
