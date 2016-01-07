package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.Notice;
import com.huotu.hotcms.service.model.NoticeCategory;
import com.huotu.hotcms.service.util.PageData;

/**
 * Created by chendeyu on 2016/1/5.
 */
public interface NoticeService {
    PageData<NoticeCategory> getPage(Integer customerId, String title, int page, int pageSize);
    Boolean saveNotice(Notice notice);
    Notice findById(Long id);
}