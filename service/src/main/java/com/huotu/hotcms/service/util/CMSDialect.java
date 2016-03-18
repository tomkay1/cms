/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.util;

import com.huotu.hotcms.service.thymeleaf.dialect.*;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *     CMS扩展的Thymeleaf 标签
 * </p>
 * @since 1.0.0
 *
 * @author xhl
 */
public class CMSDialect {

    private static List<AbstractProcessorDialect> dialectList = new ArrayList<>();


    public static List<AbstractProcessorDialect> getDialectList() {
        initDialect();
        return dialectList;
    }

    public static List<AbstractProcessorDialect> getDialectList(TemplateMode templateMode) {
        initDialect();
        return dialectList;
    }

    /*
    * 初始化thymeleaf 扩展的标签
    * */
    public static void initDialect(){
        dialectList.add(new ArticleDialect());
        dialectList.add(new LinkDialect());
        dialectList.add(new CategoryDialect());
        dialectList.add(new VideoDialect());
        dialectList.add(new NoticeDialect());
        dialectList.add(new DownloadDialect());
        dialectList.add(new TimeDialect());
        dialectList.add(new WidgetDialect());
    }

    /*
   * 初始化thymeleaf 扩展的标签
   * */
    public static void initDialect(TemplateMode templateMode){
        dialectList.add(new ArticleDialect());
        dialectList.add(new LinkDialect());
        dialectList.add(new CategoryDialect());
        dialectList.add(new VideoDialect());
        dialectList.add(new NoticeDialect());
        dialectList.add(new DownloadDialect());
        dialectList.add(new TimeDialect());
    }
}
