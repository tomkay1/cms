package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.entity.Download;
import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.model.thymeleaf.foreach.NormalForeachParam;
import com.huotu.hotcms.service.repository.DownloadRepository;
import com.huotu.hotcms.service.service.DownloadService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    DownloadRepository downloadRepository;
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

    @Override
    public List<Download> getDownloadList(NormalForeachParam param) {
        Sort sort = new Sort(Sort.Direction.DESC,"orderWeight");
        Specification<Link> specification = (root, query, cb) -> {
            List<Predicate> predicates;
            if(!StringUtils.isEmpty(param.getExcludeid())) {
                List<String> ids = Arrays.asList(param.getExcludeid());
                List<Long> linkIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
                predicates = linkIds.stream().map(id -> cb.notEqual(root.get("id").as(Long.class),id)).collect(Collectors.toList());
            }else {
                predicates = new ArrayList<>();
            }
            predicates.add(cb.equal(root.get("category").get("id").as(Long.class),param.getCategoryid()));
            predicates.add(cb.equal(root.get("deleted").as(Boolean.class),false));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        return downloadRepository.findAll(specification,new PageRequest(0,param.getSize(),sort)).getContent();
    }
}
