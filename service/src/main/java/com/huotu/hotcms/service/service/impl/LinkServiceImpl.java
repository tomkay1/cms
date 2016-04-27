package com.huotu.hotcms.service.service.impl;

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
    public PageData<LinkCategory> getPage(Integer customerId, String title, int page, int pageSize) {
        PageData<LinkCategory> data = null;
        Specification<Link> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(title)) {
                predicates.add(cb.like(root.get("title").as(String.class), "%" + title + "%"));
            }
            predicates.add(cb.equal(root.get("deleted").as(String.class), false));
            predicates.add(cb.equal(root.get("customerId").as(Integer.class), customerId));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
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
            data = new PageData<LinkCategory>();
            data.setPageCount(pageData.getTotalPages());
            data.setPageIndex(pageData.getNumber());
            data.setPageSize(pageData.getSize());
            data.setTotal(pageData.getTotalElements());
            data.setRows((LinkCategory[])linkCategoryList.toArray(new LinkCategory[linkCategoryList.size()]));
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
        Link link= linkRepository.findOne(id);
        return link ;
    }

//    @Override
//    public Page<Link> getLinkList(NormalForeachParam param) {
//        Sort sort = new Sort(Sort.Direction.DESC,"orderWeight");
//        Specification<Link> specification = (root, query, cb) -> {
//          List<Predicate> predicates;
//            if(!StringUtils.isEmpty(param.getExcludeIds())) {
//                List<String> ids = Arrays.asList(param.getExcludeIds());
//                List<Long> linkIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
//                predicates = linkIds.stream().map(id -> cb.notEqual(root.get("id").as(Long.class),id)).collect(Collectors.toList());
//            }else {
//                predicates = new ArrayList<>();
//            }
//            predicates.add(cb.equal(root.get("category").get("id").as(Long.class),param.getCategoryId()));
//            predicates.add(cb.equal(root.get("deleted").as(Boolean.class),false));
//            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
//        };
//        return linkRepository.findAll(specification,new PageRequest(0,param.getSize(),sort)).getContent();
//    }

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
            Specification<Link> specification = (root, criteriaQuery, cb) -> {
                List<Predicate> predicates = new ArrayList<>();
                if (!StringUtils.isEmpty(params.getExcludeIds())) {
                    List<String> ids = Arrays.asList(params.getExcludeIds());
                    List<Long> linkIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
                    predicates = linkIds.stream().map(id -> cb.notEqual(root.get("id").as(Long.class), id)).collect(Collectors.toList());
                }
                predicates.add(cb.equal(root.get("deleted").as(Boolean.class), false));
                predicates.add(cb.equal(root.get("category").get("id").as(Long.class), params.getCategoryId()));
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            };
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
        Specification<Link> specification = (root, criteriaQuery, cb) -> {
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
        return linkRepository.findAll(specification, new PageRequest(pageIndex, pageSize, sort));
    }
}
