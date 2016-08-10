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
import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Login;
import com.huotu.hotcms.service.model.ContentExtra;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author wenqi
 */
@Controller
@RequestMapping("/manage/link")
public class LinkController extends ContentManageController<Link, ContentExtra> {
    private static final Log log = LogFactory.getLog(LinkController.class);

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
    protected void prepareUpdateContext(Login login, Link entity, Link data, ContentExtra extra
            , RedirectAttributes attributes) throws RedirectException {
        entity.setLinkUrl(data.getLinkUrl());
    }

    @Override
    protected String openViewName() {
        return "/view/contents/linkOpen.html";
    }
}