/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.web.service;


import com.huotu.hotcms.service.widget.model.GoodsDetail;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * Created by chendeyu on 2016/4/8.
 */
public interface GoodsDetailService {

    /**
     * 商品检索
     * @param goodsId 商品主键id
     * @return
     */
//    Goods getGoodsDetail(int goodsId) throws Exception;


    /**
     * 商品详情接口
     *
     * @param goodsId
     * @param userId
     * @return
     * 如商户未登录,返回市场价
     * @throws Exception
     */
    GoodsDetail getGoodsDetail(int goodsId, int userId) throws Exception;

    /**
     * 获取微信登录二维码地址
     * @param request
     * @param goodsId
     * @return
     */
    String getGoodsWxUrl(HttpServletRequest request,Long goodsId);

    /**
     * 获取PC商城个人中心二维码地址
     * @param request
     * @param locale
     * @return
     */
    String getPersonDetailUrl(HttpServletRequest request, Locale locale) throws Exception;

    /**
     * 获取商户的二维码地址
     * @param request
     * @param locale
     * @return
     */
    String getSubscribeUrl(HttpServletRequest request, Locale locale) throws Exception;


}
