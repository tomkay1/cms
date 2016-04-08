package com.huotu.hotcms.service.widget.model;

import java.util.List;

/**
 * Created by chendeyu on 2016/4/8.
 */
public class GoodsDetail {

    /**
     * 商品主键id
     */
    private int id;

    /**
     * 商品名称
     */
    private String title;


    /**
     * 销量
     */
    private int sales;

    /**
     * 市场价
     */
    private double marketPrice;

    /**
     * 销售价
     */
    private double price;

    /**
     *会员价
     */
    private double vipPrice;

    /**
     * 商品规格
     */
    private List<String> specification;

    /**
     * 商品标签
     * （包邮，七天无理由退换货）
     */
    private String tags;

    /**
     * 库存
     */
    private String stock;

    /**
     * 缩略图完整url
     */
    private List<String> thumbnails;

    /**
     * 缩略图完整url
     */
    private String detail;

}
