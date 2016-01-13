package com.huotu.hotcms.admin.controller.common;

import com.huotu.hotcms.admin.common.StringUtil;
import com.huotu.hotcms.admin.service.StaticResourceService;
import com.huotu.hotcms.service.common.ConfigInfo;
import com.huotu.hotcms.service.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sun.misc.BASE64Decoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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

    static BASE64Decoder decoder = new sun.misc.BASE64Decoder();

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


    @RequestMapping(value="/kindeditorUpload",method = RequestMethod.POST)
    @ResponseBody
    public Result fileUploadUeImage(MultipartHttpServletRequest request) throws Exception {
        Result result=new Result();
        Date now = new Date();
        MultipartFile file=request.getFile("imgFile");
        String[] img =configInfo.getResourcesUeditor().split("/");
        //取得扩展名
        String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
        String path =img[0]+"/"+3447+"/"+img[2]+"/"+ StringUtil.DateFormat(now, "yyyyMMddHHmmSS") + "." + fileExt;

        URI uri =resourceServer.uploadResource(path, file.getInputStream());
        result.setError(0);
        result.setUrl(uri.toString());
        return result;
    }


    @RequestMapping("/ajaxEditorFileUpload")
    @ResponseBody
    public Result ajaxEditorFileUpload(String imgsrc) throws Exception {
        Result result = new Result();
        //去掉字符串前面多余的字符"data:image/png;base64,"，获得纯粹的二进制地址
        imgsrc = imgsrc.substring(22);
        try {
            //将String转换成InputStream流
            byte[] bytes1 = decoder.decodeBuffer(imgsrc);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
            String[] img =configInfo.getResourcesImg().split("/");
            Date now = new Date();
            String path =img[0]+"/"+3447+"/"+img[2]+"/"+ StringUtil.DateFormat(now, "yyyyMMddHHmmSS") + "." + "png";

            //上传至服务器
//            String fileName = StaticResourceService.RICHTEXT_UPLOAD + UUID.randomUUID().toString() + ".png";
            URI uri =resourceServer.uploadResource(path, bais);

            result.setStatus(0);
            result.setMessage(path);
            result.setUrl(uri.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}