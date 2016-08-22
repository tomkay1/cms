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
import com.huotu.hotcms.service.model.LinkModel;
import lombok.Getter;
import lombok.Setter;
import me.jiangcai.lib.resource.service.ResourceService;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * 链接模型
 * Created by cwb on 2015/12/22.
 */
@Entity
@Table(name = "cms_link")
@Getter
@Setter
public class Link extends AbstractContent implements ImagesOwner {

    /**
     * 缩略图uri
     */
    @Column(name = "thumbUri", length = 200)
    private String thumbUri;

    /**
     * 链接地址
     */
    @Column(name = "linkUrl", length = 200)
    private String linkUrl;

    public static LinkModel toLinkModel(Link link) {
        LinkModel linkModel = new LinkModel();
        linkModel.setLinkUrl(link.getLinkUrl());
        linkModel.setThumbUri(link.getThumbUri());
        return linkModel;
    }

    @Override
    public Link copy() {
        Link link = new Link();
        copyTo(link);
//        link.setThumbUri(thumbUri);
        link.setLinkUrl(linkUrl);
        return link;
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
