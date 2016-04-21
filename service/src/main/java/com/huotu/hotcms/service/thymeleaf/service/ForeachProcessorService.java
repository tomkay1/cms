/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.service;

import com.huotu.hotcms.service.thymeleaf.common.DialectTypeEnum;
import com.huotu.hotcms.service.thymeleaf.service.factory.ArticleForeachProcessor;
import com.huotu.hotcms.service.thymeleaf.service.factory.CategoryForeachProcessor;
import com.huotu.hotcms.service.thymeleaf.service.factory.DownloadForeachProcessor;
import com.huotu.hotcms.service.thymeleaf.service.factory.GalleryForeachProcessor;
import com.huotu.hotcms.service.thymeleaf.service.factory.GalleryListForeachProcessor;
import com.huotu.hotcms.service.thymeleaf.service.factory.LinkForeachProcessor;
import com.huotu.hotcms.service.thymeleaf.service.factory.NoticeForeachProcessor;
import com.huotu.hotcms.service.thymeleaf.service.factory.VideoForeachProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IProcessableElementTag;


/**
 * Created by cwb on 2016/1/4.
 */
@Component
public class ForeachProcessorService {

    @Autowired
    private ArticleForeachProcessor articleForeachProcessor;
    @Autowired
    private CategoryForeachProcessor categoryForeachProcessor;
    @Autowired
    private VideoForeachProcessor videoForeachProcessor;
    @Autowired
    private LinkForeachProcessor linkForeachProcessor;
    @Autowired
    private NoticeForeachProcessor noticeForeachProcessor;
    @Autowired
    private DownloadForeachProcessor downloadForeachProcessor;
    @Autowired
    private GalleryForeachProcessor galleryForeachProcessor;
    @Autowired
    private GalleryListForeachProcessor galleryListForeachProcessor;


    public Object resolveDataByAttr(String dialectPrefix, IProcessableElementTag elementTag, ITemplateContext context) {
        if(dialectPrefix.equals(DialectTypeEnum.ARTICLE.getDialectPrefix())) {
            return articleForeachProcessor.process(elementTag, context);
        }
        if(dialectPrefix.equals(DialectTypeEnum.CATEGORY.getDialectPrefix())) {
            return categoryForeachProcessor.process(elementTag, context);
        }
        if(dialectPrefix.equals(DialectTypeEnum.VIDEO.getDialectPrefix())) {
            return videoForeachProcessor.process(elementTag, context);
        }
        if(dialectPrefix.equals(DialectTypeEnum.LINK.getDialectPrefix())) {
            return linkForeachProcessor.process(elementTag, context);
        }
        if(dialectPrefix.equals(DialectTypeEnum.NOTICE.getDialectPrefix())) {
            return noticeForeachProcessor.process(elementTag, context);
        }
        if(dialectPrefix.equals(DialectTypeEnum.DOWNLOAD.getDialectPrefix())) {
            return downloadForeachProcessor.process(elementTag, context);
        }
        if(dialectPrefix.equals(DialectTypeEnum.GALLERY.getDialectPrefix())) {
            return galleryForeachProcessor.process(elementTag, context);
        }
        if(dialectPrefix.equals(DialectTypeEnum.GALLERYLIST.getDialectPrefix())) {
            return galleryListForeachProcessor.process(elementTag, context);
        }
        return null;
    }


}
