/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.service.MallService;
import com.huotu.huobanplus.common.entity.Brand;
import com.huotu.huobanplus.common.entity.Category;
import com.huotu.huobanplus.common.entity.User;
import com.huotu.huobanplus.sdk.common.repository.BrandRestRepository;
import com.huotu.huobanplus.sdk.common.repository.CategoryRestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author CJ
 */
@Service
public class MallServiceImpl implements MallService {

    @Autowired
    private CategoryRestRepository categoryRestRepository;
    @Autowired
    private BrandRestRepository brandRestRepository;

    @Override
    public List<Category> listCategories(long merchantId) throws IOException {
        return categoryRestRepository.listByMerchantId(merchantId);
    }

    @Override
    public List<Brand> listBrands(long merchantId) throws IOException {
        return brandRestRepository.listByMerchantId(merchantId);
    }

    @Override
    public User getLoginUser() {
        return null;
    }

    @Override
    public String getLoginUserName() {
        return null;
    }

    @Override
    public int getShoppingCartCount(Long userId) {
        return 0;
    }


}
