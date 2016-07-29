/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller.common;

import me.jiangcai.lib.resource.Resource;
import me.jiangcai.lib.resource.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.UUID;

/**
 * 资源控制器
 * 上传文件,只接受通过Ajax方式上传。
 * 上传成功之后将给出200响应,地址为资源最终地址;响应正文将携带有资源path
 * 这个资源会保存在特殊文件夹中,并在24小时内删除,所以path应该算是临时path。
 *
 * @author CJ
 */
@Controller
@RequestMapping("/manage/upload")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    public String uploadTempResource(InputStream data) throws IOException {
        String path = "tmp/" + UUID.randomUUID().toString();
        resourceService.uploadResource(path, data);
        return path;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> upload(MultipartFile file) throws IOException, URISyntaxException {
        try (InputStream inputStream = file.getInputStream()) {
            String path = uploadTempResource(inputStream);
            Resource resource = resourceService.getResource(path);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.TEXT_PLAIN);
            httpHeaders.setLocation(resource.httpUrl().toURI());
            return new ResponseEntity<>(path, httpHeaders, HttpStatus.OK);
        }
    }


    /**
     * 为fine-uploader特地准备的上传控制器
     *
     * @param file
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    @RequestMapping(value = "/fine", method = RequestMethod.POST)
    @ResponseBody
    public Object fineUpload(MultipartFile file) {
        try {
            try (InputStream inputStream = file.getInputStream()) {
                String path = uploadTempResource(inputStream);
                HashMap<String, Object> body = new HashMap<>();
                body.put("success", true);
                body.put("newUuid", path);
                return body;
            }
        } catch (Exception ex) {
            HashMap<String, Object> body = new HashMap<>();
            body.put("success", false);
            body.put("error", ex.getLocalizedMessage());
            return body;
        }

    }

}
