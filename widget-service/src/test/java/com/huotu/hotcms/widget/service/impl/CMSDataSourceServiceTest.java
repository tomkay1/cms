/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.service.impl;

import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Gallery;
import com.huotu.hotcms.service.entity.GalleryItem;
import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.repository.GalleryItemRepository;
import com.huotu.hotcms.service.repository.GalleryRepository;
import com.huotu.hotcms.service.repository.LinkRepository;
import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.service.CMSDataSourceService;
import com.huotu.hotcms.widget.test.TestBase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 这是一个级别很低的测试
 *
 * @author CJ
 */
@Transactional
public class CMSDataSourceServiceTest extends TestBase {
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private CMSDataSourceService cmsDataSourceService;
    @Autowired(required = false)
    private HttpServletResponse response;

    @Autowired
    private GalleryRepository galleryRepository;
    @Autowired
    private GalleryItemRepository galleryItemRepository;
    @Autowired
    private LinkRepository linkRepository;
    private Site site;

    @Before
    public void init() {
        // 准备下CMSContent
        site = randomSite(randomOwner());
        CMSContext.PutContext(request, response, site);
    }

    @Test
    public void category() {
        assertThat(cmsDataSourceService.findParentArticleCategory())
                .isEmpty();

        Category category = randomCategory(site, ContentType.Link);
        assertThat(cmsDataSourceService.findParentArticleCategory())
                .contains(category);

        // 再弄一个子集
        Category sub = randomCategory(site, ContentType.Link, category);

        assertThat(cmsDataSourceService.findParentArticleCategory())
                .contains(category)
                .doesNotContain(sub);
    }

    @Test
    public void link() {
        assertThat(cmsDataSourceService.findLinkCategory())
                .isEmpty();

        Category category = randomCategory(site, ContentType.Link);
        assertThat(cmsDataSourceService.findLinkCategory())
                .contains(category);

        assertThat(cmsDataSourceService.findLink(category.getId()))
                .isEmpty();

        List<Link> linkList = new ArrayList<>();
        int count = random.nextInt(10) + 2;
        while (count-- > 0) {
            linkList.add(randomLink(category));
        }

        assertThat(cmsDataSourceService.findLink(category.getId()))
                .containsAll(linkList);
    }

    @Test
    public void gallery() {
        assertThat(cmsDataSourceService.findGallery())
                .isEmpty();
        Category category = randomCategory(site, ContentType.Gallery);
        Gallery gallery = randomGallery(category);
        assertThat(cmsDataSourceService.findGallery())
                .contains(gallery);

        galleryRepository.deleteByCategory(category);
        assertThat(cmsDataSourceService.findGallery())
                .isEmpty();

        //
        Gallery gallery2 = randomGallery(category);

        assertThat(cmsDataSourceService.findGalleryItem(gallery2.getId()))
                .isEmpty();

        List<GalleryItem> itemList = new ArrayList<>();
        int count = random.nextInt(10) + 2;
        while (count-- > 0) {
            itemList.add(randomGalleryItem(gallery2));
        }

        assertThat(cmsDataSourceService.findGalleryItem(gallery2.getId()))
                .containsAll(itemList);
    }


}