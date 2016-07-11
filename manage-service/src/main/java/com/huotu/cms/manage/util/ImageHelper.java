/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.util;

import me.jiangcai.lib.resource.service.ResourceService;

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
     * 以指定格式保存一张图片
     *
     * @param type            类型 比如png,jpg
     * @param resourceService 资源服务
     * @param data            原数据
     * @return 资源path
     */
    public static final String storeAsImage(String type, ResourceService resourceService, InputStream data)
            throws IOException {
        BufferedImage image = ImageIO.read(data);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ImageIO.write(image, type, buffer);
        String path = UUID.randomUUID().toString() + "." + type;
        resourceService.uploadResource(path, new ByteArrayInputStream(buffer.toByteArray()));
        return path;
    }

}
