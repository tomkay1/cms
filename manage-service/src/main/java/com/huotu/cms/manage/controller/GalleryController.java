/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huotu.cms.manage.controller.support.ContentManageController;
import com.huotu.cms.manage.exception.RedirectException;
import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.entity.Gallery;
import com.huotu.hotcms.service.entity.GalleryItem;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Login;
import com.huotu.hotcms.service.model.ContentExtra;
import com.huotu.hotcms.service.model.SiteAndSerial;
import com.huotu.hotcms.service.repository.GalleryItemRepository;
import com.huotu.hotcms.service.repository.GalleryRepository;
import com.huotu.hotcms.service.service.CommonService;
import com.huotu.hotcms.service.util.ImageHelper;
import me.jiangcai.lib.resource.service.ResourceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 图库控制器
 */
@Controller
@RequestMapping("/manage/gallery")
public class GalleryController extends ContentManageController<Gallery, ContentExtra> {
    private static final Log log = LogFactory.getLog(GalleryController.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private GalleryRepository galleryRepository;
    @Autowired
    private GalleryItemRepository galleryItemRepository;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private ServletContext servletContext;
    @Autowired
    private CommonService commonService;

    @Override
    protected void prepareRemove(Login login, Gallery entity) throws RedirectException {
        super.prepareRemove(login, entity);

        List<GalleryItem> galleryItemList = galleryItemRepository.findByGallery(entity);
        for (GalleryItem item : galleryItemList) {
            galleryItemRepository.delete(item);
            try {
                commonService.deleteResource(item);
            } catch (IOException e) {
                throw new RedirectException(rootUri(), e);
            }
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}/items/{uuid}")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@AuthenticationPrincipal Login login, @PathVariable("id") Long id
            , @PathVariable("uuid") Long uuid) throws IOException {
        GalleryItem item = galleryItemRepository.getOne(uuid);

        if (!login.contentManageable(item.getGallery()))
            throw new AccessDeniedException("无法访问该资源");

        galleryItemRepository.delete(item);
        commonService.deleteResource(item);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/items/{uuid}")
    @Transactional(readOnly = true)
    public ResponseEntity getItem(@AuthenticationPrincipal Login login, @PathVariable("id") Long id
            , @PathVariable("uuid") Long uuid) throws IOException {
        GalleryItem item = galleryItemRepository.getOne(uuid);
        return ResponseEntity
                .ok()
                .contentType(item.toContentType())
                .body(StreamUtils.copyToByteArray(resourceService.getResource(item.getThumbUri()).getInputStream()));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{id}/items", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @ResponseBody
    public Object postItems(@AuthenticationPrincipal Login login, @PathVariable("id") Long id
            , MultipartFile qqfile, @RequestParam String qqfilename) throws IOException {
        Gallery gallery = galleryRepository.getOne(id);
        if (!login.contentManageable(gallery))
            throw new AccessDeniedException("无法访问该资源");
        if (StringUtils.isEmpty(qqfilename))
            throw new IllegalArgumentException("qqfilename is required.");
        if (qqfile.isEmpty())
            throw new IllegalArgumentException("qqfile is required.");

        GalleryItem item = new GalleryItem();
        item.setGallery(gallery);
        item.setCreateTime(LocalDateTime.now());
        item.setSerial(UUID.randomUUID().toString());
        item.setTitle(qqfilename);
        String path = "gallery/" + id + "/" + UUID.randomUUID().toString() + ".png";
        ImageHelper.storeAsImage("png", resourceService, qqfile.getInputStream(), path);
        item.setThumbUri(path);

        item = galleryItemRepository.saveAndFlush(item);

        HashMap<String, Object> body = new HashMap<>();
        body.put("success", true);
        body.put("newUuid", String.valueOf(item.getId()));
        return body;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/items2", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public ResponseEntity<String> getItems(@AuthenticationPrincipal Login login, @PathVariable("id") SiteAndSerial id)
            throws JsonProcessingException {
        Gallery gallery = galleryRepository.findByCategory_SiteAndSerial(id.getSite(), id.getSerial());
        return getItems(login, gallery.getId());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/items", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public ResponseEntity<String> getItems(@AuthenticationPrincipal Login login, @PathVariable("id") Long id) throws JsonProcessingException {
        Gallery gallery = galleryRepository.getOne(id);
        if (!login.contentManageable(gallery))
            throw new AccessDeniedException("无法访问该资源");

        List<GalleryItem> galleryItemList = galleryItemRepository.findByGallery(gallery);
        List<Map<String, Object>> list = galleryItemList.stream()
                .map((Function<GalleryItem, Map<String, Object>>) galleryItem -> {
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("serial", galleryItem.getSerial());
                    data.put("name", galleryItem.getTitle());
                    data.put("uuid", String.valueOf(galleryItem.getId()));
                    try {
                        data.put("size", resourceService.getResource(galleryItem.getThumbUri()).contentLength());
                    } catch (IOException e) {
                        // 这个错误并不重要
                        log.warn("", e);
                    }
//                    data.put("thumbnailUrl",
//                            "/"
//                                    + servletContext.getContextPath()
//                                    + "manage/gallery/" + galleryItem.getGallery().getId()
//                                    + "/items/"
//                                    + galleryItem.getId());
                    try {
                        data.put("thumbnailUrl",
                                resourceService.getResource(galleryItem.getThumbUri()).httpUrl().toString());
                    } catch (IOException ignored) {
                        // never
                        log.fatal("", ignored);
                    }

                    // thumbnailUrl 考虑到跨域策略,这里给出本域的一个地址
                    return data;
                }).collect(Collectors.toList());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(objectMapper.writeValueAsString(list));
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

    @Override
    protected String resourceName(Locale locale) {
        return "图库";
    }
}
