/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.page;

import com.huotu.cms.manage.SiteManageTest;
import com.huotu.cms.manage.controller.support.CRUDHelper;
import com.huotu.cms.manage.controller.support.CRUDTest;
import com.huotu.cms.manage.page.support.AbstractCRUDPage;
import com.huotu.hotcms.service.common.ArticleSource;
import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.widget.entity.PageInfo;
import org.junit.Test;

import java.util.Collection;
import java.util.UUID;
import java.util.function.BiConsumer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by wenqi on 2016/7/26.
 */
public class ArticleFlowTest extends SiteManageTest {

    @Test
    public void flow() throws Exception {
        Site site = loginAsSite();

        ManageMainPage manageMainPage = initPage(ManageMainPage.class);

        CRUDHelper.flow(manageMainPage.toPage(ArticlePage.class), new CRUDTest<Article>() {

            @Override
            public Collection<Article> list() throws Exception {
                return null;
            }

            @Override
            public Article randomValue() throws Exception {
                Article article=new Article();
                article.setAuthor(UUID.randomUUID().toString());
                article.setCategory(randomCategory());
                article.setTitle(UUID.randomUUID().toString());
                article.setContent(UUID.randomUUID().toString());
                article.setType(UUID.randomUUID().toString());
                article.setArticleSource(ArticleSource.ORIGINAL);
                return article;
            }

            @Override
            public BiConsumer<AbstractCRUDPage<Article>, Article> customAddFunction() throws Exception {
                return null;
            }

            @Override
            public void assertCreation(Article entity, Article data) throws Exception {
                assertThat(entity.getCategory())
                        .isEqualTo(data.getCategory());
                assertThat(entity.getTitle())
                        .isEqualTo(data.getTitle());
            }

            public void delete(){

            }
        });
    }
}
