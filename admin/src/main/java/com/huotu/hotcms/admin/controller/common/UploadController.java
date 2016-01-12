package com.huotu.hotcms.admin.controller.common;

import com.huotu.hotcms.admin.common.StringUtil;
import com.huotu.hotcms.admin.service.StaticResourceService;
import com.huotu.hotcms.service.common.ConfigInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chendeyu on 2016/1/7.
 */
@Controller
@RequestMapping("/cms")
public class UploadController {

    @Autowired
    private StaticResourceService resourceServer;

    @Autowired
    private ConfigInfo configInfo;

    @RequestMapping(value = "/siteUpLoad", method = RequestMethod.POST)
    @ResponseBody
    public Map<Object, Object> siteUpLoad(String customerId, @RequestParam(value = "btnFile", required = false) MultipartFile files) {
        int result = 0;
        Map<Object, Object> responseData = new HashMap<Object, Object>();
        try {
            Date now = new Date();
            String fileName = files.getOriginalFilename();
            String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
            String[] img =configInfo.getResourcesSiteLogo().split("/");
            String path =img[0]+"/"+customerId+"/"+img[2]+"/"+ StringUtil.DateFormat(now, "yyyyMMddHHmmSS") + "." + prefix;
            URI uri = resourceServer.uploadResource(path, files.getInputStream());
            responseData.put("fileUrl", uri);
            responseData.put("fileUri", path);
            result = 1;
        } catch (Exception e) {
            responseData.put("msg", e.getMessage());
        }
        responseData.put("result", result);

        return responseData;
    }

    @RequestMapping(value = "/impUpLoad", method = RequestMethod.POST)
    @ResponseBody
    public Map<Object, Object> impUpLoad(String customerId, @RequestParam(value = "btnFile", required = false) MultipartFile files) {
        int result = 0;
        Map<Object, Object> responseData = new HashMap<Object, Object>();
        try {
            Date now = new Date();
            String fileName = files.getOriginalFilename();
            String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
            String[] img =configInfo.getResourcesImg().split("/");
            String path =img[0]+"/"+customerId+"/"+img[2]+"/"+ StringUtil.DateFormat(now, "yyyyMMddHHmmSS") + "." + prefix;
            URI uri = resourceServer.uploadResource(path, files.getInputStream());
            responseData.put("fileUrl", uri);
            responseData.put("fileUri", path);
            result = 1;
        } catch (Exception e) {
            responseData.put("msg", e.getMessage());
        }
        responseData.put("result", result);

        return responseData;
    }


}