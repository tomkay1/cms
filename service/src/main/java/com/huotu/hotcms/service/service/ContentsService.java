package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.model.Contents;
import com.huotu.hotcms.service.util.PageData;

/**
 * Created by chendeyu on 2016/1/12.
 */
public interface ContentsService {
    PageData<Contents> getPage(String title,Long siteId,Long category, int page, int pageSize);
}
