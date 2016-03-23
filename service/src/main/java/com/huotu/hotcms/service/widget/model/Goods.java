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
 * 商品
 * Created by cwb on 2016/3/17.
 */
@Getter
@Setter
public class Goods {

    /**
     * 商品主键id
     */
    private int id;

    /**
     * 商品名称
     */
    private String title;

    /**
     * 上架时间
     * 日期格式:"yyyy-MM-dd HH:mm:ss"
     */
    private String shelveTime;

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
     * 缩略图完整url
     */
    private String thumbnail;

    public Goods() {
    }
    public Goods(int id,String title,String shelveTime,int sales,double marketPrice,double price,double vipPrice,String thumbnail) {
        this.id = id;
        this.title = title;
        this.shelveTime = shelveTime;
        this.sales = sales;
        this.marketPrice = marketPrice;
        this.price = price;
        this.vipPrice = vipPrice;
        this.thumbnail = thumbnail;
    }
}
