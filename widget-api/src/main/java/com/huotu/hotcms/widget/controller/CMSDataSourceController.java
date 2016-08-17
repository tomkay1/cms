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
     * @param parentId 数据源id
     * @return json 返回当前parentId 的所有子级元素
     * 例如{code=200,message="Success",data=[...]},{code=403,message="fail",data=[]}
     */
    @RequestMapping(value = "/findLink/{parentId}", method = RequestMethod.GET)
    public ResponseEntity findLink(@PathVariable("parentId") Long parentId) {
        List<LinkModel> data = cmsDataSourceService.findLinkContent(parentId);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/json")).body(data);
    }


    /**
     * @param serial 数据源id
     * @return json 返回当前parentId 的所有子级元素
     * 例如{code=200,message="Success",data=[...]},{code=403,message="fail",data=[]}
     */
    @RequestMapping(value = "/findArticleContent/{serial}", method = RequestMethod.GET)
    public ResponseEntity findArticleContent(@PathVariable("serial") String serial) {
        List<BaseModel> data = cmsDataSourceService.findArticleContent(serial);
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



}
