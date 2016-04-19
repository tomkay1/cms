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


    GoodsDetail getGoodsDetail(int goodsId, int userId) throws Exception;

    String getGoodsWxUrl(HttpServletRequest request,Long goodsId);
}
