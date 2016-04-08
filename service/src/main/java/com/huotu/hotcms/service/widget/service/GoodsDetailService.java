package com.huotu.hotcms.service.widget.service;

import com.huotu.hotcms.service.widget.model.JsonModel;

/**
 * Created by chendeyu on 2016/4/8.
 */
public interface GoodsDetailService {

    /**
     * 商品检索
     * @param goodsId 商品主键id
     * @param unionId 微信用户id
     * @return
     */
    JsonModel getGoodsDetail(int goodsId, String unionId) throws Exception;
}
