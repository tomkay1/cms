package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.CustomPages;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.util.PageData;

/**
 * Created by Administrator on 2016/3/18.
 */
public interface CustomPagesService {
    PageData<CustomPages> getPage(String name,Long siteId,boolean delete,boolean publish, int page,int pageSize);

    CustomPages getCustomPages(long id);

    CustomPages save(CustomPages customPages);

    CustomPages findHomePages(Site site);

    Boolean setHomePages(Long id);

    void delete(CustomPages customPages);
}
