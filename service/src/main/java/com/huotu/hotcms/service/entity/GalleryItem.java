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
import com.huotu.hotcms.service.ImagesOwner;
import com.huotu.hotcms.service.model.GalleryItemModel;
import lombok.Getter;
import lombok.Setter;
import me.jiangcai.lib.resource.service.ResourceService;
import org.springframework.http.MediaType;

import javax.persistence.Column;
import javax.persistence.Entity;
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
public class GalleryItem extends AbstractContent implements Auditable, ImagesOwner {

    /**
     * 图片规格大小,比如：98x100
     */
    @Column(name = "size", length = 20)
    private String size;

    /**
     * 图片path,它有一个特殊约定,它的后缀就是这个图片的类型,比如png 什么什么的
     */
    @Column(name = "thumbUri", length = 100)
    private String thumbUri;

    /**
     * 所属图库记录ID
     */
    @ManyToOne
    @JoinColumn(name = "galleryId")
    private Gallery gallery;


    public static GalleryItemModel getGalleryItemModel(GalleryItem galleryItem) {
        GalleryItemModel galleryItemModel = new GalleryItemModel();
        galleryItemModel.setId(galleryItem.getId());
        galleryItemModel.setThumbUri(galleryItem.getThumbUri());
        galleryItemModel.setName(galleryItem.getTitle());
        galleryItemModel.setOrderWeight(galleryItem.getOrderWeight());
        galleryItemModel.setSize(galleryItem.getSize());
        return galleryItemModel;
    }

    @Override
    public GalleryItem copy() {
        GalleryItem galleryItem = new GalleryItem();
//        galleryItem.setDeleted(isDeleted());
        galleryItem.setSerial(getSerial());
        galleryItem.setOrderWeight(getOrderWeight());
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

    public MediaType toContentType() {
        int lastDot = thumbUri.lastIndexOf(".");
        String type = thumbUri.substring(lastDot + 1);

        return MediaType.valueOf("image/" + type);
    }
}
