/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.widget.model;
import com.huotu.hotcms.service.common.SortEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 商品检索条件
 * Created by cwb on 2016/3/17.
 */
@Getter
@Setter
public class GoodsSearcher {

    /**
     * 商品分类主键id
     */
    private int goodsCatId;
    /**
     * 商品类型主键id
     */
    private int goodsTypeId;
    /**
     * 品牌主键id
     */
    private int brandId;
    /**
     * 起始价格
     */
    private int minPrice;
    /**
     * 结束价格
     */
    private int maxPrice;
    /**
     * 会员主键id（用于查询会员价）
     */
    private int userId;
    /**
     * 商品关键字（可模糊匹配）
     */
    private String keyword;
    /**
     * 页码
     */
    private int pageNo;
    /**
     * 排序依据
     */
    private SortEnum sortEnum;
    /**
     * 升降序
     * 0:降序 1:升序
     */
    private int direction;

}
