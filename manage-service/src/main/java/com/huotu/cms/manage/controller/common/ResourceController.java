/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller.common;

import me.jiangcai.lib.resource.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * 资源控制器
 * 上传文件,只接受通过Ajax方式上传。
 * 上传成功之后将给出302响应,地址为资源最终地址;响应正文将携带有资源path
 * 这个资源会保存在特殊文件夹中,并在24小时内删除,所以path应该算是临时path。
 *
 * @author CJ
 */
@Controller
@RequestMapping("/manage/upload")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    public ResponseEntity<String> upload(MultipartFile file) throws IOException {
        String path = "tmp/" + UUID.randomUUID().toString();
        try (InputStream inputStream = file.getInputStream()) {
            resourceService.uploadResource(path, inputStream);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity<>(path, httpHeaders, HttpStatus.FOUND);
        }
    }

}
