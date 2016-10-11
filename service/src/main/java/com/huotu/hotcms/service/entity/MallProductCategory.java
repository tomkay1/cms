package com.huotu.hotcms.service.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Arrays;
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
     * 字符串格式：1,2,5,8……
     */
    @Column(name = "MallCategoryId")
    private String mallCategoryId;

    /**
     * 品牌id
     * 字符串格式：1,2,5,8……
     */
    @Column(name = "MallBrandId")
    private String mallBrandId;
    /**
     * 商品标题
     */
    @Column(name = "GoodTitle", length = 200)
    private String goodTitle;
    /**
     * 销量
     */
    @Column(name = "SalesCount")
    private Integer salesCount;
    /**
     * 商品价格
     */
    @Column(name = "Price")
    private Double price;
    /**
     * 库存量 -1无限制
     */
    @Column(name = "Store")
    private Integer stock;
    /**
     * 冗余字段，数据源对应的contentURI
     */
    private String contentURI;

    public List<Long> getMallCategoryId() {

        return null;
    }

    public void setMallCategoryId(List<Long> mallCategoryId) {
        this.mallCategoryId = Arrays.toString(mallCategoryId.toArray()).replace("[", "").replace("]", "");
    }


}
