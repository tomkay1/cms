/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.hotcms.service.util;

import me.jiangcai.lib.resource.service.ResourceService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author CJ
 */
public class ImageHelper {

    /**
     * @param file 上传文件
     * @return 获取上传文件的后缀名
     */
    public static String fileExtensionName(MultipartFile file) {
        if (!StringUtils.isEmpty(file.getContentType())) {
            return MediaType.parseMediaType(file.getContentType()).getSubtype();
        } else {
            return fileExtensionName(file.getOriginalFilename());
        }
    }

    /**
     * @param fileName 文件名
     * @return 获取文件名的后缀名
     */
    public static String fileExtensionName(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            return fileName.substring(index + 1);
        }
        throw new IllegalArgumentException("unknown fileExtensionName of " + fileName);
    }

    /**
     * 以指定格式保存一张图片
     *
     * @param type            类型 比如png,jpg
     * @param resourceService 资源服务
     * @param data            原数据
     * @return 资源path
     */
    public static String storeAsImage(String type, ResourceService resourceService, InputStream data)
            throws IOException {
        String path = UUID.randomUUID().toString() + "." + type;
        storeAsImage(type, resourceService, data, path);
        return path;
    }


    /**
     * 以原格式保存一张图片,并且保存到指定path
     *
     * @param resourceService 资源服务
     * @param data            原数据
     * @param path            资源系统的路径
     */
    public static void storeAsImage(ResourceService resourceService, InputStream data, String path) throws IOException {
        storeAsImage(null, resourceService, data, path);
    }

    /**
     * 以指定格式保存一张图片,并且保存到指定path
     *
     * @param type            类型 比如png,jpg 如果为null则原格式保存
     * @param resourceService 资源服务
     * @param data            原数据
     * @param path            资源系统的路径
     */
    public static void storeAsImage(String type, ResourceService resourceService, InputStream data, String path) throws IOException {
        try {
            byte[] originData = StreamUtils.copyToByteArray(data);
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(originData));
            if (!StringUtils.isEmpty(type)) {
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                ImageIO.write(image, type, buffer);
                resourceService.uploadResource(path, new ByteArrayInputStream(buffer.toByteArray()));
            } else
                resourceService.uploadResource(path, new ByteArrayInputStream(originData));
        } finally {
            //noinspection ThrowFromFinallyBlock
            data.close();
        }
    }

    public static void assertSame(Resource resource, Resource resource1) {
        try {
            BufferedImage image1 = ImageIO.read(resource.getInputStream());

            BufferedImage image2 = ImageIO.read(resource1.getInputStream());
            assert image1.getWidth() == image2.getWidth() && image1.getHeight() == image2.getHeight();
        } catch (IOException ex) {
            throw new AssertionError(ex);
        }
    }
}
