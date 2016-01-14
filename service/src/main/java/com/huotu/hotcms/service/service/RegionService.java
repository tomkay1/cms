/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.Region;
import com.huotu.hotcms.service.util.PageData;

import java.util.Set;

/**
 * Created by cwb on 2015/12/24.
 */
public interface RegionService {
    Region getRegion(String area);
    PageData<Region> getPage(String name,int page,int pageSize);

    Region getRegionByCode(String regionCode);

    Boolean isRegionByCode(String regionCode);

    Region getRegionByLangCodeAndRegionCode(String langCode,String regionCode);
}
