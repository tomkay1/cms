/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.entity.BaseEntity;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.ArticleCategory;
import com.huotu.hotcms.service.model.thymeleaf.current.ArticleCurrentParam;
import com.huotu.hotcms.service.model.thymeleaf.foreach.PageableForeachParam;
import com.huotu.hotcms.service.model.thymeleaf.next.ArticleNextParam;
import com.huotu.hotcms.service.model.thymeleaf.next.ArticlePreviousParam;
import com.huotu.hotcms.service.repository.ArticleRepository;
import com.huotu.hotcms.service.service.ArticleService;
import com.huotu.hotcms.service.service.CategoryService;
import com.huotu.hotcms.service.util.PageData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Administrator xhl 2016/1/6.
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    private static Log log = LogFactory.getLog(ArticleServiceImpl.class);

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CategoryService categoryService;

    @Override
    public Article findById(Long id) {
        return articleRepository.findOne(id);
    }

    @Override
    public Page<Article> getArticleList(PageableForeachParam articleForeachParam) throws Exception {
        int pageIndex = articleForeachParam.getPageNo() - 1;
        int pageSize = articleForeachParam.getPageSize();
        Sort sort = new Sort(Sort.Direction.DESC, "orderWeight");
        if (!StringUtils.isEmpty(articleForeachParam.getSpecifyIds())) {
            return getSpecifyArticles(articleForeachParam.getSpecifyIds(), pageIndex, pageSize, sort);
        }
        if (!StringUtils.isEmpty(articleForeachParam.getCategoryId())) {
            return getArticles(articleForeachParam, pageIndex, pageSize, sort);
        } else {
            return getAllArticle(articleForeachParam, pageIndex, pageSize, sort);
        }
    }

    private Page<Article> getAllArticle(PageableForeachParam params, int pageIndex, int pageSize, Sort sort) {
        List<Category> subCategories = categoryService.getSubCategories(params.getParentcId());
        if (subCategories.size() == 0) {
            try {
                throw new Exception("父栏目节点没有子栏目");
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        Specification<Article> specification = (root, criteriaQuery, cb) -> {
            List<Predicate> p1 = new ArrayList<>();
            for (Category category : subCategories) {
                p1.add(cb.equal(root.get("category").as(Category.class), category));
            }
            Predicate predicate = cb.or(p1.toArray(new Predicate[p1.size()]));
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(params.getExcludeIds())) {
                List<String> ids = Arrays.asList(params.getExcludeIds());
                List<Long> articleIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
                predicates = articleIds.stream().map(id -> cb.notEqual(root.get("id").as(Long.class), id)).collect(Collectors.toList());
            }
            predicates.add(cb.equal(root.get("deleted").as(Boolean.class), false));
            predicates.add(predicate);
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        return articleRepository.findAll(specification, new PageRequest(pageIndex, pageSize, sort));
    }

    private Page<Article> getArticles(PageableForeachParam params, int pageIndex, int pageSize, Sort sort) throws Exception {
        try {
            Specification<Article> specification = (root, criteriaQuery, cb) -> {
                List<Predicate> predicates = new ArrayList<>();
                if (!StringUtils.isEmpty(params.getExcludeIds())) {
                    List<String> ids = Arrays.asList(params.getExcludeIds());
                    List<Long> articleIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
                    predicates = articleIds.stream().map(id -> cb.notEqual(root.get("id").as(Long.class), id)).collect(Collectors.toList());
                }
                predicates.add(cb.equal(root.get("deleted").as(Boolean.class), false));
                predicates.add(cb.equal(root.get("category").get("id").as(Long.class), params.getCategoryId()));
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            };
            return articleRepository.findAll(specification, new PageRequest(pageIndex, pageSize, sort));
        } catch (Exception ex) {
            throw new Exception("获得文章列表出现错误");
        }
    }

    private Page<Article> getSpecifyArticles(String[] specifyIds, int pageIndex, int pageSize, Sort sort) {
        List<String> ids = Arrays.asList(specifyIds);
        List<Long> articleIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
        Specification<Article> specification = (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = articleIds.stream().map(id -> cb.equal(root.get("id").as(Long.class), id)).collect(Collectors.toList());
            return cb.or(predicates.toArray(new Predicate[predicates.size()]));
        };
        return articleRepository.findAll(specification, new PageRequest(pageIndex, pageSize, sort));
    }

    @Override
    public PageData<ArticleCategory> getPage(long ownerId, String title, int page, int pageSize) {
        PageData<ArticleCategory> data = null;
        Specification<Article> specification = BaseEntity.Specification(ownerId, title, false);

        Page<Article> pageData = articleRepository.findAll(specification, new PageRequest(page - 1, pageSize));
        if (pageData != null) {
            List<Article> articles = pageData.getContent();
            List<ArticleCategory> articleCategoryList = new ArrayList<>();
            for (Article article : articles) {
                ArticleCategory articleCategory = new ArticleCategory();
                articleCategory.setSiteName(article.getCategory().getSite().getName());
//                articleCategory.setSite(article.getCategory().getSite());
                articleCategory.setCategoryName(article.getCategory().getName());
//                articleCategory.setCategory(article.getCategory());
                articleCategory.setCreateTime(article.getCreateTime());
                articleCategory.setCustomerId(article.getCategory().getSite().getOwner().getCustomerId());
                articleCategory.setId(article.getId());
                articleCategory.setDescription(article.getDescription());
                articleCategory.setTitle(article.getTitle());
                articleCategoryList.add(articleCategory);
            }
            data = new PageData<>();
            data.setPageCount(pageData.getTotalPages());
            data.setPageIndex(pageData.getNumber());
            data.setPageSize(pageData.getSize());
            data.setTotal(pageData.getTotalElements());
            data.setRows(articleCategoryList.toArray(new ArticleCategory[articleCategoryList.size()]));
        }
        return data;
    }

    @Override
    public Boolean saveArticle(Article article) {
        articleRepository.save(article);
        return true;
    }

    @Override
    public Article getArticleByParam(ArticleCurrentParam articleCurrentParam) {
        Article article=null;
        if (articleCurrentParam != null) {
            if (articleCurrentParam.getId() != null) {
                article=articleRepository.getOne(articleCurrentParam.getId());
            } else {
                article=articleRepository.getOne(articleCurrentParam.getDefaultid());
            }
        }
        article=setArticleThumbUri(article);
        return article;
    }

    @Override
    public Article setArticleThumbUri(Article article) {
        if(article!=null) {
            Category category = article.getCategory();
            if (category != null) {
                Site site = category.getSite();
                if (site != null) {
                    article.setThumbUri(site.getResourceUrl() + article.getThumbUri());
                }
            }
        }
        return article;
    }

    @Override
    public Article getArticleNextByParam(ArticleNextParam articleNextParam) {
        Article article=null;
        if (articleNextParam != null) {
            if (articleNextParam.getId() != null) {
                article=articleRepository.findAllByIdAndNext(articleNextParam.getId());
            }
        }
        article=setArticleThumbUri(article);
        return article;
    }

    @Override
    public Article getArticlePreiousByParam(ArticlePreviousParam articlePreviousParam) {
        Article article=null;
        if (articlePreviousParam != null) {
            if (articlePreviousParam.getId() != null) {
                article=articleRepository.findAllByIdAndPreious(articlePreviousParam.getId());
            }
        }
        article=setArticleThumbUri(article);
        return article;
    }
}
