/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.Notice;
import com.huotu.hotcms.service.model.NoticeCategory;
import com.huotu.hotcms.service.model.thymeleaf.foreach.NormalForeachParam;
import com.huotu.hotcms.service.util.PageData;

import java.util.List;

/**
 * Created by chendeyu on 2016/1/5.
 */
public interface NoticeService {
    /**
     * @param ownerId
     * @param title
     * @param page
     * @param pageSize
     * @return
     */
    PageData<NoticeCategory> getPage(long ownerId, String title, int page, int pageSize);
    Boolean saveNotice(Notice notice);
    Notice findById(Long id);

    List<Notice> getSpecifyNotices(String[] specifyIds);

    List<Notice> getNoticeList(NormalForeachParam noticeForeachParam);
}
