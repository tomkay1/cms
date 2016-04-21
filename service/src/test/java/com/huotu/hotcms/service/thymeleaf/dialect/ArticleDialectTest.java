package com.huotu.hotcms.service.thymeleaf.dialect;

import org.junit.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * Article方言测试
 * 包含
 *
 * @author CJ
 */
public class ArticleDialectTest extends DialectTest {

    @Override
    String templateName() {
        return "article.html";
    }

    @Test
    public void test1() throws Exception {
        perform().andDo(print());
    }
}