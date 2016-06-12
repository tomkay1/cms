/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.model.ArticleCategory;
import com.huotu.hotcms.service.model.thymeleaf.current.ArticleCurrentParam;
import com.huotu.hotcms.service.model.thymeleaf.foreach.PageableForeachParam;
import com.huotu.hotcms.service.model.thymeleaf.next.ArticleNextParam;
import com.huotu.hotcms.service.model.thymeleaf.next.ArticlePreviousParam;
import com.huotu.hotcms.service.util.PageData;
import org.springframework.data.domain.Page;

/**
 * <p>
 *     文章服务
 * </p>
 * @author xhl
 *
 * @since 1.0.0
 */
public interface ArticleService {

    /**
     * 获取所有article 分页信息
     *
     * @param customerId
     * @param title
     * @param page
     * @param pageSize
     * @return
     */
    PageData<ArticleCategory> getPage(Integer customerId, String title, int page, int pageSize);

    Boolean saveArticle(Article article);

    Article findById(Long id);

    Page<Article> getArticleList(PageableForeachParam articleForeachParam) throws Exception;

    /**
     * <p>
     *    根据articleCurrentParam 获得文章对象
     * </p>
     * @param articleCurrentParam
     * @return article
     */
    Article getArticleByParam(ArticleCurrentParam articleCurrentParam);

    /**
     * 处理文章相关缩略图地址
     *
     * @param article
     * @return
     */
    Article setArticleThumbUri(Article article);

    /**
     * <p>
     *     根据articleNextParam对象获得 文章对象（下一篇文章）
     * </p>
     * @param articleNextParam
     * @return article
     */
    Article getArticleNextByParam(ArticleNextParam articleNextParam);

    /**
     * <p>
     *     根据ArticlePreviousParam对象获得上一篇文章信息对象
     * </p>
     * @param articlePreviousParam
     * @return article
     */
    Article getArticlePreiousByParam(ArticlePreviousParam articlePreviousParam);
}
