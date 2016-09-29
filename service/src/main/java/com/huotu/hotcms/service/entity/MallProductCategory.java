package com.huotu.hotcms.service.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

/**
 * 商城产品数据源
 * 可以以此过滤一批商品，而且这个过程可以再来。
 * Created by lhx on 2016/9/28.
 */
@Entity
@Table(name = "cms_mall_product_category")
@Setter
@Getter
public class MallProductCategory extends ProductCategory {

    /**
     * 类目id
     */
    @Column
    private List<Long> mallCategoryId;

    /**
     * 品牌id
     */
    @Column
    private List<Long> mallBrandId;

    /**
     * 商品标题
     */
    @Column(name = "goodTitle", length = 200)
    private String goodTitle;

    /**
     * 销量
     */
    @Column(name = "salesCount")
    private int salesCount;

    /**
     * 商品价格
     *
     * @since 1.4
     */
    @Column(name = "Price")
    private double price;

    /**
     * 库存量 -1无限制
     */
    @Column(name = "Store")
    private int stock;


}
