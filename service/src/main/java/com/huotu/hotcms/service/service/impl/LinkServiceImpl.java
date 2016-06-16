/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.entity.BaseEntity;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.model.LinkCategory;
import com.huotu.hotcms.service.model.thymeleaf.foreach.NormalForeachParam;
import com.huotu.hotcms.service.repository.LinkRepository;
import com.huotu.hotcms.service.service.CategoryService;
import com.huotu.hotcms.service.service.LinkService;
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
 * Created by chendeyu on 2016/1/6.
 */
@Service
public class LinkServiceImpl implements LinkService {
    private static Log log = LogFactory.getLog(LinkServiceImpl.class);

    @Autowired
    LinkRepository linkRepository;

    @Autowired
    CategoryService categoryService;


    @Override
    public PageData<LinkCategory> getPage(long ownerId, String title, int page, int pageSize) {
        PageData<LinkCategory> data = null;
        Specification<Link> specification = BaseEntity.Specification(ownerId, title, false);

        Page<Link> pageData = linkRepository.findAll(specification,new PageRequest(page - 1, pageSize));
        if (pageData != null) {
            List<Link> links =pageData.getContent();
            List<LinkCategory> linkCategoryList =new ArrayList<>();
            for(Link link : links){
                LinkCategory linkCategory = new LinkCategory();
                linkCategory.setCategoryName(link.getCategory().getName());
                linkCategory.setCreateTime(link.getCreateTime());
                linkCategory.setId(link.getId());
                linkCategory.setDescription(link.getDescription());
                linkCategory.setTitle(link.getTitle());
                linkCategoryList.add(linkCategory);
            }
            data = new PageData<>();
            data.setPageCount(pageData.getTotalPages());
            data.setPageIndex(pageData.getNumber());
            data.setPageSize(pageData.getSize());
            data.setTotal(pageData.getTotalElements());
            data.setRows(linkCategoryList.toArray(new LinkCategory[linkCategoryList.size()]));
        }
        return  data;
    }

    @Override
    public Boolean saveLink(Link link) {
        linkRepository.save(link);
        return true;
    }

    @Override
    public Link findById(Long id) {
        return linkRepository.findOne(id);
    }

    @Override
    public List<Link> getSpecifyLinks(String[] specifyIds) {
        List<String> ids = Arrays.asList(specifyIds);
        List<Long> linkIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
        Specification<Link> specification = (root, query, cb) -> {
            List<Predicate> predicates = linkIds.stream().map(id -> cb.equal(root.get("id").as(Long.class),id)).collect(Collectors.toList());
            return cb.or(predicates.toArray(new Predicate[predicates.size()]));
        };
        return linkRepository.findAll(specification, new Sort(Sort.Direction.DESC,"orderWeight"));
    }

    @Override
    public Page<Link> getLinkList(NormalForeachParam normalForeachParam) throws Exception {
        int pageIndex = normalForeachParam.getPageNo() - 1;
        int pageSize = normalForeachParam.getPageSize();
        if(normalForeachParam.getSize()!=null){
            pageSize=normalForeachParam.getSize();
        }
        Sort sort = new Sort(Sort.Direction.DESC, "orderWeight");
        if (!StringUtils.isEmpty(normalForeachParam.getSpecifyIds())) {
            return getSpecifyLinks(normalForeachParam.getSpecifyIds(), pageIndex, pageSize, sort);
        }
        if (!StringUtils.isEmpty(normalForeachParam.getCategoryId())) {
            return getLinks(normalForeachParam, pageIndex, pageSize, sort);
        } else {
            return getAllArticle(normalForeachParam, pageIndex, pageSize, sort);
        }
    }

    private Page<Link> getSpecifyLinks(String[] specifyIds, int pageIndex, int pageSize, Sort sort) {
        List<String> ids = Arrays.asList(specifyIds);
        List<Long> linkIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
        Specification<Link> specification = (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = linkIds.stream().map(id -> cb.equal(root.get("id").as(Long.class), id)).collect(Collectors.toList());
            return cb.or(predicates.toArray(new Predicate[predicates.size()]));
        };
        return linkRepository.findAll(specification, new PageRequest(pageIndex, pageSize, sort));
    }

    private Page<Link> getLinks(NormalForeachParam params, int pageIndex, int pageSize, Sort sort) throws Exception {
        try {
            Specification<Link> specification = BaseEntity.Specification(params);
            return linkRepository.findAll(specification, new PageRequest(pageIndex, pageSize, sort));
        } catch (Exception ex) {
            throw new Exception("获得文章列表出现错误");
        }
    }

    private Page<Link> getAllArticle(NormalForeachParam params, int pageIndex, int pageSize, Sort sort) {
        List<Category> subCategories = categoryService.getSubCategories(params.getParentcId());
        if (subCategories.size() == 0) {
            try {
                throw new Exception("父栏目节点没有子栏目");
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        Specification<Link> specification = BaseEntity.Specification(params, subCategories);
        return linkRepository.findAll(specification, new PageRequest(pageIndex, pageSize, sort));
    }
}
