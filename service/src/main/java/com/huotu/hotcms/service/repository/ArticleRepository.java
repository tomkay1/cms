/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.repository;

import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by xhl on 2015/12/24.
 */
public interface ArticleRepository extends JpaRepository<Article, Long>, JpaSpecificationExecutor<Article> {

    /**
     * 查找栏目下所有的文章
     * @param category 栏目
     * @return 所有的文章
     */
    List<Article> findByCategory(Category category);


    /**
     * 根绝siteID 查询文章
     * @param siteId 站点ID
     * @return 文章
     */
    Article findArticleByCategory_Site_SiteId(long siteId);

    /**
     * <p>
     *     根据指定的文章ID查找下一篇文章
     * </p>
     * @param id 指定的文章ID]
     * @return article
     * */
    @Query(value = "select * from cms_article  where id >?1 and categoryId=(select categoryId from cms_article where id=?1) order BY id asc LIMIT 1",nativeQuery = true)
    Article findAllByIdAndNext(Long id);


    /**
     * <p>
     *     根据指定的ID查找上一篇文章
     * </p>
     * @param id 指定的文章ID
     * @return article
     * */
    @Query(value = "select * from cms_article  where id <?1 and categoryId=(select categoryId from cms_article where id=?1) order BY id desc LIMIT 1",nativeQuery = true)
    Article findAllByIdAndPreious(Long id);
}
