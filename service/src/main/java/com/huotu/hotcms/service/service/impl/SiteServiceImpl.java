/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.common.ConfigInfo;
import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Download;
import com.huotu.hotcms.service.entity.Gallery;
import com.huotu.hotcms.service.entity.GalleryList;
import com.huotu.hotcms.service.entity.Host;
import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.entity.Notice;
import com.huotu.hotcms.service.entity.Region;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.Template;
import com.huotu.hotcms.service.entity.Video;
import com.huotu.hotcms.service.exception.NoSiteFoundException;
import com.huotu.hotcms.service.repository.ArticleRepository;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.repository.CustomPagesRepository;
import com.huotu.hotcms.service.repository.DownloadRepository;
import com.huotu.hotcms.service.repository.GalleryListRepository;
import com.huotu.hotcms.service.repository.GalleryRepository;
import com.huotu.hotcms.service.repository.HostRepository;
import com.huotu.hotcms.service.repository.LinkRepository;
import com.huotu.hotcms.service.repository.NoticeRepository;
import com.huotu.hotcms.service.repository.RegionRepository;
import com.huotu.hotcms.service.repository.SiteRepository;
import com.huotu.hotcms.service.repository.TemplateRepository;
import com.huotu.hotcms.service.repository.VideoRepository;
import com.huotu.hotcms.service.service.CategoryService;
import com.huotu.hotcms.service.service.HostService;
import com.huotu.hotcms.service.service.SiteService;
import com.huotu.hotcms.service.util.SerialUtil;
import com.huotu.hotcms.service.util.StringUtil;
import me.jiangcai.lib.resource.service.ResourceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Created by chendeyu on 2015/12/24.
 */
@Service
public class SiteServiceImpl implements SiteService {

    private static final Log log = LogFactory.getLog(SiteServiceImpl.class);
    @Autowired
    SiteRepository siteRepository;
    @Autowired
    HostRepository hostRepository;
    @Autowired
    ConfigInfo configInfo;
    @Autowired
    ResourceService resourceService;
    @Autowired
    TemplateRepository templateRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    CustomPagesRepository customPagesRepository;
    @Autowired
    DownloadRepository downloadRepository;
    @Autowired
    GalleryRepository galleryRepository;
    @Autowired
    LinkRepository linkRepository;
    @Autowired
    NoticeRepository noticeRepository;
    @Autowired
    VideoRepository videoRepository;
    @Autowired
    GalleryListRepository galleryListRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private HostService hostService;

    @Override
    public Site closestSite(Host host, Locale locale) throws NoSiteFoundException {
        if (host.getSites() == null || host.getSites().isEmpty())
            throw new NoSiteFoundException("no site here");
        if (host.getSites().size() == 1)
            return host.getSites().values().iterator().next();
        //逐个检查语言环境
        Locale originLocale = locale;

        while (locale != null) {
            Region region = regionRepository.findOne(locale);
            if (region == null) {
                //降级
                locale = downGrade(locale);
                continue;
            }
            Site site = host.getSites().get(region);
            if (site != null)
                return site;
            locale = downGrade(locale);
        }
        // 实在没有找到
        log.warn("no site found for host:" + host + " locale:" + originLocale);
        return host.getSites().values().iterator().next();
    }

    private Locale downGrade(Locale locale) {
        if (locale.getVariant().length() > 0)
            return new Locale(locale.getLanguage(), locale.getCountry());
        if (locale.getCountry().length() > 0)
            return new Locale(locale.getLanguage());
        return null;
    }

    @Override
    public Site getSite(long siteId) {
        return siteRepository.findOne(siteId);
    }

    @Override
    public Set<Site> findByOwnerIdAndDeleted(long ownerId, boolean deleted) {
        return siteRepository.findByOwner_IdAndDeleted(ownerId, deleted);
    }


//    @Override
//    public void siteCopy(long templateId, Site customerSite) throws Exception {
//        //根据模板ID读取到相应的站点
//        Template template = templateRepository.findOne(templateId);
////        Site templateSite = template.getSite();
//        List<CustomPages> customPages = customPagesRepository.findBySite(template);
//        for (CustomPages customPage : customPages) {
//            String templateResourceConfigUrl = configInfo.getResourcesConfig(template) + "/" + customPage.getId() + ".xml";
//            URI url = resourceService.getResource(templateResourceConfigUrl).httpUrl().toURI();
//            InputStream inputStream = HttpUtils.getInputStreamByUrl(url.toURL());
//            WidgetPage widgetPage = JAXB.unmarshal(inputStream, WidgetPage.class);
//            pageResolveService.createPageAndConfigByWidgetPage(widgetPage,
//                    customerSite.getSiteId(), false);
//        }
//        createDefaultWidgetPage(template, customerSite);
//        deepCopy(template, customerSite);
//    }

    @Override
    public void siteCopy(Template template, Site targetSite, boolean removeFirst) {
        if (removeFirst) {
            //TODO 删除所有正文,数据源和页面
        }
        deepCopy(template, targetSite);
    }

    @Override
    public Site newSite(String[] domains, String homeDomains, Site site, Locale locale) {
        if (!StringUtil.Contains(domains, homeDomains)) {//不存在主推域名则外抛出
            // 这是一个很糟糕的设计
            throw new IllegalArgumentException("没有主推域名");
//            return new ResultView(ResultOptionEnum.NOFIND_HOME_DEMON.getCode(), ResultOptionEnum.NOFIND_HOME_DEMON.getValue(), null);
        }
        Region region = regionRepository.findOne(locale);
        if (region == null) {
            region = new Region();
            region.setLocale(locale);
            region = regionRepository.save(region);
        }

        site.setRegion(region);
        site = siteRepository.save(site);

        //检查域名,如果是新的域名 则直接添加,如果已经存在域名则需要检查Owner
        for (String domain : domains) {
            Host host = hostService.getHost(domain);
            if (host == null) {
                host = new Host();
                host.setDomain(domain);
                host.setOwner(site.getOwner());
                host.setSerial(SerialUtil.formatSerial(site));
                if (domain.equals(homeDomains))
                    host.setHome(true);
                host.addSite(site);
                hostRepository.save(host);
            } else {
                // host 已存在
                if (!host.getOwner().equals(site.getOwner())) {
                    throw new IllegalArgumentException("域名已经存在");
//                    return new ResultView(ResultOptionEnum.DOMAIN_EXIST.getCode(), ResultOptionEnum.DOMAIN_EXIST.getValue(), null);
                }
                host.addSite(site);
                hostRepository.save(host);
            }
        }
        siteRepository.save(site);
//        return new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), site);
        return site;
    }

    @Override
    public Site patchSite(String[] domains, String homeDomains, Site site, Locale locale) {
        // 将之前关联
        hostService.stopHookSite(site);
        return newSite(domains, homeDomains, site, locale);
    }

    /**
     * 深度复制   站点下属类目全部复制
     *
     * @param templateSite 模板站点
     * @param customerSite 商户站点
     */
    private void deepCopy(Site templateSite, Site customerSite) {
        /*对Category的复制*/
        customerSite = siteRepository.findOne(customerSite.getSiteId());//确保查询的数据是完备的

        List<Category> categories = categoryService.getCategories(templateSite);
        for (Category category : categories) {
            Category newCategory = new Category();
            newCategory.setSerial(SerialUtil.formatSerial(customerSite));
            newCategory.setSite(customerSite);
            newCategory.setParent(category.getParent());
            newCategory.setName(category.getName());
//            newCategory.setCustom(category.isCustom());
            newCategory.setDeleted(category.isDeleted());
//            newCategory.setRoute(category.getRoute());
//            newCategory.setModelId(category.getModelId());
            newCategory.setContentType(category.getContentType());
            newCategory.setOrderWeight(category.getOrderWeight());
//            newCategory.setParentIds(category.getParentIds());
            newCategory.setCreateTime(category.getCreateTime());//这两个时间不确定是否要复制
            newCategory.setUpdateTime(category.getUpdateTime());
            category = categoryRepository.save(newCategory);
            itemsCopy(category, customerSite);
        }
        // TODO 页面的复制
    }

    /**
     * 其他数据源的深度复制
     *
     * @param category     复制后的栏目
     * @param customerSite 用户站点
     */
    private void itemsCopy(Category category, Site customerSite) {
        //文章复制
        List<Article> articles = articleRepository.findByCategory(category);
        for (Article article : articles) {
            Article newArticle = new Article();
            newArticle.setCategory(category);
            newArticle.setDescription(article.getDescription());
            newArticle.setDeleted(article.isDeleted());
            newArticle.setArticleSource(article.getArticleSource());
            newArticle.setAuthor(article.getAuthor());
            newArticle.setContent(article.getContent());
            newArticle.setSystem(article.isSystem());
            newArticle.setThumbUri(article.getThumbUri());
            newArticle.setOrderWeight(article.getOrderWeight());
            newArticle.setSerial(SerialUtil.formatSerial(customerSite));
            newArticle.setTitle(article.getTitle());
            newArticle.setCreateTime(article.getCreateTime());
            newArticle.setUpdateTime(article.getUpdateTime());
            articleRepository.save(newArticle);
        }
        //下载模型复制
        List<Download> downloads = downloadRepository.findByCategory(category);
        for (Download download : downloads) {
            Download d = new Download();
            d.setOrderWeight(download.getOrderWeight());
            d.setUpdateTime(download.getUpdateTime());
            d.setTitle(download.getTitle());
            d.setCreateTime(download.getCreateTime());
            d.setSerial(SerialUtil.formatSerial(customerSite));
            d.setDownloadUrl(download.getDownloadUrl());
            d.setCategory(category);
            d.setDescription(download.getDescription());
            d.setDeleted(download.isDeleted());
            downloadRepository.save(d);
        }
        //图库模型复制
        List<Gallery> galleries = galleryRepository.findByCategory(category);
        for (Gallery gallery : galleries) {
            Gallery g = new Gallery();
            g.setDeleted(gallery.isDeleted());
            g.setOrderWeight(gallery.getOrderWeight());
            g.setUpdateTime(gallery.getUpdateTime());
            g.setDescription(gallery.getDescription());
            g.setContent(gallery.getContent());
            g.setLinkUrl(gallery.getLinkUrl());
            g.setThumbUri(gallery.getThumbUri());
            g.setCategory(category);
            g.setSerial(SerialUtil.formatSerial(customerSite));
            g.setTitle(gallery.getTitle());
            g.setCreateTime(gallery.getCreateTime());

            g = galleryRepository.save(g);
            //图库集合复制
            List<GalleryList> galleryLists = galleryListRepository.findByGallery(gallery);
            for (GalleryList gl : galleryLists) {
                GalleryList galleryList = new GalleryList();
                galleryList.setGallery(g);
                galleryList.setSite(customerSite);
                galleryList.setUpdateTime(gl.getUpdateTime());
                galleryList.setOrderWeight(gl.getOrderWeight());
                galleryList.setSerial(SerialUtil.formatSerial(customerSite));
                galleryList.setSize(gl.getSize());
                galleryList.setThumbUri(gl.getThumbUri());
                galleryList.setDeleted(gl.isDeleted());
                galleryListRepository.save(galleryList);
            }
        }

        //链接模型复制
        List<Link> links = linkRepository.findByCategory(category);
        for (Link link : links) {
            Link link1 = new Link();
            link1.setOrderWeight(link.getOrderWeight());
            link1.setUpdateTime(link.getUpdateTime());
            link1.setTitle(link.getTitle());
            link1.setSerial(SerialUtil.formatSerial(customerSite));
            link1.setLinkUrl(link.getLinkUrl());
            link1.setThumbUri(link.getThumbUri());
            link1.setCategory(link.getCategory());
            link1.setCreateTime(link.getCreateTime());
            link1.setDescription(link.getDescription());
            link1.setDeleted(link.isDeleted());
            linkRepository.save(link1);
        }
        //公告模型复制
        List<Notice> notices = noticeRepository.findByCategory(category);
        for (Notice notice : notices) {
            Notice notice1 = new Notice();
            notice1.setDeleted(notice.isDeleted());
            notice1.setOrderWeight(notice.getOrderWeight());
            notice1.setUpdateTime(notice.getUpdateTime());
            notice1.setCreateTime(notice.getCreateTime());
            notice1.setDescription(notice.getDescription());
            notice1.setContent(notice.getContent());
            notice1.setCategory(category);
            notice1.setSerial(SerialUtil.formatSerial(customerSite));
            notice1.setTitle(notice.getTitle());
            noticeRepository.save(notice1);
        }
        //视频模型复制
        List<Video> videos = videoRepository.findByCategory(category);
        for (Video video : videos) {
            Video v = new Video();
            v.setOrderWeight(video.getOrderWeight());
            v.setUpdateTime(video.getUpdateTime());
            v.setTitle(video.getTitle());
            v.setSerial(SerialUtil.formatSerial(customerSite));
            v.setCategory(category);
            v.setOutLinkUrl(video.getOutLinkUrl());
            v.setThumbUri(video.getThumbUri());
            v.setVideoUrl(video.getVideoUrl());//视频地址 ？
            v.setDescription(video.getDescription());
            v.setDeleted(video.isDeleted()); //是否需要将此作为条件
            videoRepository.save(v);
        }

    }

}
