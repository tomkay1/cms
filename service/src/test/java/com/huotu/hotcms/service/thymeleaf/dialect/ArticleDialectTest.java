package com.huotu.hotcms.service.thymeleaf.dialect;

import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.repository.ArticleRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * Article方言测试
 * 包含
 *
 * @author CJ
 */
public class ArticleDialectTest extends DialectTest {

    @Autowired
    ArticleRepository articleRepository;

    @Override
    String templateName() {
        return "article.html";
    }

    @Test
    public void test1() throws Exception {
        perform().andDo(print());
    }

    @Test
    public void nextArticle(){
        Long id=new Long(60);
        Article article= articleRepository.findAllByIdAndNext(id);
    }

    @Test
    public void prdArticle(){
        Long id=new Long(60);
        Article article=articleRepository.findAllByIdAndPreious(id);
    }
}