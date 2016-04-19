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

import java.util.ArrayList;
import java.util.List;

/**
 * 商品分类模型
 * Created by cwb on 2016/3/17.
 */
@Getter
@Setter
public class GoodsCategory {
    /**
     * 商品分类id
     */
    private int id;
    /**
     * 商品分类名称
     */
    private String name;
    /**
     * 该商品分类下的所有一级子分类
     */
    private List<GoodsCategory> children;
    /**
     * 关联的商品类型的id
     */
    private Integer goodsTypeId;
    /**
     * 完整的图片url
     */
    private String picUrl;

    private Link _links;

    public GoodsCategory() {
    }
    public GoodsCategory(int id,String name,Integer goodsTypeId,String picUrl) {
        this.id = id;
        this.name = name;
        this.goodsTypeId = goodsTypeId;
        this.picUrl = picUrl;
    }

    public void addChild(GoodsCategory goodsCategory) {
        if(children==null||children.size()==0) {
            children = new ArrayList<>();
        }
        children.add(goodsCategory);
    }
}
