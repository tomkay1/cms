/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.entity;

import com.huotu.hotcms.service.util.ImageHelper;
import com.huotu.hotcms.service.util.SerialUtil;
import lombok.Getter;
import lombok.Setter;
import me.jiangcai.lib.resource.service.ResourceService;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

/**
 * 视频模型
 * Created by cwb on 2015/12/22.
 */
@Entity
@Table(name = "cms_video")
@Getter
@Setter
public class Video extends AbstractContent {


    /**
     * 缩略图uri
     */
    @Column(name = "thumbUri")
    private String thumbUri;

    /**
     * 内部储存地址
     */
    @Column(name = "videoUrl")
    private String videoUrl;

    /**
     * 外部链接地址
     */
    @Column(name = "outLinkUrl")
    private String outLinkUrl;

    /**
     * 播放次数
     */
    @Column(name = "playTimes")
    private int playTimes;

    @Override
    public Video copy() {
        Video video=new Video();
        video.setThumbUri(thumbUri);
        video.setVideoUrl(videoUrl);
        video.setTitle(getTitle());
        video.setOutLinkUrl(outLinkUrl);
        video.setDescription(getDescription());
        video.setCreateTime(LocalDateTime.now());
        video.setDeleted(isDeleted());
        video.setOrderWeight(getOrderWeight());
        video.setUpdateTime(LocalDateTime.now());
        video.setCategory(getCategory());
        video.setSerial(getSerial());
        return video;
    }

    @Override
    public Video copy(Site site, Category category) {
        Video video=copy();
        video.setSerial(SerialUtil.formatSerial(site));
        video.setCategory(category);
        return video;
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
//    @ManyToOne
//    @JoinColumn(name = "categoryId")
//    private Category category;
}
