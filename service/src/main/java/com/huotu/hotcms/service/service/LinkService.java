package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.model.LinkCategory;
import com.huotu.hotcms.service.model.thymeleaf.foreach.NormalForeachParam;
import com.huotu.hotcms.service.util.PageData;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by chendeyu on 2016/1/6.
 */
public interface LinkService {
    PageData<LinkCategory> getPage(Integer customerId, String title, int page, int pageSize);
    Boolean saveLink(Link link);
    Link findById(Long id);

//    List<Link> getLinkList(NormalForeachParam linkForeachParam);

    List<Link> getSpecifyLinks(String[] specifyids);

    /**
     * 根据NormalForeachParam 实体类来获得分页链接模型数据列表,
     * NormalForeachParam 该实体类是通过编写的参数标签或者当前http 上下文 request中获得参数实体
     *
     * @param normalForeachParam 该实体类是通过编写的参数标签或者当前http 上下文 request中获得参数实体
     * @return
     * @throws Exception
     */
    Page<Link> getLinkList(NormalForeachParam normalForeachParam) throws Exception;
}
