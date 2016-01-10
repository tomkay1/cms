package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.model.ArticleCategory;
import com.huotu.hotcms.service.model.thymeleaf.ArticleForeachParam;
import com.huotu.hotcms.service.repository.ArticleRepository;
import com.huotu.hotcms.service.service.ArticleService;
import com.huotu.hotcms.service.util.PageData;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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
    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public Article findById(Long id) {
        return articleRepository.findById(id);
    }

    @Override
    public Page<Article> getArticleList(ArticleForeachParam articleForeachParam) {
        int pageIndex = articleForeachParam.getPageno()-1;
        int pageSize = articleForeachParam.getPagesize();
        Sort sort = new Sort(Sort.Direction.DESC, "orderWeight");

        if(!StringUtils.isEmpty(articleForeachParam.getSpecifyids())) {
            List<String> ids = Arrays.asList(articleForeachParam.getSpecifyids());
            List<Long> articleIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
            Specification<Article> specification = (root, criteriaQuery, cb) -> {
                List<Predicate> predicates = articleIds.stream().map(id -> cb.equal(root.get("id").as(Long.class), id)).collect(Collectors.toList());
                return cb.or(predicates.toArray(new Predicate[predicates.size()]));
            };
            return articleRepository.findAll(specification,new PageRequest(pageIndex,pageSize,sort));
        }
        Specification<Article> specification = (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(!StringUtils.isEmpty(articleForeachParam.getExcludeid())) {
                List<String> ids = Arrays.asList(articleForeachParam.getExcludeid());
                List<Long> articleIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
                predicates = articleIds.stream().map(id -> cb.notEqual(root.get("id").as(Long.class), id)).collect(Collectors.toList());
            }
            predicates.add(cb.equal(root.get("deleted").as(Boolean.class),0));
            predicates.add(cb.equal(root.get("category").get("id").as(Long.class), articleForeachParam.getCategoryid()));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        return articleRepository.findAll(specification,new PageRequest(pageIndex,pageSize,sort));
    }

    //    @Override
//    public List<Article> getArticleList(ArticleForeachParam articleForeachParam) {
//        return null;
//    }
//
    @Override
    public PageData<ArticleCategory> getPage(Integer customerId, String title, int page, int pageSize) {
        PageData<ArticleCategory> data = null;
        Specification<Article> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(title)) {
                predicates.add(cb.like(root.get("title").as(String.class), "%" + title + "%"));
            }
            predicates.add(cb.equal(root.get("deleted").as(String.class), false));
            predicates.add(cb.equal(root.get("customerId").as(Integer.class), customerId));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        Page<Article> pageData = articleRepository.findAll(specification,new PageRequest(page - 1, pageSize));
        if (pageData != null) {
            List<Article> articles =pageData.getContent();
            List<ArticleCategory> articleCategoryList =new ArrayList<>();
            for(Article article : articles){
                ArticleCategory articleCategory = new ArticleCategory();
                articleCategory.setSiteName(article.getCategory().getSite().getName());
//                articleCategory.setSite(article.getCategory().getSite());
                articleCategory.setCategoryName(article.getCategory().getName());
//                articleCategory.setCategory(article.getCategory());
                articleCategory.setCreateTime(article.getCreateTime());
                articleCategory.setCustomerId(article.getCustomerId());
                articleCategory.setId(article.getId());
                articleCategory.setDescription(article.getDescription());
                articleCategory.setTitle(article.getTitle());
                articleCategoryList.add(articleCategory);
            }
            data = new PageData<ArticleCategory>();
            data.setPageCount(pageData.getTotalPages());
            data.setPageIndex(pageData.getNumber());
            data.setPageSize(pageData.getSize());
            data.setTotal(pageData.getTotalElements());
            data.setRows((ArticleCategory[])articleCategoryList.toArray(new ArticleCategory[articleCategoryList.size()]));
        }
        return  data;
    }

    @Override
    public Boolean saveArticle(Article article) {
        articleRepository.save(article);
        return true;
    }


}
