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
public interface ArticleRepository extends JpaRepository<Article,Long>,JpaSpecificationExecutor {

//    Article findOneByIdGreatThanOrderByIdAsc(Long id);

    /**
     * 根绝站点ID和序列号 查询 文章
     * @param siteId 站点ID
     * @param serial 序列号
     * @return article {@link com.huotu.hotcms.service.entity.Article}
     */
    Article findBySiteIdAndSerial(long siteId,String serial);

    /**
     * 查找栏目下所有的文章
     * @param category 栏目
     * @return 所有的文章
     */
    List<Article> findByCategory(Category category);



    @Query("select o from Article o where o.category.site.siteId=?1")
    Article findArticleBySiteId(long siteId);

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
