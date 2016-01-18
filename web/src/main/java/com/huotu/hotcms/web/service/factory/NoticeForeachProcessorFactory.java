/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.web.service.factory;

import com.huotu.hotcms.service.entity.Notice;
import com.huotu.hotcms.service.model.thymeleaf.foreach.NormalForeachParam;
import com.huotu.hotcms.service.service.NoticeService;
import com.huotu.hotcms.web.thymeleaf.expression.DialectAttributeFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IProcessableElementTag;

import java.util.List;

/**
 * Created by cwb on 2016/1/18.
 */
public class NoticeForeachProcessorFactory {

    private final int DEFAULT_PAGE_SIZE = 5;

    private static Log log = LogFactory.getLog(NoticeForeachProcessorFactory.class);

    private static NoticeForeachProcessorFactory instance;

    private NoticeForeachProcessorFactory(){}

    public static NoticeForeachProcessorFactory getInstance() {
        if(instance == null) {
            instance = new NoticeForeachProcessorFactory();
        }
        return instance;
    }

    public List<Notice> process(IProcessableElementTag elementTag,ITemplateContext context) {
        try {
            NormalForeachParam noticeForeachParam = DialectAttributeFactory.getInstance().getForeachParam(elementTag, NormalForeachParam.class);
            WebApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
            NoticeService noticeService = (NoticeService)applicationContext.getBean("noticeServiceImpl");
            //根据指定id获取栏目列表
            if(noticeForeachParam.getSpecifyids()!=null) {
                return noticeService.getSpecifyNotices(noticeForeachParam.getSpecifyids());
            }
            if(StringUtils.isEmpty(noticeForeachParam.getCategoryid())) {
                throw new Exception("栏目id没有指定");
            }
            if(noticeForeachParam.getSize()==null) {
                noticeForeachParam.setSize(DEFAULT_PAGE_SIZE);
            }
            return noticeService.getNoticeList(noticeForeachParam);
        }catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());//TODO 上线时删除
        }
        return null;
    }

}
