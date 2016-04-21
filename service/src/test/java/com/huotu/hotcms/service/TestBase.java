package com.huotu.hotcms.service;

import com.huotu.hotcms.service.config.ServiceTestConfig;
import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.repository.ArticleRepository;
import org.luffy.test.SpringWebTest;
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
public class TestBase extends SpringWebTest {

    ArticleRepository articleRepository;

    Article newRandomArticle() {
        Article article = new Article();
        article.setTitle(UUID.randomUUID().toString());
        return articleRepository.save(article);
    }
}
