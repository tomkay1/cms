/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.util;

import com.huotu.hotcms.service.thymeleaf.dialect.ArticleDialect;
import com.huotu.hotcms.service.thymeleaf.dialect.CategoryDialect;
import com.huotu.hotcms.service.thymeleaf.dialect.DownloadDialect;
import com.huotu.hotcms.service.thymeleaf.dialect.GalleryDialect;
import com.huotu.hotcms.service.thymeleaf.dialect.GalleryItemDialect;
import com.huotu.hotcms.service.thymeleaf.dialect.LinkDialect;
import com.huotu.hotcms.service.thymeleaf.dialect.NoticeDialect;
import com.huotu.hotcms.service.thymeleaf.dialect.OldWidgetDialect;
import com.huotu.hotcms.service.thymeleaf.dialect.TimeDialect;
import com.huotu.hotcms.service.thymeleaf.dialect.VideoDialect;
import org.thymeleaf.dialect.AbstractProcessorDialect;

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


    /**
     * @return list if all dialects
     * @deprecated 不要再用这个方法了, 应该使用@{@link org.springframework.beans.factory.annotation.Autowired}
     * 一个{@link org.thymeleaf.dialect.IDialect}的{@link java.util.Set 集合}
     */
    public static List<AbstractProcessorDialect> getDialectList() {
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
        dialectList.add(new GalleryDialect());
        dialectList.add(new GalleryItemDialect());
        dialectList.add(new TimeDialect());
        dialectList.add(new OldWidgetDialect());
    }

}
