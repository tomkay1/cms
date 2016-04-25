package com.huotu.hotcms.web.service;


import com.huotu.hotcms.service.widget.model.GoodsDetail;

import javax.servlet.http.HttpServletRequest;

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
     * @return
     */
    String getPersonDetailUrl(HttpServletRequest request) throws Exception;

    /**
     * 获取商户的二维码地址
     * @param request
     * @return
     */
    String getSubscribeUrl(HttpServletRequest request) throws Exception;


}
