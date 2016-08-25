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
import javax.persistence.Lob;
import javax.persistence.Table;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * 简单产品,由CMS系统自身维护的产品信息
 * 它的{@link #category 数据源}必然是一个{@link ProductCategory}
 *
 * @author CJ
 */
@Entity
@Table(name = "cms_product")
@Setter
@Getter
public class Product extends AbstractContent implements ImagesOwner {

    /**
     * 缩略图的path
     */
    @Column(length = 200)
    private String thumbPath;

    // 数据上应该是通过一个大文本（二进制也可以）保存
    // JSON 是一个很好的结构
    // {
    //   'fieldId':['abc']
    // }
    @Lob
    private String jsonData;

    @Override
    public Product copy() {
        return null;
    }

    @Override
    public int[] imageResourceIndexes() {
        return new int[]{0};
    }

    @Override
    public String[] getResourcePaths() {
        return new String[]{getThumbPath()};
    }

    @Override
    public void updateResource(int index, String path, ResourceService resourceService) throws IOException {
        if (getThumbPath() != null)
            resourceService.deleteResource(getThumbPath());
        setThumbPath(path);
    }

    @Override
    public String generateResourcePath(int index, ResourceService resourceService, InputStream stream) {
        return UUID.randomUUID().toString();
    }
}
