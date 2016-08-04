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
 * 资源的拥有者
 *
 * @author CJ
 */
public interface ResourcesOwner {

    /**
     * 如果缺失某一项资源,那么该值会为null
     *
     * @return 获取它的图片路径组, 永不为空
     */
    String[] getResourcePaths();

    /**
     * 直接更改资源路径,如果有必要需要在此处删除原资源
     *
     * @param paths           所有的新资源路径
     * @param resourceService 资源服务
     */
    default void updateResources(String[] paths, ResourceService resourceService) throws IOException {
        for (int i = 0; i < paths.length; i++) {
            updateResource(i, paths[i], resourceService);
        }
    }

    /**
     * 直接更改某一项资源路径,如果原资源存在则应该删除（看情况）
     *
     * @param index 索引
     * @param path  新的资源路径
     */
    void updateResource(int index, String path, ResourceService resourceService) throws IOException;

    /**
     * 更新所有资源
     *
     * @param resourceService 资源服务
     * @param paths           原资源在资源系统中的path,不可为空
     * @throws IOException 保存时出错
     */
    default void updateResources(ResourceService resourceService, String[] paths) throws IOException {
        for (int i = 0; i < paths.length; i++) {
            updateResource(i, resourceService, paths[i]);
        }
    }

    // 参考
    default void updateResource(ResourceService resourceService, InputStream[] streams) throws IOException
            , IllegalArgumentException {
        for (int i = 0; i < streams.length; i++) {
            updateResource(i, resourceService, streams[i]);
        }
    }

    /**
     * 更新其中一项资源
     *
     * @param index           资源索引,从0开始
     * @param resourceService 资源服务
     * @param path            原资源在资源系统中的path
     * @throws IOException 保存时出错
     */
    default void updateResource(int index, ResourceService resourceService, String path) throws IOException {
        try (InputStream stream = resourceService.getResource(path).getInputStream()) {
            updateResource(index, resourceService, stream);
        }
    }

    default void updateResource(int index, ResourceService resourceService, InputStream stream) throws IOException {
        String newPath = generateResourcePath(index, resourceService, stream);
        resourceService.uploadResource(newPath, stream);
        updateResource(index, newPath, resourceService);
    }

    /**
     * 产生一个资源路径
     *
     * @param index           资源索引
     * @param resourceService 资源服务
     * @param stream          即将新增的资源内容,可能不传的;属于参考参数
     * @return
     */
    String generateResourcePath(int index, ResourceService resourceService, InputStream stream);


}
