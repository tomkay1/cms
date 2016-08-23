/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.entity;

import com.huotu.hotcms.service.Auditable;
import com.huotu.hotcms.service.Copyable;
import com.huotu.hotcms.service.ImagesOwner;
import com.huotu.hotcms.service.model.GalleryItemModel;
import lombok.Getter;
import lombok.Setter;
import me.jiangcai.lib.resource.service.ResourceService;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cms_galleryItem")
@Getter
@Setter
public class GalleryItem implements Auditable, Copyable<GalleryItem>, ImagesOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    /**
//     * 站点ID
//     */
//    @ManyToOne
//    @JoinColumn(name = "siteId")
//    private Site site;

    /**
     * 序列号
     */
    @Column(name = "serial", length = 100)
    private String serial;

    /**
     * 排序权重
     */
    @Column(name = "orderWeight")
    private int orderWeight;

    /**
     * 图片规格大小,比如：98x100
     */
    @Column(name = "size", length = 20)
    private String size;

    /**
     * 图片
     */
    @Column(name = "thumbUri", length = 100)
    private String thumbUri;


    /**
     * 是否已删除
     */
    @Column(name = "deleted")
    private boolean deleted = false;

    /**
     * 所属图库记录ID
     */
    @ManyToOne
    @JoinColumn(name = "galleryId")
    private Gallery gallery;

    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(name = "updateTime")
    private LocalDateTime updateTime;

    public static GalleryItemModel getGalleryItemModel(GalleryItem galleryItem) {
        GalleryItemModel galleryItemModel = new GalleryItemModel();
        galleryItemModel.setId(galleryItem.getId());
        galleryItemModel.setThumbUri(galleryItem.getThumbUri());
        return galleryItemModel;
    }

    @Override
    public GalleryItem copy() {
        GalleryItem galleryItem = new GalleryItem();
        galleryItem.setDeleted(isDeleted());
        galleryItem.setSerial(serial);
        galleryItem.setOrderWeight(orderWeight);
//        galleryItem.setThumbUri(thumbUri);
        galleryItem.setCreateTime(LocalDateTime.now());
        galleryItem.setGallery(gallery);
//        galleryItem.setSite(site);
        galleryItem.setUpdateTime(LocalDateTime.now());
        return galleryItem;
    }

    @Override
    public int[] imageResourceIndexes() {
        return new int[]{0};
    }

    @Override
    public String[] getResourcePaths() {
        return new String[]{getThumbUri()};
    }

    @Override
    public void updateResource(int index, String path, ResourceService resourceService) throws IOException {
        if (getThumbUri() != null) {
            resourceService.deleteResource(getThumbUri());
        }
        setThumbUri(path);
    }

    @Override
    public String generateResourcePath(int index, ResourceService resourceService, InputStream stream) {
        return UUID.randomUUID().toString();
    }

}
