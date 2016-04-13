/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.widget.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.rest.core.annotation.Description;

import java.util.Objects;

/**
 * 商品
 *
 * @author CJ
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Goods {

    private Long id;

    @Description("商品编号")
    private String code;


    /**
     * 商品场景,按照建模分析应该是一个枚举,但为了保证这个字段拥有充分的可扩展性,只是给予文字描述约束
     * <p>
     * 0 普通商品
     * 1 限时团购商品
     * <p>
     * since 2015.11.6
     */
    private int scenes;


    /**
     * 商品标题
     */
    private String title;


    /**
     * 库存量 -1无限制
     */
    private int stock;

    /**
     * 可售状态
     */
    private boolean marketable;

    /**
     * 市场价
     * @since 1.4
     *
     */
    private double marketPrice;

    /**
     * 商品价格
     * @since 1.4
     */
    private double price;
    /**
     * 成本价
     */
    private double cost;

    /**
     * 是否已禁用
     */
    private boolean disabled;

    /**
     * 商品小图资源路径 这个路径需要跟相关资源主目录相关。
     */
    private String smallPic;

    /**
     * 缩略图
     * <p>相对商城资源的path</p>
     * @since 1.4
     */
    private String thumbnailPic;


    @Description("商品详情")
    private String intro;

    @Description("商品规格详情")
    private SpecDescriptions specDescriptions;

    @Description("商品规格")
    private String spec;

    /**
     * 销量,据说不太准确,交易成功就会增加,但被取消也不会减少
     * @since 1.4
     */
    @Description("销售量")
    private int salesCount;

    /**
     * @since 1.4
     */
    @Description("类型Id")
    private Integer typeId;

    /**
     * @since 1.4
     */
    @Description("商品类型")
    private String goodsType;

    /**
     * @since 1.4
     */
    @Description("简介")
    private String brief;

    private Link _links;

    @Override
    public String toString() {
        return "Goods{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", scenes=" + scenes +
//                ", category=" + category +
                ", title='" + title + '\'' +
//                ", owner=" + owner +
                ", stock=" + stock +
                ", marketable=" + marketable +
                ", marketPrice=" + marketPrice +
                ", price=" + price +
                ", cost=" + cost +
                ", disabled=" + disabled +
                ", smallPic='" + smallPic + '\'' +
                ", thumbnailPic='" + thumbnailPic + '\'' +
                ", SpecDescriptions=" + specDescriptions +
                ", spec=" + spec +
                ", salesCount=" + salesCount +
                ", typeId=" + typeId +
                ", goodsType=" + goodsType +
                ", brief=" + brief +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Goods goods = (Goods) o;
        if (getId() != null)
            return Objects.equals(getId(), goods.getId());
        return Objects.equals(scenes, goods.scenes) &&
                Objects.equals(stock, goods.stock) &&
                Objects.equals(marketable, goods.marketable) &&
                Objects.equals(marketPrice, goods.marketPrice) &&
                Objects.equals(price, goods.price) &&
                Objects.equals(cost, goods.cost) &&
                Objects.equals(disabled, goods.disabled) &&
                Objects.equals(id, goods.id) &&
                Objects.equals(code, goods.code) &&
                Objects.equals(title, goods.title) &&
                Objects.equals(smallPic, goods.smallPic) &&
                Objects.equals(thumbnailPic, goods.thumbnailPic) &&
                Objects.equals(specDescriptions, goods.specDescriptions) &&
                Objects.equals(spec, goods.spec) &&
                Objects.equals(salesCount, goods.salesCount) &&
                Objects.equals(typeId, goods.typeId) &&
                Objects.equals(goodsType, goods.goodsType) &&
                Objects.equals(brief, goods.brief);
    }

    @Override
    public int hashCode() {
        if (getId() != null)
            return Objects.hash(getId());
        return Objects.hash(id, code,scenes, title, stock, marketable,
                marketPrice, price, cost, disabled, smallPic, thumbnailPic, specDescriptions,spec, salesCount, typeId, goodsType, brief);
    }
}