package com.huotu.hotcms.service.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.NumberUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
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
     * 最低销量
     */
    @Column(name = "SalesCount")
    private Integer salesCount;
    /**
     * 商品价格最低价
     */
    @Column(name = "minPrice")
    private Double minPrice;
    /**
     * 商品价格最高价
     */
    @Column(name = "maxPrice")
    private Double maxPrice;

    /**
     * 最低库存量 -1无限制
     */
    @Column(name = "Store")
    private Integer stock;

    /**
     * 是否删除商品
     */
    @Column(name = "Disabled")
    private Boolean disabled;

    /**
     * 是否上架
     */
    @Column(name = "markerTable")
    private Boolean markerTable;

    /**
     * 冗余字段，数据源对应的contentURI
     */
    private String contentURI;

    public List<Long> getMallCategoryId() {
        return toList(this.mallCategoryId);
    }

    public void setMallCategoryId(List<Long> mallCategoryId) {
        if (null == mallCategoryId)
            return;
        this.mallCategoryId = Arrays.toString(mallCategoryId.toArray()).replace("[", "").replace("]", "");
    }


    public List<Long> getMallBrandId() {
        return toList(this.mallBrandId);
    }

    public void setMallBrandId(List<Long> mallBrandId) {
        if (null == mallBrandId)
            return;
        this.mallBrandId = Arrays.toString(mallBrandId.toArray()).replace("[", "").replace("]", "");
    }


    private List<Long> toList(String strs) {
        if (strs == null || strs.equals(""))
            return null;
        else {
            String[] array = strs.split(",");
            List<Long> list = new ArrayList<>();
            for (String str : array) {
                list.add(NumberUtils.parseNumber(str, Long.class));
            }
            return list;
        }
    }

}
