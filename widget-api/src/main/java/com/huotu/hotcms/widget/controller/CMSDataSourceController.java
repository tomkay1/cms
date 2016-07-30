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
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Gallery;
import com.huotu.hotcms.service.entity.GalleryItem;
import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.repository.GalleryRepository;
import com.huotu.hotcms.service.service.CategoryService;
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
    @Autowired
    private GalleryRepository galleryRepository;
    @Autowired
    private CategoryService categoryService;


    /**
     * 根据parent 的contentType 来决定查询的数据类型 {@link com.huotu.hotcms.widget.service.CMSDataSourceService}
     *
     * @param parentId 数据源id
     * @return json 返回当前parentId 的所有子级元素
     * 例如{status=200,message="Success",data=[...]},{status=404,message="fail",data=[]}
     */
    @RequestMapping(value = "/findGalleryItem/{parentId}", method = RequestMethod.GET)
    public ResponseEntity findGalleryItem(@PathVariable("parentId") Long parentId) {
        Gallery gallery = galleryRepository.findOne(parentId);
        if (gallery != null && gallery.getCategory().getContentType().equals(ContentType.Gallery)) {
            List<GalleryItem> data = cmsDataSourceService.findGalleryItem(parentId);
            return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/json")).body(data);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * @param parentId 数据源id
     * @return json 返回当前parentId 的所有子级元素
     * 例如{code=200,message="Success",data=[...]},{code=403,message="fail",data=[]}
     */
    @RequestMapping(value = "/findLink/{parentId}", method = RequestMethod.GET)
    public ResponseEntity findLink(@PathVariable("parentId") Long parentId) {
        Category category = categoryService.get(parentId);
        if (category != null && category.getContentType().equals(ContentType.Link)) {
            List<Link> data = cmsDataSourceService.findLink(parentId);
            return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/json")).body(data);
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * @param parentId 数据源id
     * @return json 返回当前parentId 的所有子级元素
     * 例如{code=200,message="Success",data=[...]},{code=403,message="fail",data=[]}
     */
    @RequestMapping(value = "/findChildrenArticleCategory/{parentId}", method = RequestMethod.GET)
    public ResponseEntity findChildrenArticleCategory(@PathVariable("parentId") Long parentId) {
        Category category = categoryService.get(parentId);
        if (category != null && category.getContentType().equals(ContentType.Article)) {
            String data = cmsDataSourceService.findChildrenArticleCategory(parentId);
            return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/json")).body(data);
        }
        return ResponseEntity.noContent().build();
    }

}
