/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller.common;

import com.huotu.hotcms.service.entity.login.Login;
import com.huotu.hotcms.service.util.ResultOptionEnum;
import com.huotu.hotcms.widget.CMSContext;
import me.jiangcai.lib.resource.service.ResourceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/manage/cms")
public class UploadController {

    private static final Log log = LogFactory.getLog(UploadController.class);

    @Autowired
    private ResourceService resourceService;


    /**
     * 上传永久资源
     *
     * @param login
     * @param file
     * @return
     */
    @RequestMapping(value = "/resourceUpload", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> resourceUpload(@AuthenticationPrincipal Login login
            , @RequestParam(value = "file", required = false) MultipartFile file) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (login.siteManageable(CMSContext.RequestContext().getSite())) {
                String fileName = file.getOriginalFilename();
                String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                String path = "resource/" + CMSContext.RequestContext().getSite().getSiteId() + "/" + UUID.randomUUID().toString() + "." + suffix;
                URI uri = resourceService.uploadResource(path, file.getInputStream()).httpUrl().toURI();
                map.put("fileUri", uri);
                map.put("path", path);
                map.put("code", ResultOptionEnum.OK.getCode());
                map.put("msg", ResultOptionEnum.RESOURCE_PERMISSION_ERROR.getValue());
            } else {
                throw new IllegalStateException("上传失败，没有权限");
            }
        } catch (Exception e) {
            log.error("", e);
            map.put("code", ResultOptionEnum.RESOURCE_PERMISSION_ERROR.getCode());
            map.put("msg", e.getMessage());
        }
        return map;
    }


    /**
     * 删除永久资源
     *
     * @param login
     * @param path  一个request参数
     */
    @RequestMapping(value = "/deleteResource", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteResource(@AuthenticationPrincipal Login login, @RequestParam("path") String path)
            throws IOException {
        try {
            if (login.siteManageable(CMSContext.RequestContext().getSite())) {
                resourceService.deleteResource(path);
            } else {
                throw new IllegalStateException("删除失败，没有权限");
            }
        } catch (IOException e) {
            throw new IllegalStateException("删除失败，没有权限", e);
        }
    }

}