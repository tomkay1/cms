/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service;

import com.huotu.hotcms.service.config.ServiceTestConfig;
import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.repository.ArticleRepository;
import org.luffy.test.SpringWebTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * 用于测试的基类
 *
 * @author CJ
 */
@ContextConfiguration(classes = ServiceTestConfig.class)
@WebAppConfiguration
@Transactional
public abstract class TestBase extends SpringWebTest {

    ArticleRepository articleRepository;

    @Bean
    Article newRandomArticle() {
        Article article = new Article();
        article.setTitle(UUID.randomUUID().toString());

        Long id=new Long(60);
        articleRepository.findAllByIdAndNext(id);

        articleRepository.findAllByIdAndPreious(id);
        return articleRepository.save(article);
    }


}
