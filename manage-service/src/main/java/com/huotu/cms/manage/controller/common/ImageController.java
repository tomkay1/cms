/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.cms.manage.controller.common;

import com.huotu.hotcms.service.util.ImageHelper;
import lombok.SneakyThrows;
import me.jiangcai.lib.ee.ServletUtils;
import me.jiangcai.lib.resource.Resource;
import me.jiangcai.lib.resource.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.Base64;

/**
 * 图片控制器
 *
 * @author CJ
 */
@Controller
public class ImageController {

    @Autowired
    private ResourceService resourceService;

    @SneakyThrows(UnsupportedEncodingException.class)
    public String forThumbnailForWidth(HttpServletRequest request, String path, int width) {
        return ServletUtils.buildContextUrl(request).append("/thumbnailImage/")
                .append(Base64.getEncoder().encodeToString(path.getBytes("UTF-8")))
                .append("/width/").append(width).toString();
    }

    @SneakyThrows(UnsupportedEncodingException.class)
    public String forThumbnailForHeight(HttpServletRequest request, String path, int height) {
        return ServletUtils.buildContextUrl(request).append("/thumbnailImage/")
                .append(Base64.getEncoder().encodeToString(path.getBytes("UTF-8")))
                .append("/height/").append(height).toString();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/thumbnailImage/{path}/width/{width}")
    public ResponseEntity<?> thumbnailAsWidth(@PathVariable("path") String path
            , @PathVariable("width") int width) throws IOException, URISyntaxException {
        return thumbnail(path, width, -1);
    }

    private ResponseEntity<?> thumbnail(String originPath, int width, int height) throws IOException, URISyntaxException {
        // 确定格式
        String path = new String(Base64.getDecoder().decode(originPath), "UTF-8");
        String type = ImageHelper.fileExtensionName(path);
        final Resource resource = resourceService.getResource(path);
        BufferedImage originImage = ImageIO.read(resource.getInputStream());
        // 目标rate
        double rate;
        if (width != -1) {
            rate = (double) width / (double) originImage.getWidth();
        } else {
            rate = (double) height / (double) originImage.getHeight();
        }
        if (rate >= 1) {
            // 直接渲染原资源
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(resource.httpUrl().toURI())
                    .build();
//            return ResponseEntity.ok()
//                    .contentType(MediaType.parseMediaType(MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(type)))
//                    .body(resourceService.getResource(path).getInputStream());
        }

        int targetWidth = (int) ((double) originImage.getWidth() * rate);
        int targetHeight = (int) ((double) originImage.getHeight() * rate);

        BufferedImage image = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        image.getGraphics().drawImage(originImage, 0, 0, targetWidth, targetHeight, null);
        // 渲染回数据
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ImageIO.write(image, type, buffer);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("image/" + type))
                .body(buffer.toByteArray());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/thumbnailImage/{path}/height/{height}")
    public ResponseEntity<?> thumbnailAsHeight(@PathVariable("path") String path
            , @PathVariable("height") int height) throws IOException, URISyntaxException {
        return thumbnail(path, -1, height);
    }

}
