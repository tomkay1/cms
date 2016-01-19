package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.model.ArticleCategory;
import com.huotu.hotcms.service.model.thymeleaf.current.ArticleCurrentParam;
import com.huotu.hotcms.service.model.thymeleaf.foreach.PageableForeachParam;
import com.huotu.hotcms.service.repository.ArticleRepository;
import com.huotu.hotcms.service.service.ArticleService;
import com.huotu.hotcms.service.service.CategoryService;
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

    @Autowired
    private CategoryService categoryService;

    @Override
    public Article findById(Long id) {
        return articleRepository.findById(id);
    }

    @Override
    public Page<Article> getArticleList(PageableForeachParam articleForeachParam) {
        int pageIndex = articleForeachParam.getPageno()-1;
        int pageSize = articleForeachParam.getPagesize();
        Sort sort = new Sort(Sort.Direction.DESC, "orderWeight");
        if(!StringUtils.isEmpty(articleForeachParam.getSpecifyids())) {
            return getSpecifyArticles(articleForeachParam.getSpecifyids(),pageIndex,pageSize,sort);
        }
        if(!StringUtils.isEmpty(articleForeachParam.getCategoryid())) {
            return getArticles(articleForeachParam, pageIndex, pageSize, sort);
        }else {
            return getAllArticle(articleForeachParam, pageIndex, pageSize, sort);
        }
    }

    private Page<Article> getAllArticle(PageableForeachParam params, int pageIndex, int pageSize, Sort sort){
        List<Category> subCategories =  categoryService.getSubCategories(params.getParentcid());
        if(subCategories.size()==0) {
            try {
                throw new Exception("父栏目节点没有子栏目");
            }catch (Exception e) {
                e.printStackTrace();//TODO 日志处理
            }
        }
        Specification<Article> specification = (root, criteriaQuery, cb) -> {
            List<Predicate> p1 = new ArrayList<>();
            for(Category category : subCategories) {
                p1.add(cb.equal(root.get("category").as(Category.class), category));
            }
            Predicate predicate = cb.or(p1.toArray(new Predicate[p1.size()]));
            List<Predicate> predicates = new ArrayList<>();
            if(!StringUtils.isEmpty(params.getExcludeids())) {
                List<String> ids = Arrays.asList(params.getExcludeids());
                List<Long> articleIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
                predicates = articleIds.stream().map(id -> cb.notEqual(root.get("id").as(Long.class), id)).collect(Collectors.toList());
            }
            predicates.add(cb.equal(root.get("deleted").as(Boolean.class), false));
            predicates.add(predicate);
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        return articleRepository.findAll(specification,new PageRequest(pageIndex,pageSize,sort));
    }

    private Page<Article> getArticles(PageableForeachParam params, int pageIndex, int pageSize, Sort sort) {
        Specification<Article> specification = (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(!StringUtils.isEmpty(params.getExcludeids())) {
                List<String> ids = Arrays.asList(params.getExcludeids());
                List<Long> articleIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
                predicates = articleIds.stream().map(id -> cb.notEqual(root.get("id").as(Long.class), id)).collect(Collectors.toList());
            }
            predicates.add(cb.equal(root.get("deleted").as(Boolean.class),false));
            predicates.add(cb.equal(root.get("category").get("id").as(Long.class), params.getCategoryid()));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        return articleRepository.findAll(specification,new PageRequest(pageIndex,pageSize,sort));
    }

    private Page<Article> getSpecifyArticles(String[] specifyIds,int pageIndex,int pageSize,Sort sort) {
        List<String> ids = Arrays.asList(specifyIds);
        List<Long> articleIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
        Specification<Article> specification = (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = articleIds.stream().map(id -> cb.equal(root.get("id").as(Long.class), id)).collect(Collectors.toList());
            return cb.or(predicates.toArray(new Predicate[predicates.size()]));
        };
        return articleRepository.findAll(specification,new PageRequest(pageIndex,pageSize,sort));
    }

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

    @Override
    public Article getArticleByParam(ArticleCurrentParam articleCurrentParam) {
        if (articleCurrentParam != null) {
            if (articleCurrentParam.getId() != null) {
                return articleRepository.getOne(articleCurrentParam.getId());
            } else {
                return articleRepository.getOne(articleCurrentParam.getDefaultid());
            }
        }
        return null;
    }
}
