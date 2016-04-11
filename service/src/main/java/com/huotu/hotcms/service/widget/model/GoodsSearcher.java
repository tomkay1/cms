/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.widget.model;
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
    private Long goodsCatId;
    /**
     * 商品类型主键id
     */
    private Integer goodsTypeId;
    /**
     * 品牌主键id
     */
    private Long brandId;
    /**
     * 起始销售价格(闭合，如果会员是登录状态则查询的应该是会员价)
     */
    private Double minPrice;
    /**
     * 结束销售价格(闭合，如果会员是登录状态则查询的应该是会员价)
     */
    private Double maxPrice;
    /**
     * 会员主键id（用于查询会员价）
     */
    private Long userId;
    /**
     * 商品关键字（可模糊匹配）
     */
    private String keyword;
    /**
     * 页码
     */
    private Integer page;
    /**
     * 排序(propertyName[,desc|asc]，排序方向默认asc，这个参数支持多个以达成多条件排序)
     */
    private String sort;

}
