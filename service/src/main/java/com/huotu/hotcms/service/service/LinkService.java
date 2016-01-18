package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.model.LinkCategory;
import com.huotu.hotcms.service.model.thymeleaf.foreach.NormalForeachParam;
import com.huotu.hotcms.service.util.PageData;

import java.util.List;

/**
 * Created by chendeyu on 2016/1/6.
 */
public interface LinkService {
    PageData<LinkCategory> getPage(Integer customerId, String title, int page, int pageSize);
    Boolean saveLink(Link link);
    Link findById(Long id);

    List<Link> getLinkList(NormalForeachParam linkForeachParam);

    List<Link> getSpecifyLinks(String[] specifyids);
}
