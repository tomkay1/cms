/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service;

import me.jiangcai.lib.resource.service.ResourceService;

import java.io.IOException;
import java.io.InputStream;

/**
 * 这是一个拥有至少一个图片的角色
 * 可以配合{@link me.jiangcai.lib.resource.service.ResourceService}获取具体的图片,也可以更换图片（一个或者全部）
 *
 * @author CJ
 */
public interface ImagesOwner {

    /**
     * 如果缺失某一张图片,那么该值会为null
     *
     * @return 获取它的图片路径组, 永不为空
     */
    String[] getImagePaths();

    /**
     * 更新所有图片
     *
     * @param resourceService 资源服务
     * @param paths           原图片在资源系统中的path,不可为空
     * @throws IOException              保存时出错
     * @throws IllegalArgumentException 这个货或者某个货不是图片
     */
    default void updateImages(ResourceService resourceService, String[] paths) throws IOException
            , IllegalArgumentException {
        for (int i = 0; i < paths.length; i++) {
            updateImage(i, resourceService, paths[i]);
        }
    }

    // 参考
    default void updateImages(ResourceService resourceService, InputStream[] streams) throws IOException
            , IllegalArgumentException {
        for (int i = 0; i < streams.length; i++) {
            updateImage(i, resourceService, streams[i]);
        }
    }

    /**
     * 更新其中一张图片
     *
     * @param index           图片索引,从0开始
     * @param resourceService 资源服务
     * @param path            原图片在资源系统中的path
     * @throws IOException              保存时出错
     * @throws IllegalArgumentException path不是一张有效的图片
     */
    default void updateImage(int index, ResourceService resourceService, String path) throws IOException
            , IllegalArgumentException {
        try (InputStream stream = resourceService.getResource(path).getInputStream()) {
            updateImage(index, resourceService, stream);
        }
    }

    void updateImage(int index, ResourceService resourceService, InputStream stream) throws IOException
            , IllegalArgumentException;


}
