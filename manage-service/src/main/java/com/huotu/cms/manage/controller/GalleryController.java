/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller;

import com.huotu.cms.manage.controller.support.ContentManageController;
import com.huotu.cms.manage.exception.RedirectException;
import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.entity.Gallery;
import com.huotu.hotcms.service.entity.GalleryItem;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Login;
import com.huotu.hotcms.service.model.ContentExtra;
import com.huotu.hotcms.service.repository.GalleryItemRepository;
import com.huotu.hotcms.service.repository.GalleryRepository;
import me.jiangcai.lib.resource.service.ResourceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 图库控制器
 */
@Controller
@RequestMapping("/manage/gallery")
public class GalleryController extends ContentManageController<Gallery, ContentExtra> {
    private static final Log log = LogFactory.getLog(GalleryController.class);

    @Autowired
    private GalleryRepository galleryRepository;
    @Autowired
    private GalleryItemRepository galleryItemRepository;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private ServletContext servletContext;

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/items/{uuid}", produces = "image/*")
    @Transactional(readOnly = true)
    public ResponseEntity getItems(@AuthenticationPrincipal Login login, @PathVariable("id") Long id
            , @PathVariable("uuid") Long uuid) throws IOException {
        GalleryItem item = galleryItemRepository.getOne(uuid);
        return ResponseEntity
                .ok()
                .contentType(item.toContentType())
                .body(StreamUtils.copyToByteArray(resourceService.getResource(item.getThumbUri()).getInputStream()));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/items", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public Object getItems(@AuthenticationPrincipal Login login, @PathVariable("id") Long id) {
        Gallery gallery = galleryRepository.getOne(id);
        if (!login.contentManageable(gallery))
            throw new AccessDeniedException("无法访问该资源");

        List<GalleryItem> galleryItemList = galleryItemRepository.findByGallery(gallery);
        if (galleryItemList.isEmpty())
            return Collections.emptyList();
        return galleryItemList.stream()
                .map((Function<GalleryItem, Map<String, Object>>) galleryItem -> {
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("name", galleryItem.getName());
                    data.put("uuid", String.valueOf(galleryItem.getId()));
                    try {
                        data.put("size", resourceService.getResource(galleryItem.getThumbUri()).contentLength());
                    } catch (IOException e) {
                        // 这个错误并不重要
                        log.warn("", e);
                    }
                    data.put("thumbnailUrl",
                            "/"
                                    + servletContext.getContextPath()
                                    + "manage/gallery/" + galleryItem.getGallery().getId()
                                    + "/items"
                                    + galleryItem.getId());
                    // thumbnailUrl 考虑到跨域策略,这里给出本域的一个地址
                    return data;
                }).collect(Collectors.toList());
    }

    @Override
    protected ContentType contentType() {
        return ContentType.Gallery;
    }

    @Override
    protected Gallery preparePersistContext(Login login, Site site, Gallery data, ContentExtra extra
            , RedirectAttributes attributes) throws RedirectException {
        return data;
    }

    @Override
    protected String indexViewName() {
        return "/view/contents/gallery.html";
    }

    @Override
    protected void prepareUpdateContext(Login login, Gallery entity, Gallery data, ContentExtra extra
            , RedirectAttributes attributes) throws RedirectException {
//        entity.setLinkUrl(data.getLinkUrl());
    }

    @Override
    protected String openViewName() {
        return "/view/contents/galleryOpen.html";
    }
}
