/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.web.service;

import com.huotu.hotcms.service.entity.Host;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.service.HostService;
import com.huotu.hotcms.service.service.impl.HostServiceImpl;
import com.huotu.hotcms.web.common.DialectTypeEnum;
import com.huotu.hotcms.web.model.ForeachDialectModel;
import com.huotu.hotcms.web.model.Seo;
import com.huotu.hotcms.web.thymeleaf.expression.ForeachDialectAttributeFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.expression.IExpressionObjects;
import org.thymeleaf.model.IProcessableElementTag;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;


/**
 * Created by cwb on 2016/1/4.
 */
public class ForeachProcessorService extends BaseProcessorService {


    public Object resolveDataByAttr(IProcessableElementTag elementTag,ITemplateContext context){
        ForeachDialectModel model= ForeachDialectAttributeFactory.getInstance().getHtml5Attr(elementTag);
        IExpressionObjects expressContent= context.getExpressionObjects();
        HttpServletRequest request=(HttpServletRequest)expressContent.getObject("request");
        if(model!=null){
            if(dialectPrefix.equals(DialectTypeEnum.ARTICLE.getValue())){
                WebApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
                HostService hostService = (HostService)applicationContext.getBean("hostServiceImpl");
                Host host = hostService.getHost(request.getServerName());
                Set<Site> sites = host.getSites();
                return sites;
            }
            if(dialectPrefix.equals(DialectTypeEnum.LINK.getValue()))
            {
                //TODO:测试数据服务
                Seo[] site=new Seo[]{
                        new Seo("链接一"),
                        new Seo("链接二"),
                        new Seo("链接三"),
                        new Seo("链接四"),
                        new Seo("链接五"),
                };
                return site;
            }
            //解析数据服务
        }
        return  null;
    }


}
