/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.entity.AbstractContent;
import com.huotu.hotcms.service.entity.Notice;
import com.huotu.hotcms.service.model.NoticeCategory;
import com.huotu.hotcms.service.model.thymeleaf.foreach.NormalForeachParam;
import com.huotu.hotcms.service.repository.NoticeRepository;
import com.huotu.hotcms.service.service.NoticeService;
import com.huotu.hotcms.service.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by chendeyu on 2016/1/5.
 */
@Service
public class NoticeServiceImpl implements NoticeService {


    @Autowired
    NoticeRepository noticeRepository;


    @Override
    public PageData<NoticeCategory> getPage(long ownerId, String title, int page, int pageSize) {
        PageData<NoticeCategory> data = null;
        Specification<Notice> specification = AbstractContent.Specification(ownerId, title, false);

        Page<Notice> pageData = noticeRepository.findAll(specification, new PageRequest(page - 1, pageSize));
        if (pageData != null) {
            List<Notice> notices = pageData.getContent();
            List<NoticeCategory> noticeCategoryList = new ArrayList<>();
            for (Notice notice : notices) {
                NoticeCategory noticeCategory = new NoticeCategory();
                noticeCategory.setCategoryName(notice.getCategory().getName());
                noticeCategory.setCreateTime(notice.getCreateTime());
                if (notice.getCategory().getSite().getOwner() != null)
                    noticeCategory.setCustomerId(notice.getCategory().getSite().getOwner().getCustomerId());
                noticeCategory.setId(notice.getId());
                noticeCategory.setContent(notice.getContent());
                noticeCategory.setTitle(notice.getTitle());
                noticeCategoryList.add(noticeCategory);
            }
            data = new PageData<>();
            data.setPageCount(pageData.getTotalPages());
            data.setPageIndex(pageData.getNumber());
            data.setPageSize(pageData.getSize());
            data.setTotal(pageData.getTotalElements());
            data.setRows(noticeCategoryList.toArray(new NoticeCategory[noticeCategoryList.size()]));
        }
        return data;
    }

    @Override
    public Boolean saveNotice(Notice notice) {
        noticeRepository.save(notice);
        return true;
    }

    @Override
    public Notice findById(Long id) {
        Notice notice = noticeRepository.findOne(id);
        return notice;
    }

    @Override
    public List<Notice> getSpecifyNotices(String[] specifyIds) {
        List<String> ids = Arrays.asList(specifyIds);
        List<Long> noticeIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
        Specification<Notice> specification = (root, query, cb) -> {
            List<Predicate> predicates = noticeIds.stream().map(id -> cb.equal(root.get("id").as(Long.class), id)).collect(Collectors.toList());
            return cb.or(predicates.toArray(new Predicate[predicates.size()]));
        };
        return noticeRepository.findAll(specification, new Sort(Sort.Direction.DESC, "orderWeight"));
    }

    @Override
    public List<Notice> getNoticeList(NormalForeachParam param) {
        Sort sort = new Sort(Sort.Direction.DESC, "orderWeight");
        Specification<Notice> specification = (root, query, cb) -> {
            List<Predicate> predicates;
            if (!org.springframework.util.StringUtils.isEmpty(param.getExcludeIds())) {
                List<String> ids = Arrays.asList(param.getExcludeIds());
                List<Long> linkIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
                predicates = linkIds.stream().map(id -> cb.notEqual(root.get("id").as(Long.class), id)).collect(Collectors.toList());
            } else {
                predicates = new ArrayList<>();
            }
            predicates.add(cb.equal(root.get("category").get("id").as(Long.class), param.getCategoryId()));
            predicates.add(cb.equal(root.get("deleted").as(Boolean.class), false));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        return noticeRepository.findAll(specification, new PageRequest(0, param.getSize(), sort)).getContent();
    }
}
