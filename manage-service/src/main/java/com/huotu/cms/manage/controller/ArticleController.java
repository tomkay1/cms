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
import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Login;
import com.huotu.hotcms.service.model.ContentExtra;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

/**
 * 管理文章内容
 *
 * @author CJ
 */
@Controller
@RequestMapping("/manage/article")
public class ArticleController extends ContentManageController<Article, ContentExtra> {
    private static final Log log = LogFactory.getLog(ArticleController.class);

    @Override
    protected String indexViewName() {
        return "/view/contents/article.html";
    }

    @Override
    protected void prepareUpdateContext(Login login, Article entity, Article data, ContentExtra extra
            , RedirectAttributes attributes) throws RedirectException {
        entity.setContent(data.getContent());
        entity.setAuthor(data.getAuthor());
        entity.setArticleSource(data.getArticleSource());
    }

    @Override
    protected String openViewName() {
        return "/view/contents/articleOpen.html";
    }

    @Override
    protected ContentType contentType() {
        return ContentType.Article;
    }

    @Override
    protected Article preparePersistContext(Login login, Site site, Article data, ContentExtra extra
            , RedirectAttributes attributes) throws RedirectException {
        return data;
    }

    @Override
    protected String resourceName(Locale locale) {
        return "文章";
    }
}