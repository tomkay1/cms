/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.entity;

import com.huotu.hotcms.service.ResourcesOwner;
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
 * 下载模型
 * Created by cwb on 2015/12/22.
 */
@Entity
@Table(name = "cms_download")
@Getter
@Setter
public class Download extends AbstractContent implements ResourcesOwner {

    /**
     * 文件名称
     */
    @Column(name = "fileName", length = 50)
    private String fileName;

    /**
     * 下载地址
     */
    @Column(name = "downloadUrl", length = 200)
    private String downloadPath;

    /**
     * 下载次数
     */
    @Column(name = "downloads")
    private int downloads;

    @Override
    public Download copy() {
        Download download = new Download();
        copyTo(download);
        download.setFileName(fileName);
        return download;
    }

    @Override
    public String[] getResourcePaths() {
        return new String[]{getDownloadPath()};
    }

    @Override
    public void updateResource(int index, String path, ResourceService resourceService) throws IOException {
        if (getDownloadPath() != null)
            resourceService.deleteResource(getDownloadPath());
        setDownloadPath(path);
    }

    @Override
    public String generateResourcePath(int index, ResourceService resourceService, InputStream stream) {
        return UUID.randomUUID().toString();
    }

}
