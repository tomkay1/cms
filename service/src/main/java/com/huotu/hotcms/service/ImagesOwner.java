/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.hotcms.service;

import com.huotu.hotcms.service.util.ImageHelper;
import me.jiangcai.lib.resource.service.ResourceService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * 这是一个拥有至少一个图片的角色
 * 可以配合{@link me.jiangcai.lib.resource.service.ResourceService}获取具体的图片,也可以更换图片（一个或者全部）
 * <p>图片拥有者必然是一个资源拥有者,只不过图片的索引跟全部资源的索引并不一致。但是图片的行为模式必然是被资源所有者共享的。</p>
 *
 * @author CJ
 */
public interface ImagesOwner extends ResourcesOwner {

    /**
     * 如果缺失某一张图片,那么该值会为null
     *
     * @return 获取它的图片路径组, 永不为空
     */
    default String[] getImagePaths() {
        String[] resources = getResourcePaths();
        // 我们需要知道哪些是图片
        int[] indexes = imageResourceIndexes();
        String[] images = new String[indexes.length];
        int i = 0;
        for (int index : indexes) {
            images[i++] = resources[index];
        }
        return images;
    }

    /**
     * 比如
     * <code>{0}</code>
     *
     * @return 图片资源在所有资源中的索引
     */
    int[] imageResourceIndexes();

    // 覆盖了原声明,可能会因为图片格式不正确 直接抛错
    default void updateResource(int index, ResourceService resourceService, InputStream stream, String originNameOrPath) throws IOException
            , IllegalArgumentException {
        if (Arrays.binarySearch(imageResourceIndexes(), index) >= 0) {
            // 是图片
            String imagePath = generateResourcePath(index, resourceService, stream) + "." + ImageHelper.fileExtensionName(originNameOrPath);
            ImageHelper.storeAsImage(resourceService, stream, imagePath);
            updateResource(index, imagePath, resourceService);
        } else {
            updateOtherResource(index, resourceService, stream, originNameOrPath);
        }
    }

    /**
     * 更新一个非图片资源
     *
     * @param index            资源索引
     * @param resourceService  资源服务
     * @param stream           新资源的数据流
     * @param originNameOrPath 原文件名或者原路径 必须携带扩展名
     * @throws IOException 保存时出错
     */
    default void updateOtherResource(int index, ResourceService resourceService, InputStream stream, String originNameOrPath) throws IOException {
        String newPath = generateResourcePath(index, resourceService, stream) + "." + ImageHelper.fileExtensionName(originNameOrPath);
        resourceService.uploadResource(newPath, stream);
        updateResource(index, newPath, resourceService);
    }


    /**
     * 更新所有图片
     *
     * @param resourceService 资源服务
     * @param imagePaths      原图片在资源系统中的path,不可为空
     * @throws IOException              保存时出错
     * @throws IllegalArgumentException 这个货或者某个货不是图片
     */
    default void updateImages(ResourceService resourceService, String[] imagePaths) throws IOException
            , IllegalArgumentException {
        for (int i = 0; i < imagePaths.length; i++) {
            updateImage(i, resourceService, imagePaths[i]);
        }
    }

    // 参考
//    default void updateImages(ResourceService resourceService, InputStream[] streams) throws IOException
//            , IllegalArgumentException {
//        for (int i = 0; i < streams.length; i++) {
//            updateImage(i, resourceService, streams[i], imagePath);
//        }
//    }

    /**
     * 更新其中一张图片
     *
     * @param imageIndex      图片索引,从0开始
     * @param resourceService 资源服务
     * @param imagePath       原图片在资源系统中的path
     * @throws IOException              保存时出错
     * @throws IllegalArgumentException path不是一张有效的图片
     */
    default void updateImage(int imageIndex, ResourceService resourceService, String imagePath) throws IOException
            , IllegalArgumentException {
        try (InputStream stream = resourceService.getResource(imagePath).getInputStream()) {
            updateImage(imageIndex, resourceService, stream, imagePath);
        }
    }

    /**
     * 更新图片
     *
     * @param imageIndex       图片索引,从0开始
     * @param resourceService  资源服务
     * @param stream           数据
     * @param originNameOrPath 原文件名或者原路径 必须携带扩展名
     * @throws IOException              保存时出错
     * @throws IllegalArgumentException 资源不是一张有效的图片
     */
    default void updateImage(int imageIndex, ResourceService resourceService, InputStream stream, String originNameOrPath) throws IOException
            , IllegalArgumentException {
        // 把imageIndex 换算为resourceIndex
        int index = imageResourceIndexes()[imageIndex];
        updateResource(index, resourceService, stream, originNameOrPath);
    }


}
