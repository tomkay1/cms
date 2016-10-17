package com.huotu.hotcms.service.entity;

import com.huotu.hotcms.service.entity.converter.MallProductCategoryAttrConverter;
import com.huotu.hotcms.service.model.MallProductCategoryModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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
    @Convert(converter = MallProductCategoryAttrConverter.class)
    private List<Long> mallCategoryId;

    /**
     * 品牌id
     */
    @Column(name = "MallBrandId")
    @Convert(converter = MallProductCategoryAttrConverter.class)
    private List<Long> mallBrandId;

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

    @ManyToOne
    private Gallery gallery;
    /**
     * 冗余字段，数据源对应的contentURI
     */
    @Transient
    private String contentURI;

    public MallProductCategoryModel toMallProductCategoryModel() {
        MallProductCategoryModel mallProductCategoryModel = new MallProductCategoryModel();
        mallProductCategoryModel.setMallBrandId(this.getMallBrandId());
        mallProductCategoryModel.setMallCategoryId(this.getMallCategoryId());
        mallProductCategoryModel.setName(this.getName());
        mallProductCategoryModel.setSite(this.getSite());
        mallProductCategoryModel.setGoodTitle(this.getGoodTitle());
        mallProductCategoryModel.setMarkerTable(this.markerTable);
        mallProductCategoryModel.setDisabled(this.disabled);
        mallProductCategoryModel.setMaxPrice(this.maxPrice);
        mallProductCategoryModel.setMinPrice(this.minPrice);
        mallProductCategoryModel.setSalesCount(this.salesCount);
        mallProductCategoryModel.setSerial(this.getSerial());
        mallProductCategoryModel.setParent(this.getParent());
        mallProductCategoryModel.setContentType(this.getContentType());
        mallProductCategoryModel.setCreateTime(this.getCreateTime());
        mallProductCategoryModel.setId(this.getId());
        mallProductCategoryModel.setStock(this.stock);
        mallProductCategoryModel.setOrderWeight(this.getOrderWeight());
        mallProductCategoryModel.setContentURI(this.getContentURI());
        return mallProductCategoryModel;
    }



}
