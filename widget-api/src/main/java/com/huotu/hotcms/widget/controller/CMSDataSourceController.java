/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.controller;

import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.common.EnumUtils;
import com.huotu.hotcms.service.entity.AbstractContent;
import com.huotu.hotcms.service.entity.GalleryItem;
import com.huotu.hotcms.service.model.BaseModel;
import com.huotu.hotcms.service.model.LinkModel;
import com.huotu.hotcms.service.model.widget.VideoModel;
import com.huotu.hotcms.widget.service.CMSDataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")).body(data);
    }


    /**
     * @param serial 数据源id
     * @return json 返回当前parentId 的所有子级元素
     * 例如{code=200,message="Success",data=[...]},{code=403,message="fail",data=[]}
     */
    @RequestMapping(value = "/findArticleContent/{serial}", method = RequestMethod.GET)
    public ResponseEntity findArticleContent(@PathVariable("serial") String serial) {
        List<BaseModel> data = cmsDataSourceService.findArticleContent(serial, 0);
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
     * 查询当前站点下数据内容列表
     *
     * @param search      搜索条件
     * @param contentType 内容类型
     * @param draw        页码
     * @param length      每页显示数量
     * @return json 返回当前parentId 的所有子级元素
     */
    @RequestMapping(value = "/findContentType", method = RequestMethod.GET)
    public ResponseEntity findContentType(ContentType contentType, int draw, int length
            , @RequestParam(value = "search[value]") String search) throws IOException {
        // 数据源列表
        Pageable pageable = new PageRequest(draw - 1, length, new Sort(Sort.Direction.ASC, "id"));
        ContentType type = EnumUtils.valueOf(ContentType.class, contentType);
        Page<? extends AbstractContent> page = cmsDataSourceService.findContent(type, pageable, search);
        Map<String, Object> map = new HashMap<>();
        map.put("pageNum", draw); //当前页索引
        map.put("pageSize", length);
        map.put("totalPages", page.getTotalPages()); //共多少页
        map.put("totalElements", page.getTotalElements()); //元素的总数量
        map.put("data", page.getContent().stream().map((Function<AbstractContent, Map<String, Object>>) content -> {
            HashMap<String, Object> data = new HashMap<>();
            data.put("id", content.getId());
            data.put("name", content.getTitle());
            data.put("serial", content.getSerial());
            data.put("date", content.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            if (content instanceof GalleryItem) {
                data.put("thumpUri", ((GalleryItem) content).getThumbUri());
                data.put("size", ((GalleryItem) content).getSize());
            } else {
                data.put("thumpUri", "");
                data.put("size", "");
            }
            return data;
        }).collect(Collectors.toList()));
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")).body(map);
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
