package com.huotu.hotcms.repository;

import com.huotu.hotcms.entity.Article;
import com.huotu.hotcms.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Administrator on 2015/12/24.
 */
public interface ArticleRepository extends JpaRepository<Article,Long> {

    Article findByTitle(String title);
    Article findByCategory(Category category);

}
