package com.huotu.hotcms.admin.controller.common;

import com.huotu.hotcms.admin.common.StringUtil;
import com.huotu.hotcms.service.common.ConfigInfo;
import com.huotu.hotcms.service.entity.WidgetMains;
import com.huotu.hotcms.service.model.Result;
import com.huotu.hotcms.service.service.RedisService;
import com.huotu.hotcms.service.service.WidgetService;
import com.huotu.hotcms.service.util.HttpUtils;
import com.huotu.hotcms.service.util.ResultOptionEnum;
import com.huotu.hotcms.service.util.ResultView;
import com.huotu.hotcms.service.widget.service.StaticResourceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sun.misc.BASE64Decoder;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chendeyu on 2016/1/7.
 */
@Controller
@RequestMapping("/cms")
public class UploadController {
    private static final Log log = LogFactory.getLog(UploadController.class);
    @Autowired
    private StaticResourceService resourceServer;
    @Autowired
    private WidgetService widgetService;

    static BASE64Decoder decoder = new sun.misc.BASE64Decoder();

    @Autowired
    private ConfigInfo configInfo;

    @RequestMapping(value = "/siteUpLoad", method = RequestMethod.POST)
    @ResponseBody
    public ResultView siteUpLoad(Integer customerId, @RequestParam(value = "btnFile", required = false) MultipartFile files) {
        ResultView resultView = null;
        try {
            Date now = new Date();
            String fileName = files.getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            if("jpg, jpeg,png,gif,bmp".contains(suffix))
            {
                String path=configInfo.getResourcesSiteLogo(customerId)+"/"+StringUtil.DateFormat(now, "yyyyMMddHHmmSS") + "." + suffix;
                URI uri = resourceServer.uploadResource(path, files.getInputStream());
                Map<String,Object> map= new HashMap<String, Object>();
                map.put("fileUrl", uri);
                map.put("fileUri", path);
                resultView = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), map);
            }else{
                resultView = new ResultView(ResultOptionEnum.FILE_FORMATTER_ERROR.getCode(), ResultOptionEnum.FILE_FORMATTER_ERROR.getValue(), null);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            resultView = new ResultView(ResultOptionEnum.SERVERFAILE.getCode(),e.getMessage(), null);
        }
        return resultView;
    }

    @RequestMapping(value = "/imgUpLoad", method = RequestMethod.POST)
    @ResponseBody
    public ResultView imgUpLoad(Integer customerId, @RequestParam(value = "btnFile", required = false) MultipartFile files) {
        ResultView resultView = null;
        try {
            Date now = new Date();
            String fileName = files.getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            if("jpg, jpeg,png,gif,bmp".contains(suffix)){
            String path=configInfo.getResourcesImg(customerId)+"/"+StringUtil.DateFormat(now, "yyyyMMddHHmmSS") + "." + suffix;
                URI uri = resourceServer.uploadResource(path, files.getInputStream());
                BufferedImage sourceImg = javax.imageio.ImageIO.read(files.getInputStream());
                Map<String,Object> map= new HashMap<String, Object>();
                map.put("fileUrl", uri);
                map.put("fileUri", path);
                map.put("wide", sourceImg.getWidth());
                map.put("height", sourceImg.getHeight());
                resultView = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), map);
            }else{
                resultView = new ResultView(ResultOptionEnum.FILE_FORMATTER_ERROR.getCode(), ResultOptionEnum.FILE_FORMATTER_ERROR.getValue(), null);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            resultView = new ResultView(ResultOptionEnum.SERVERFAILE.getCode(),e.getMessage(), null);
        }
        return resultView;
    }

    @RequestMapping(value = "/widgetUpLoad", method = RequestMethod.POST)
    @ResponseBody
    public ResultView widgetUpLoad(Long id, @RequestParam(value = "btnFile1", required = false) MultipartFile files) {
        ResultView resultView = null;
        try {
            Date now = new Date();
            String fileName = files.getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            if("html".contains(suffix)){
                Long widgetId = Long.valueOf(id);
                String path=configInfo.getResourcesWidget()+"/template_"+widgetId + "." + suffix;
                URI uri = resourceServer.uploadResource(path, files.getInputStream());
                String content = HttpUtils.getHtmlByUrl(uri.toURL());
                Map<String,Object> map= new HashMap<String, Object>();
                WidgetMains widgetMains = widgetService.findWidgetMainsById(widgetId);//修改
                widgetMains.setResourceUri(path);
                widgetService.saveWidgetMains(widgetMains);
                map.put("fileContent",content);
                map.put("fileUri", uri);
                map.put("fileUrl", path);
                resultView = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), map);
            }else{
                resultView = new ResultView(ResultOptionEnum.FILE_FORMATTER_ERROR.getCode(), ResultOptionEnum.FILE_FORMATTER_ERROR.getValue(), null);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            resultView = new ResultView(ResultOptionEnum.SERVERFAILE.getCode(),e.getMessage(), null);
        }
        return resultView;
    }

    @RequestMapping(value = "/saveWidget", method = RequestMethod.POST)
    @ResponseBody
    public ResultView saveWidget(Long id,String content,String path) throws Exception {
        if(id==null) {
            throw new Exception("id 不能为空");
        }
        ResultView resultView = null;
        String uri = resourceServer.getWidgetResource(path).toString();
        File file=new File(uri);
        try {
            WidgetMains widgetMains = widgetService.findWidgetMainsById(id);
            widgetMains.setResourceUri(path);
            widgetService.saveWidgetMains(widgetMains);
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(content);
            bw.close();
            resultView = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
        } catch (Exception e) {
            log.error(e.getMessage());
            resultView = new ResultView(ResultOptionEnum.SERVERFAILE.getCode(),e.getMessage(), null);
        }
        return resultView;
    }



    @RequestMapping(value = "/downloadUpLoad", method = RequestMethod.POST)
    @ResponseBody
    public ResultView downloadUpLoad(Integer customerId, @RequestParam(value = "btnFile", required = false) MultipartFile files) {
        ResultView resultView = null;
        try {
            Date now = new Date();
            String fileName = files.getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            if("txt,zip,jar,docx,doc,xlsx".contains(suffix)){
                String path=configInfo.getResourcesDownload(customerId)+"/"+StringUtil.DateFormat(now, "yyyyMMddHHmmSS") + "." + suffix;
                URI uri = resourceServer.uploadResource(path, files.getInputStream());
                Map<String,Object> map= new HashMap<String, Object>();
                map.put("fileUrl", uri);
                map.put("fileUri", path);
                resultView = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), map);
            }else{
                resultView = new ResultView(ResultOptionEnum.FILE_FORMATTER_ERROR.getCode(), ResultOptionEnum.FILE_FORMATTER_ERROR.getValue(), null);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            resultView = new ResultView(ResultOptionEnum.SERVERFAILE.getCode(),e.getMessage(), null);
        }
        return resultView;
    }


    @RequestMapping(value="/kindeditorUpload",method = RequestMethod.POST)
    @ResponseBody
    public Result fileUploadUeImage(Integer customerId,MultipartHttpServletRequest multipartHttpServletRequest) throws Exception {
        Result result=new Result();
        Date now = new Date();
        MultipartFile file=multipartHttpServletRequest.getFile("imgFile");
//        String[] img =configInfo.getResourcesUeditor().split("/");
        //取得扩展名
        String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
//        String path =img[0]+"/"+3447+"/"+img[2]+"/"+ StringUtil.DateFormat(now, "yyyyMMddHHmmSS") + "." + fileExt;

        String path=configInfo.getResourcesUeditor(customerId)+"/"+StringUtil.DateFormat(now, "yyyyMMddHHmmSS") + "." + fileExt;

        URI uri = resourceServer.uploadResource(path, file.getInputStream());
        result.setError(0);
        result.setUrl(uri.toString());
        return result;
    }


    @RequestMapping("/ajaxEditorFileUpload")
    @ResponseBody
    public Result ajaxEditorFileUpload(Integer customerId,String imgsrc) throws Exception {
        Result result = new Result();
        //去掉字符串前面多余的字符"data:image/png;base64,"，获得纯粹的二进制地址
        imgsrc = imgsrc.substring(22);
        try {
            //将String转换成InputStream流
            byte[] bytes1 = decoder.decodeBuffer(imgsrc);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
//            String[] img =configInfo.getResourcesImg().split("/");
//            Date now = new Date();
//            String path =img[0]+"/"+3447+"/"+img[2]+"/"+ StringUtil.DateFormat(now, "yyyyMMddHHmmSS") + "." + "png";
            Date now = new Date();
//            String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
            String path=configInfo.getResourcesUeditor(customerId)+"/"+StringUtil.DateFormat(now, "yyyyMMddHHmmSS") + ".png";
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