/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service;

import com.huotu.huobanplus.common.entity.Brand;
import com.huotu.huobanplus.common.entity.Category;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

/**
 * 提供伙伴商城的相关数据
 *
 * @author CJ
 */
public interface MallService {

    /**
     * @param merchantId 商户号 {@link com.huotu.hotcms.service.entity.login.Owner#customerId}
     * @return 相关类目
     * @throws IOException {@link com.huotu.huobanplus.sdk.common.repository.CategoryRestRepository#findAll(Pageable)}
     */
    List<Category> listCategories(long merchantId) throws IOException;

    /**
     * @param merchantId 商户号 {@link com.huotu.hotcms.service.entity.login.Owner#customerId}
     * @return 品牌
     * @throws IOException
     */
    List<Brand> listBrands(long merchantId) throws IOException;
}
