/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.ResourcesOwner;
import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.entity.AbstractContent;
import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Download;
import com.huotu.hotcms.service.entity.Gallery;
import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.entity.Notice;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.Video;
import com.huotu.hotcms.service.entity.support.MallGoodsContent;
import com.huotu.hotcms.service.repository.ArticleRepository;
import com.huotu.hotcms.service.repository.ContentRepository;
import com.huotu.hotcms.service.repository.DownloadRepository;
import com.huotu.hotcms.service.repository.GalleryRepository;
import com.huotu.hotcms.service.repository.LinkRepository;
import com.huotu.hotcms.service.repository.NoticeRepository;
import com.huotu.hotcms.service.repository.VideoRepository;
import com.huotu.hotcms.service.service.CommonService;
import com.huotu.hotcms.service.service.ContentService;
import com.huotu.hotcms.service.service.TemplateService;
import com.huotu.huobanplus.sdk.common.repository.GoodsRestRepository;
import me.jiangcai.lib.resource.service.ResourceService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.NumberUtils;

import javax.persistence.criteria.Predicate;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ContentServiceImpl implements ContentService {

    private final Map<ContentType, Class<? extends AbstractContent>> types;
    @Autowired
    private ContentRepository contentRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private DownloadRepository downloadRepository;
    @Autowired
    private GalleryRepository galleryRepository;
    @Autowired
    private NoticeRepository noticeRepository;
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private ResourceService resourceService;
    @Autowired
    private CommonService commonService;

    @Autowired
    private GoodsRestRepository goodsRestRepository;

    {
        HashMap<ContentType, Class<? extends AbstractContent>> typeClassHashMap = new HashMap<>();
        typeClassHashMap.put(ContentType.Article, Article.class);
        typeClassHashMap.put(ContentType.Download, Download.class);
        typeClassHashMap.put(ContentType.Gallery, Gallery.class);
        typeClassHashMap.put(ContentType.Notice, Notice.class);
        typeClassHashMap.put(ContentType.Link, Link.class);
        typeClassHashMap.put(ContentType.Video, Video.class);
        types = Collections.unmodifiableMap(typeClassHashMap);
    }

    @Override
    public ContentType[] normalContentTypes() {
        return new ContentType[]{
                ContentType.Article,
                ContentType.Download,
                ContentType.Gallery,
                ContentType.Notice,
                ContentType.Link,
                ContentType.Video
        };
    }

    public AbstractContent getContent(Site site, String serial) {
        if (serial.startsWith("mallgood_")) {
            MallGoodsContent mallGoodsContent = new MallGoodsContent();
            String goodsId = serial.substring(serial.lastIndexOf("mallgood_"));
            com.huotu.huobanplus.common.entity.Goods goods = null;
            try {
                goods = goodsRestRepository.getOneByPK(NumberUtils.parseNumber(goodsId, Long.class));
            } catch (IOException e) {
            }
            mallGoodsContent.setMallGoods(goods);
            return mallGoodsContent;
        }
        return contentRepository.findOne((root, query, cb) -> cb.and(cb.equal(root.get("category").get("site"), site)
                , cb.equal(root.get("serial"), serial)));
    }


    @Override
    public Iterable<AbstractContent> list(String title, Site site, Long category, Pageable pageable) {
        Specification<AbstractContent> specification = (root, query, cb) -> {
            Predicate predicate = cb.equal(root.get("category").get("site"), site);
            if (title != null) {
                Predicate titlePredicate = cb.or(cb.like(root.get("title"), "%" + title + "%")
                        , cb.like(root.get("description"), "%" + title + "%"));
                predicate = cb.and(titlePredicate, predicate);
            }

            if (category != null) {
                predicate = cb.and(cb.equal(root.get("category").get("id"), category), predicate);
            }
            return predicate;
        };

        if (pageable == null)
            return contentRepository.findAll(specification);
        return contentRepository.findAll(specification, pageable);
    }

    @Override
    public Iterable<AbstractContent> listBySite(Site site, Pageable pageable) {
        Specification<AbstractContent> specification = specificationBySite(site);

        if (pageable == null)
            return contentRepository.findAll(specification);
        return contentRepository.findAll(specification, pageable);
    }

    @Override
    public Iterable<AbstractContent> listBySite(Site site, ContentType contentType, Pageable pageable) {
        Specification<AbstractContent> specification = specificationBySiteAndContentType(site, contentType);

        if (pageable == null)
            return contentRepository.findAll(specification);
        return contentRepository.findAll(specification, pageable);
    }

    @NotNull
    private Specification<AbstractContent> specificationBySiteAndContentType(Site site, ContentType contentType) {
        return (root, query, cb) -> cb.and(
                cb.equal(root.get("category").get("site"), site)
                , cb.equal(root.get("category").get("contentType"), contentType));
    }

    @NotNull
    private Specification<AbstractContent> specificationBySite(Site site) {
        return (root, query, cb) -> cb.equal(root.get("category").get("site"), site);
    }

//    @Override
//    public long countBySite(Site site) {
//        Specification<AbstractContent> specification = specificationBySite(site);
//        return contentRepository.count(specification);
//    }

    private Iterable<AbstractContent> listByCategory(Category category, Pageable pageable) {
        Specification<AbstractContent> specification = specificationByCategory(category);

        if (pageable == null)
            return contentRepository.findAll(specification);
        return contentRepository.findAll(specification, pageable);
    }

    @NotNull
    private Specification<AbstractContent> specificationByCategory(Category category) {
        return (root, query, cb) -> cb.equal(root.get("category"), category);
    }

    @Override
    public AbstractContent findById(long contentId, ContentType type) {
        Class<? extends AbstractContent> clazz = types.get(type);
        if (clazz == null)
            throw new IllegalArgumentException(type.name() + " is unknown.");
        return findById(contentId, clazz);
    }

    @Override
    public AbstractContent findById(long contentId, Class<? extends AbstractContent> clazz) {
        if (clazz == Article.class)
            return articleRepository.findOne(contentId);
        if (clazz == Download.class)
            return downloadRepository.findOne(contentId);
        if (clazz == Gallery.class)
            return galleryRepository.findOne(contentId);
        if (clazz == Notice.class)
            return noticeRepository.findOne(contentId);
        if (clazz == Link.class)
            return linkRepository.findOne(contentId);
        if (clazz == Video.class)
            return videoRepository.findOne(contentId);

        throw new IllegalArgumentException(clazz.getName() + " is unknown.");
    }

    @Override
    public void delete(AbstractContent content) throws IOException {
        contentRepository.delete(content);
        if (content instanceof ResourcesOwner)
            commonService.deleteResource((ResourcesOwner) content);
    }

    @Override
    public AbstractContent newContent(ContentType type) {
        Class<? extends AbstractContent> clazz = types.get(type);
        if (clazz == null)
            throw new IllegalArgumentException(type.name() + " is unknown.");
        AbstractContent content;
        try {
            content = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }

        init(content);

        return content;
    }

    @Override
    public void init(AbstractContent content) {
        content.setSerial(UUID.randomUUID().toString().replace("-", ""));
        content.setCreateTime(LocalDateTime.now());
        content.setDeleted(false);
    }

    @Override
    public void copyTo(Category src, Category dist) throws IOException {
        for (AbstractContent content : listByCategory(src, null)) {
            // 执行复制
            // 不复制页面
            if (content.getClass().getName().endsWith("PageInfo"))
                continue;
            AbstractContent newContent = content.copy();

            // 看下目标站是否已存在
            String append = "";
            while (getContent(dist.getSite(), newContent.getSerial() + append) != null) {
                append += TemplateService.DuplicateAppend;
            }
            newContent.setSerial(newContent.getSerial() + append);
            newContent.setCategory(dist);
            contentRepository.save(newContent);
            //成功了 那就复制资源！

            if (newContent instanceof ResourcesOwner) {
                ResourcesOwner oldResources = (ResourcesOwner) content;
                ResourcesOwner newResources = (ResourcesOwner) newContent;
                //首先做一个检查 新的资源肯定都是没的！
                for (String path : newResources.getResourcePaths()) {
                    assert path == null;
                }

                newResources.updateResources(resourceService, oldResources.getResourcePaths());
            }

            // TODO 特殊类型 图库！
//            for (Gallery gallery : galleries) {
//                g = gallery.copy(customerSite, dist);
//                if (!StringUtils.isEmpty(gallery.getThumbUri()))
//                    g.setThumbUri(copyStaticResource(gallery.getThumbUri()));
//                g = contentRepository.save(g);
//                //图库集合复制
//                galleryItems = galleryItemRepository.findByGallery(gallery);
//                for (GalleryItem gl : galleryItems) {
//                    galleryItem = gl.copy(customerSite, dist);
//                    galleryItem.setGallery(g);
//                    if (!StringUtils.isEmpty(galleryItem.getThumbUri()))
//                        galleryItem.setThumbUri(copyStaticResource(galleryItem.getThumbUri()));
//                    galleryItemRepository.save(galleryItem);
//                }
//            }
        }
    }


//    @Override
//    public PageData<Contents> getPage(String title, Long siteId, Long category, int page, int pageSize) {
//        PageData<Contents> data = null;
//        List<Object[]> contentsList =new ArrayList<>();
//        List<Object[]> contentsSize =new ArrayList<>();
//        if(title==null){
//            title="%"+""+"%";
//        }
//        else{
//            title ="%"+title+"%";
//        }
//        if(category==-1){//当搜索条件只有站点时
//            contentsList = baseEntityRepository.findAllContentsBySiteIdAndName(siteId, title,(page-1)*pageSize,pageSize);
//            contentsSize =baseEntityRepository.findContentsSizeBySiteIdAndName(siteId, title);
//        }
//        else{
//            String parentIds=categoryService.getCategoryParentIds(category);
//            contentsList=baseEntityRepository.findAllContentsBySiteIdAndCategoryIdsAndName(siteId,parentIds,title,(page-1)*pageSize,pageSize);
//            contentsSize =baseEntityRepository.findContentsSizeBySiteIdAndCategoryIdsAndName(siteId, parentIds, title);
//        }
//        List<Contents> contentsList1 = new ArrayList<>();
//        for(Object[] o : contentsList) {
//            Contents contents = new Contents();
//            contents.setTitle((String)o[0]);
//            contents.setDescription((String)o[1]);
//            contents.setName((String) o[2]);
//            contents.setId((Long) o[3]);
//            Integer modelId=(Integer) o[4];
//            if(modelId!=null) {
//                contents.setModelId(modelId);
//                contents.setModel(EnumUtils.valueOf(ModelType.class, modelId).toString().toLowerCase());
//                contents.setModelname(EnumUtils.valueOf(ModelType.class, modelId).getValue().toString());
//            }
//            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//定义格式，不显示毫秒
//            String str = df.format(o[5]);
//            contents.setCreateTime((String) str);
//            contentsList1.add(contents);
//        }
//        int PageCount =0;
//        if (contentsList.size()!=0){
//            int yushu=contentsSize.size()%pageSize;
//            if(yushu==0){
//                PageCount =contentsSize.size()/pageSize;
//            }
//            else {
//                PageCount =contentsSize.size()/pageSize+1;
//            }
//        }
//        else{
//             PageCount =0;
//        }
//        data = new PageData<Contents>();
//        data.setPageCount(PageCount);//总页码
//        data.setPageIndex(page);//页码
//        data.setPageSize(contentsList.size());//页容量
//        data.setTotal(contentsSize.size());//总数
//        data.setRows((Contents[])contentsList1.toArray(new Contents[contentsList1.size()]));
//        return data;
//    }

}
