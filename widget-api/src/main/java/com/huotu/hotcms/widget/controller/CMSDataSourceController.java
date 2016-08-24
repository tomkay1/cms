/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.controller;

import com.huotu.hotcms.service.model.BaseModel;
import com.huotu.hotcms.service.model.DataModel;
import com.huotu.hotcms.service.model.LinkModel;
import com.huotu.hotcms.service.model.widget.VideoModel;
import com.huotu.hotcms.widget.service.CMSDataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

/**
 * 查询数据源接口
 * Created by lhx on 2016/7/26.
 */
@Controller
@RequestMapping("/dataSource")
public class CMSDataSourceController {

    @Autowired
    private CMSDataSourceService cmsDataSourceService;
    /**
     * @param serial
     * @return json 返回当前parentId 的所有子级元素
     * 例如{code=200,message="Success",data=[...]},{code=403,message="fail",data=[]}
     */
    @RequestMapping(value = "/findLink/{serial}", method = RequestMethod.GET)
    public ResponseEntity findLink(@PathVariable("serial") String serial) {
        List<LinkModel> data = cmsDataSourceService.findLinkContent(serial);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/json")).body(data);
    }


    /**
     * @param serial 数据源id
     * @return json 返回当前parentId 的所有子级元素
     * 例如{code=200,message="Success",data=[...]},{code=403,message="fail",data=[]}
     */
    @RequestMapping(value = "/findArticleContent/{serial}", method = RequestMethod.GET)
    public ResponseEntity findArticleContent(@PathVariable("serial") String serial) {
        List<BaseModel> data = cmsDataSourceService.findArticleContent(serial, );
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/json")).body(data);
    }

    /**
     * @param serial 数据源id
     * @return json 返回当前parentId 的所有子级元素
     * 例如{code=200,message="Success",data=[...]},{code=403,message="fail",data=[]}
     */
    @RequestMapping(value = "/findVideoContent/{serial}", method = RequestMethod.GET)
    public ResponseEntity findVideoContent(@PathVariable("serial") String serial) {
        List<VideoModel> data = cmsDataSourceService.findVideoContent(serial);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/json")).body(data);
    }


    /**
     * Json：contentType(0："文章", 1： "链接", 2： "视频", 3： "公告", 4,："图片", 5： "下载"，6：”页面”)
     * pageNum页号
     * pageSize每页显示数量
     * pageId页面id
     *
     * @return json 返回当前parentId 的所有子级元素
     * 例如{code=200,message="Success",data=[...]},{code=403,message="fail",data=[]}
     */
    @RequestMapping(value = "/findContentType", method = RequestMethod.GET)
    public ResponseEntity findContentType(Long contentType, Integer draw, Integer length, Long pageId
            , @RequestParam(value = "search[value]") String search) throws IOException {
        DataModel dataModel = cmsDataSourceService.findContentType(contentType, draw, length, pageId, search);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/json")).body(dataModel);
    }

//    /**
//     * @param galleryId 内容类型{@link com.huotu.hotcms.service.entity.Gallery#id }
//     * @param pageNum     当前页号
//     * @param pageSize    页面数量
//     * @return json 返回当前parentId 的所有子级元素
//     * 例如{code=200,message="Success",data=[...]},{code=403,message="fail",data=[]}
//     */
//    @RequestMapping(value = "/findGalleryItemList/{galleryId}/{pageNum}/{pageSize}", method = RequestMethod.GET)
//    public ResponseEntity findGalleryItemList(@PathVariable("galleryId") Long galleryId
//            ,@PathVariable("pageNum") int pageNum,@PathVariable("pageSize") int pageSize) {
//
//        return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/json")).body(null);
//    }



}
