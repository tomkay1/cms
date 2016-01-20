/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.web.service.factory;

import com.huotu.hotcms.service.model.thymeleaf.foreach.NormalForeachParam;
import com.huotu.hotcms.service.service.LinkService;
import com.huotu.hotcms.web.thymeleaf.expression.DialectAttributeFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IProcessableElementTag;


/**
 * Created by cwb on 2016/1/6.
 */
public class LinkForeachProcessorFactory {

    private final int DEFAULT_PAGE_SIZE = 5;

    private static Log log = LogFactory.getLog(LinkForeachProcessorFactory.class);

    private static LinkForeachProcessorFactory instance;

    public static LinkForeachProcessorFactory getInstance() {
        if(instance == null) {
            instance = new LinkForeachProcessorFactory();
        }
        return instance;
    }

    public Object process(IProcessableElementTag elementTag, ITemplateContext context) {
        try {
            NormalForeachParam linkForeachParam = DialectAttributeFactory.getInstance().getForeachParam(elementTag, NormalForeachParam.class);
            WebApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
            LinkService linkService = (LinkService)applicationContext.getBean("linkServiceImpl");
            //根据指定id获取栏目列表
            if(linkForeachParam.getSpecifyids()!=null) {
                return linkService.getSpecifyLinks(linkForeachParam.getSpecifyids());
            }
            if(StringUtils.isEmpty(linkForeachParam.getCategoryid())) {
                throw new Exception("栏目id没有指定");
            }
            if(linkForeachParam.getSize()==null) {
                linkForeachParam.setSize(DEFAULT_PAGE_SIZE);
            }
            return linkService.getLinkList(linkForeachParam);
        }catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
