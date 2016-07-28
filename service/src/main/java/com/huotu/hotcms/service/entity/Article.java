/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.entity;

import com.huotu.hotcms.service.ImagesOwner;
import com.huotu.hotcms.service.common.ArticleSource;
import com.huotu.hotcms.service.util.ImageHelper;
import com.huotu.hotcms.service.util.SerialUtil;
import lombok.Getter;
import lombok.Setter;
import me.jiangcai.lib.resource.service.ResourceService;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

/**
 * 文章模型
 * Created by cwb on 2015/12/21.
 */
@Entity
@Table(name = "cms_article")
@Getter
@Setter
public class Article extends AbstractContent implements ImagesOwner {

    /**
     * 缩略图的path
     */
    @Column(name = "thumbUri", length = 200)
    private String thumbUri;

    /**
     * 文章来源
     */
    @Column(name = "articleSource")
    private ArticleSource articleSource;

    /**
     * 文章内容（富文本）
     */
    @Lob
    @Column(name = "content")
    private String content;

    /**
     * /浏览量
     */
    @Column(name = "scans")
    private int scans;

    /**
     * 点赞数量
     */
    @Column(name = "lauds")
    private int lauds;

    /**
     * 被踩数量
     */
    @Column(name = "unlauds")
    private int unlauds;

    /**
     * 作者
     */
    @Column(name = "author", length = 80)
    private String author;

    /**
     * 是否系统文章,系统文章不允许删除
     **/
    @Column(name = "isSystem")
    private boolean system = false;

    /**
     * 文章类型
     */
    @Column(name = "type")
    private String type;

    @Override
    public Article copy() {
        Article article = new Article();
        article.setAuthor(author);
        article.setContent(content);
        article.setCategory(getCategory());
        article.setSerial(SerialUtil.formatSerial(getCategory().getSite()));
        article.setSystem(system);
        article.setThumbUri(thumbUri);
        article.setCreateTime(LocalDateTime.now());
        article.setArticleSource(articleSource);
        article.setOrderWeight(getOrderWeight());
        article.setDeleted(isDeleted());
        article.setUpdateTime(LocalDateTime.now());
        article.setDescription(getDescription());
        article.setTitle(getTitle());
        article.setSerial(getSerial());
        article.setCategory(getCategory());
        return article;
    }

    @Override
    public Article copy(Site site, Category category) {
        Article article = copy();
        article.setSerial(SerialUtil.formatSerial(site));
        article.setCategory(category);
        return article;
    }

    @Override
    public String[] getImagePaths() {
        return new String[]{thumbUri};
    }

    @Override
    public void updateImage(int index, ResourceService resourceService, InputStream stream) throws IOException
            , IllegalArgumentException {
        if (thumbUri != null) {
            resourceService.deleteResource(thumbUri);
        }
        thumbUri = ImageHelper.storeAsImage("png", resourceService, stream);
    }

//    /**
//     * 所属栏目
//     */
//    @Basic
//    @ManyToOne
//    @JoinColumn(name = "categoryId")
//    private Category category;


}
