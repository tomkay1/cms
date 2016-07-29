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
import com.huotu.hotcms.service.util.ImageHelper;
import lombok.Getter;
import lombok.Setter;
import me.jiangcai.lib.resource.service.ResourceService;

import javax.persistence.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * 模板信息,模板也就是一个站点
 * 模板名称也就是站点名称
 */
@Entity
@Table(name = "cms_template")
@Getter
@Setter
public class Template extends Site implements ImagesOwner {

    /**
     * 浏览量
     */
    @Column(name = "scans")
    private int scans;

    /**
     * 点赞数量
     */
    @Column(name = "lauds")
    private int lauds;

    /**
     * 使用数
     */
    @Column(name="useNumber")
    private int useNumber;

    /**
     * 模板类型，eg: 汽车行业，服装行业...
     */
    @ManyToOne(cascade = CascadeType.PERSIST)
    private TemplateType templateType;


    @Override
    public String[] getImagePaths() {
        return new String[]{getLogoUri()};
    }

    @Override
    public void updateImage(int index, ResourceService resourceService, InputStream stream) throws IOException
            , IllegalArgumentException {
        if (getLogoUri() != null) {
            resourceService.deleteResource(getLogoUri());
        }
        setLogoUri(ImageHelper.storeAsImage("png", resourceService, stream));
    }
}
