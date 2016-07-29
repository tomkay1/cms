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
import com.huotu.cms.manage.util.web.CookieUser;
import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Login;
import com.huotu.hotcms.service.model.ContentExtra;
import com.huotu.hotcms.service.model.LinkCategory;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.repository.LinkRepository;
import com.huotu.hotcms.service.service.LinkService;
import com.huotu.hotcms.service.util.PageData;
import com.huotu.hotcms.service.util.ResultOptionEnum;
import com.huotu.hotcms.service.util.ResultView;
import me.jiangcai.lib.resource.service.ResourceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author wenqi
 */
@Controller
@RequestMapping("/manage/link")
public class LinkController extends ContentManageController<Link,ContentExtra> {
    private static final Log log = LogFactory.getLog(LinkController.class);

    @Autowired
    private ResourceService resourceService;

    @Override
    protected ContentType contentType() {
        return ContentType.Link;
    }

    @Override
    protected Link preparePersistContext(Login login, Site site, Link data, ContentExtra extra
            , RedirectAttributes attributes) throws RedirectException {
        return data;
    }

    @Override
    protected String indexViewName() {
        return "/view/contents/link.html";
    }

    @Override
    protected void prepareUpdate(Login login, Link entity, Link data, ContentExtra extra
            , RedirectAttributes attributes) throws RedirectException {
        entity.setTitle(data.getTitle());
        entity.setDescription(data.getDescription());
        entity.setUpdateTime(LocalDateTime.now());
        entity.setLinkUrl(data.getLinkUrl());
        try {
            uploadTempImageToOwner(entity,extra.getTempPath());
        } catch (IOException e) {
            log.warn("图片转存异常："+e.getMessage());
        }
    }

    @Override
    protected String openViewName() {
        return "/view/contents/linkOpen.html";
    }
}