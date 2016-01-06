package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.entity.Notice;
import com.huotu.hotcms.service.model.NoticeCategory;
import com.huotu.hotcms.service.repository.NoticeRepository;
import com.huotu.hotcms.service.service.NoticeService;
import com.huotu.hotcms.service.util.PageData;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chendeyu on 2016/1/5.
 */
@Service
public class NoticeServiceImpl implements NoticeService {


    @Autowired
    NoticeRepository noticeRepository;


    @Override
    public PageData<NoticeCategory> getPage(Integer customerId, String title, int page, int pageSize) {
        PageData<NoticeCategory> data = null;
        Specification<Notice> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(title)) {
                predicates.add(cb.like(root.get("title").as(String.class), "%" + title + "%"));
            }
            predicates.add(cb.equal(root.get("deleted").as(String.class), false));
            predicates.add(cb.equal(root.get("customerId").as(Integer.class), customerId));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        Page<Notice> pageData = noticeRepository.findAll(specification,new PageRequest(page - 1, pageSize));
        if (pageData != null) {
            List<Notice> notices =pageData.getContent();
            List<NoticeCategory> noticeCategoryList =new ArrayList<>();
            for(Notice notice : notices){
                NoticeCategory noticeCategory = new NoticeCategory();
                noticeCategory.setCategoryName(notice.getCategory().getName());
                noticeCategory.setCreateTime(notice.getCreateTime());
                noticeCategory.setCustomerId(notice.getCustomerId());
                noticeCategory.setId(notice.getId());
                noticeCategory.setContent(notice.getContent());
                noticeCategory.setTitle(notice.getTitle());
                noticeCategoryList.add(noticeCategory);
            }
            data = new PageData<NoticeCategory>();
            data.setPageCount(pageData.getTotalPages());
            data.setPageIndex(pageData.getNumber());
            data.setPageSize(pageData.getSize());
            data.setTotal(pageData.getTotalElements());
            data.setRows((NoticeCategory[])noticeCategoryList.toArray(new NoticeCategory[noticeCategoryList.size()]));
        }
        return  data;
    }

    @Override
    public Boolean saveNotice(Notice notice) {
        noticeRepository.save(notice);
        return true;
    }

    @Override
    public Notice findById(Long id) {
        Notice notice= noticeRepository.findOne(id);
        return notice ;
    }
}
