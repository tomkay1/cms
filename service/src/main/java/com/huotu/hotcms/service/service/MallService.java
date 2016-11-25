/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.huobanplus.common.entity.Brand;
import com.huotu.huobanplus.common.entity.Category;
import com.huotu.huobanplus.common.entity.User;
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

    /**
     * 获取登录的用户
     *
     * @return 登录返回商城用户，未登录返回null
     */
    User getLoginUser();

    /**
     * 获取当前登录用户名称
     *
     * @return 用户名称，null
     */
    String getLoginUserName();

    /**
     * 获取当前购物车商品数量
     *
     * @param userId 用户id
     * @return 购物车商品数量
     */
    int getShoppingCartCount(Long userId);


    /**
     * 获取当前商城域名
     *
     * @param owner
     * @return
     */
    String getMallDomain(Owner owner);


}
