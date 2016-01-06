package com.huotu.hotcms.service.repository;

import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Administrator on 2015/12/24.
 */
public interface ArticleRepository extends JpaRepository<Article,Long> {

    Article findByTitle(String title);
    Article findByCategory(Category category);

    Article findById(Long id);
}
