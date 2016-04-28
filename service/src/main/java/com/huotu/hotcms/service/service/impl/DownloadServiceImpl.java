package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Download;
import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.model.thymeleaf.foreach.NormalForeachParam;
import com.huotu.hotcms.service.model.thymeleaf.foreach.PageableForeachParam;
import com.huotu.hotcms.service.repository.DownloadRepository;
import com.huotu.hotcms.service.service.CategoryService;
import com.huotu.hotcms.service.service.DownloadService;
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
 * Created by chendeyu on 2016/1/11.
 */
@Service
public class DownloadServiceImpl implements DownloadService {
    private static Log log = LogFactory.getLog(DownloadServiceImpl.class);

    @Autowired
    DownloadRepository downloadRepository;

    @Autowired
    CategoryService categoryService;

    @Override
    public Boolean saveDownload(Download download) {
        downloadRepository.save(download);
        return true;
    }

    @Override
    public Download findById(Long id) {

        Download download =  downloadRepository.findOne(id);
        return download;
    }

    @Override
    public List<Download> getSpecifyDownloads(String[] specifyIds) {
        List<String> ids = Arrays.asList(specifyIds);
        List<Long> linkIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
        Specification<Link> specification = (root, query, cb) -> {
            List<Predicate> predicates = linkIds.stream().map(id -> cb.equal(root.get("id").as(Long.class),id)).collect(Collectors.toList());
            return cb.or(predicates.toArray(new Predicate[predicates.size()]));
        };
        return downloadRepository.findAll(specification, new Sort(Sort.Direction.DESC,"orderWeight"));
    }

//    @Override
//    public List<Download> getDownloadList(NormalForeachParam param) {
//        Sort sort = new Sort(Sort.Direction.DESC,"orderWeight");
//        Specification<Link> specification = (root, query, cb) -> {
//            List<Predicate> predicates;
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
//        return downloadRepository.findAll(specification,new PageRequest(0,param.getSize(),sort)).getContent();
//    }

    @Override
    public Page<Download> getDownloadList(PageableForeachParam pageableForeachParam) throws Exception {
        int pageIndex = pageableForeachParam.getPageNo() - 1;
        int pageSize = pageableForeachParam.getPageSize();
        Sort sort = new Sort(Sort.Direction.DESC, "orderWeight");
        if (!StringUtils.isEmpty(pageableForeachParam.getSpecifyIds())) {
            return getSpecifyLinks(pageableForeachParam.getSpecifyIds(), pageIndex, pageSize, sort);
        }
        if (!StringUtils.isEmpty(pageableForeachParam.getCategoryId())) {
            return getDownloads(pageableForeachParam, pageIndex, pageSize, sort);
        } else {
            return getAllDownloads(pageableForeachParam, pageIndex, pageSize, sort);
        }
    }

    private Page<Download> getSpecifyLinks(String[] specifyIds, int pageIndex, int pageSize, Sort sort) {
        List<String> ids = Arrays.asList(specifyIds);
        List<Long> downloadsIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
        Specification<Link> specification = (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = downloadsIds.stream().map(id -> cb.equal(root.get("id").as(Long.class), id)).collect(Collectors.toList());
            return cb.or(predicates.toArray(new Predicate[predicates.size()]));
        };
        return downloadRepository.findAll(specification, new PageRequest(pageIndex, pageSize, sort));
    }

    private Page<Download> getDownloads(PageableForeachParam params, int pageIndex, int pageSize, Sort sort) throws Exception {
        try {
            Specification<Link> specification = (root, criteriaQuery, cb) -> {
                List<Predicate> predicates = new ArrayList<>();
                if (!StringUtils.isEmpty(params.getExcludeIds())) {
                    List<String> ids = Arrays.asList(params.getExcludeIds());
                    List<Long> downloadsIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
                    predicates = downloadsIds.stream().map(id -> cb.notEqual(root.get("id").as(Long.class), id)).collect(Collectors.toList());
                }
                predicates.add(cb.equal(root.get("deleted").as(Boolean.class), false));
                predicates.add(cb.equal(root.get("category").get("id").as(Long.class), params.getCategoryId()));
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            };
            return downloadRepository.findAll(specification, new PageRequest(pageIndex, pageSize, sort));
        } catch (Exception ex) {
            throw new Exception("获得下载模型列表出现错误");
        }
    }

    private Page<Download> getAllDownloads(PageableForeachParam params, int pageIndex, int pageSize, Sort sort) {
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
        return downloadRepository.findAll(specification, new PageRequest(pageIndex, pageSize, sort));
    }

}
