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
 * 视频模型
 * Created by cwb on 2015/12/22.
 */
@Entity
@Table(name = "cms_video")
@Getter
@Setter
public class Video extends AbstractContent implements ImagesOwner {


    /**
     * 缩略图uri
     */
    @Column(name = "thumbUri")
    private String thumbUri;

    /**
     * 内部储存地址,path;如果为null就表示没有保存在我方资源系统,只有{@link #outLinkUrl}可用
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
        Video video = new Video();
        copyTo(video);
//        video.setThumbUri(thumbUri);
//        video.setVideoUrl(videoUrl);
        video.setOutLinkUrl(outLinkUrl);
        return video;
    }

    @Override
    public int[] imageResourceIndexes() {
        return new int[]{0};
    }

    @Override
    public String[] getResourcePaths() {
        return new String[]{
                getThumbUri(), getVideoUrl()
        };
    }

    @Override
    public void updateResource(int index, String path, ResourceService resourceService) throws IOException {
        switch (index) {
            case 0:
                if (getThumbUri() != null)
                    resourceService.deleteResource(getThumbUri());
                setThumbUri(path);
                break;
            case 1:
                if (getVideoUrl() != null)
                    resourceService.deleteResource(getVideoUrl());
                setVideoUrl(path);
        }
    }

    @Override
    public String generateResourcePath(int index, ResourceService resourceService, InputStream stream) {
        return UUID.randomUUID().toString();
    }

}
