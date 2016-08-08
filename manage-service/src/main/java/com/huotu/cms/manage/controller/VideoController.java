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
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.Video;
import com.huotu.hotcms.service.entity.login.Login;
import com.huotu.hotcms.service.model.ContentExtra;
import me.jiangcai.lib.resource.service.ResourceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author wenqi
 */
@Controller
@RequestMapping("/manage/video")
public class VideoController extends ContentManageController<Video,ContentExtra> {
    private static final Log log = LogFactory.getLog(SiteController.class);

    @Autowired
    private ResourceService resourceService;

    @Override
    protected ContentType contentType() {
        return ContentType.Video;
    }

    @Override
    protected Video preparePersistContext(Login login, Site site, Video data, ContentExtra extra, RedirectAttributes attributes) throws RedirectException {
        return data;
    }

    @Override
    protected String indexViewName() {
        return "/view/contents/video.html";
    }

    @Override
    protected void prepareUpdate(Login login, Video entity, Video data, ContentExtra extra, RedirectAttributes attributes) throws RedirectException {
        entity.setTitle(data.getTitle());
        entity.setOutLinkUrl(data.getOutLinkUrl());
        entity.setDescription(data.getDescription());
        entity.setUpdateTime(LocalDateTime.now());
        try {
            commonService.uploadTempImageToOwner(entity, extra.getTempPath());
        } catch (IOException e) {
            log.warn("图片转存异常："+e.getMessage());
        }
    }

    @Override
    protected String openViewName() {
        return "/view/contents/videoOpen.html";
    }
}
