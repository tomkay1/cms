/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.common.ContentType;
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
import com.huotu.hotcms.service.repository.ArticleRepository;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.repository.ContentRepository;
import com.huotu.hotcms.service.repository.DownloadRepository;
import com.huotu.hotcms.service.repository.GalleryItemRepository;
import com.huotu.hotcms.service.repository.GalleryRepository;
import com.huotu.hotcms.service.repository.LinkRepository;
import com.huotu.hotcms.service.repository.NoticeRepository;
import com.huotu.hotcms.service.repository.SiteRepository;
import com.huotu.hotcms.service.repository.TemplateRepository;
import com.huotu.hotcms.service.repository.VideoRepository;
import com.huotu.hotcms.service.service.CategoryService;
import com.huotu.hotcms.service.service.ContentService;
import com.huotu.hotcms.service.service.SiteService;
import com.huotu.hotcms.service.service.TemplateService;
import me.jiangcai.lib.resource.Resource;
import me.jiangcai.lib.resource.service.ResourceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class TemplateServiceImpl implements TemplateService {

    private static final Log log = LogFactory.getLog(TemplateServiceImpl.class);

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private ContentService contentService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryService categoryService;
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
    private ContentRepository contentRepository;

    @Autowired
    private ResourceService resourceService;

    private Map<String, Boolean> laudMap = new HashMap<>();

    //使用Redis
    @Override
    public boolean laud(long templateId, long ownerId, int behavior) {
        if (behavior == 1 && isLauded(templateId, ownerId))
            return false;
        Template template = templateRepository.findOne(templateId);
        try {
            String key = templateId + "$" + ownerId;
//            int laudNum = template.getLauds();
            if (1 == behavior) {//点赞
                template.setLauds(template.getLauds() + 1);
                laudMap.put(key, true);
            } else {
                template.setLauds(template.getLauds() - 1);
                laudMap.remove(key);
            }
            templateRepository.save(template);
            return true;
        } catch (Exception e) {
            log.warn("Unexpected", e);
            return false;
        }
    }

    @Override
    public void preview(Template template) {
        template.setScans(template.getScans() + 1);
    }

    @Override
    public void use(long templateSiteID, long customerSiteId, int mode) throws IOException {
        Template template = templateRepository.findOne(templateSiteID);
        Site site = siteRepository.findOne(customerSiteId);
        if (1 == mode) {
            siteService.deleteData(site);
        }
        copy(template, site);
        template.setUseNumber(template.getUseNumber() + 1);//使用数+1
//        template.setEnabled(true);
        templateRepository.save(template);
    }

    @Override
    public boolean isLauded(long templateId, long ownerId) {
        //目前只是简单实现
        String key = templateId + "$" + ownerId;
        return laudMap.get(key) != null;
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
     * @param from 源站点
     * @param to   商户站点
     */
    private void copy(Site from, Site to) throws IOException {
        // 考虑到 有一些数据源具有上下级，所以应该先把没有上级的 弄好，再弄带这些上级的 以此类推。
        Set<Category> categories = null;

        while (true) {
            if (categories == null)
                categories = categoryRepository.findBySiteAndParentNull(from);
            else
                categories = categoryRepository.findBySiteAndParentIn(from, categories);
            if (categories.isEmpty())
                break;
            // 执行copy
            for (Category category : categories) {
                Category newOne = categoryService.copyTo(category, to);
                //Page信息的复制
//                copyPageInfo(category, copyCategory, to);
                //对内容的复制
                contentService.copyTo(category, newOne);
            }
        }

        // TODO 页面复制
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
     * 复制一份所有数据源旗下的内容
     *
     * @param src          源数据源
     * @param dist         目标数据源
     * @param customerSite 商户站点
     */
    private void copyContent(Category src, Category dist, Site customerSite) throws IOException {
        for (ContentType contentType : ContentType.values()) {
            contentService.copyTo(src, dist);
        }
        //对Article的复制


        List<Article> articles = articleRepository.findByCategory(src);
        Article newArticle;
        for (Article article : articles) {
            newArticle = article.copy(customerSite, dist);
            if (!StringUtils.isEmpty(newArticle.getThumbUri()))
                newArticle.setThumbUri(copyStaticResource(newArticle.getThumbUri()));
            contentRepository.save(newArticle);
        }
        //下载模型复制
        List<Download> downloads = downloadRepository.findByCategory(src);
        Download d;
        for (Download download : downloads) {
            d = download.copy(customerSite, dist);
            if (!StringUtils.isEmpty(download.getDownloadPath())) {
                d.setDownloadPath(copyStaticResource(download.getDownloadPath()));
                contentRepository.save(d);
            }
            //图库模型复制
            List<Gallery> galleries = galleryRepository.findByCategory(src);
            Gallery g;
            GalleryItem galleryItem;
            List<GalleryItem> galleryItems;
            for (Gallery gallery : galleries) {
                g = gallery.copy(customerSite, dist);
                if (!StringUtils.isEmpty(gallery.getThumbUri()))
                    g.setThumbUri(copyStaticResource(gallery.getThumbUri()));
                g = contentRepository.save(g);
                //图库集合复制
                galleryItems = galleryItemRepository.findByGallery(gallery);
                for (GalleryItem gl : galleryItems) {
                    galleryItem = gl.copy(customerSite, dist);
                    galleryItem.setGallery(g);
                    if (!StringUtils.isEmpty(galleryItem.getThumbUri()))
                        galleryItem.setThumbUri(copyStaticResource(galleryItem.getThumbUri()));
                    galleryItemRepository.save(galleryItem);
                }
            }
            //链接模型复制
            List<Link> links = linkRepository.findByCategory(src);
            Link link1;
            for (Link link : links) {
                link1 = link.copy(customerSite, dist);
                if (!StringUtils.isEmpty(link1.getThumbUri())) {
                    link1.setThumbUri(copyStaticResource(link1.getThumbUri()));
                }
                contentRepository.save(link1);
            }
            //公告模型复制
            List<Notice> notices = noticeRepository.findByCategory(src);
            Notice notice1;
            for (Notice notice : notices) {
                notice1 = notice.copy(customerSite, dist);
                contentRepository.save(notice1);
            }
            //视频模型复制
            List<Video> videos = videoRepository.findByCategory(src);
            Video v;
            for (Video video : videos) {
                v = video.copy(customerSite, dist);
                if (!StringUtils.isEmpty(v.getThumbUri()))
                    v.setThumbUri(copyStaticResource(v.getThumbUri()));
                if (!StringUtils.isEmpty(v.getVideoUrl()))
                    v.setVideoUrl(copyStaticResource(v.getVideoUrl()));
                contentRepository.save(v);
            }
        }
    }
}
