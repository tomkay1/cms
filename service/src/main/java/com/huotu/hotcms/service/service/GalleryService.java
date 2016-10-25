/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.Gallery;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.thymeleaf.foreach.PageableForeachParam;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface  GalleryService {

    @Transactional(readOnly = true)
    List<Gallery> listGalleries(Site site);

    Boolean saveGallery(Gallery gallery);
    Gallery findById(Long id);

    List<Gallery> getSpecifyGallerys(String[] specifyIds);

    /**
     * 标签解析时获取所有gallery
     *
     * @param foreachParam
     * @return
     * @throws Exception
     */
    Page<Gallery> getGalleryList(PageableForeachParam foreachParam) throws Exception;

}
