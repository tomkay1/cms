/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller;

import com.huotu.cms.manage.ContentManageTest;
import com.huotu.cms.manage.page.ArticlePage;
import com.huotu.hotcms.service.common.ArticleSource;
import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.entity.Site;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ArticleControllerTest extends ContentManageTest<Article> {

    public ArticleControllerTest() {
        super(ContentType.Article, ArticlePage.class);
    }

    @Override
    protected void normalRandom(Article value, Site site) {
        value.setAuthor(UUID.randomUUID().toString());
        value.setContent(UUID.randomUUID().toString());
        value.setType(UUID.randomUUID().toString());
        value.setArticleSource(ArticleSource.values()[random.nextInt(ArticleSource.values().length)]);
    }

    @Override
    protected void assertCreation(Article entity, Article data) {
        assertThat(entity.getArticleSource())
                .isEqualByComparingTo(data.getArticleSource());
        assertThat(entity.getAuthor())
                .isEqualTo(data.getAuthor());
        assertThat(entity.getContent())
                .isEqualTo(data.getContent());
//        assertThat(entity.getType())
        // 资源
    }
}
