package com.huotu.hotcms.service.repository;

import com.huotu.hotcms.service.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by xhl on 2015/12/24.
 */
public interface ArticleRepository extends JpaRepository<Article,Long>,JpaSpecificationExecutor {

    Article findById(Long id);

    @Query(value = "select * from cms_article  where id >?1 order BY id asc LIMIT 1",nativeQuery = true)
    Article findAllByIdAndNext(Long id);


    @Query(value = "select * from cms_article  where id <?1 order BY id desc LIMIT 1",nativeQuery = true)
    Article findAllByIdAndPreious(Long id);
}
