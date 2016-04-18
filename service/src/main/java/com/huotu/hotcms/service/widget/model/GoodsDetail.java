package com.huotu.hotcms.service.widget.model;

import com.huotu.huobanplus.common.entity.support.SpecDescriptions;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.rest.core.annotation.Description;

import javax.persistence.Column;

/**
 * Created by chendeyu on 2016/4/8.
 */
@Getter
@Setter
public class GoodsDetail {

    private Long id;

    @Description("商品编号")
    private String code;

    /**
     * 所属品牌
     */
    private String brandName;


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
     * 用户价格
     * @since 1.4
     */
    private Double[] userPrice;

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

    /**
     * 商品大图资源
     *
     * @since 1.4.2
     */
    @Column(name = "Big_Pic")
    private String bigPic;


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




}
