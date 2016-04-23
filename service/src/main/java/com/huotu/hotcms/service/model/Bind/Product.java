package com.huotu.hotcms.service.model.Bind;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.rest.core.annotation.Description;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * Created by chendeyu on 2016/4/22.
 */

@Getter
@Setter
public class Product {

    @Id
    @Column(name = "Product_Id")
    private Long id;


    @Description("货品编号")
    @Column(name = "Bn", length = 30)
    private String code;

    @Description("成本价")
    @Column(name = "Cost")
    private double costPrice;

    @Description("销售价")
    @Column(name = "Mktprice")
    private double marketPrice;

    /**
     * 规格说明
     * 应该是一个数组
     */
    @Column(name = "Pdt_Desc")
    private String spec;

    /**
     * 名称
     */
    @Column(name = "Name")
    private String name;

    /**
     * 价格
     */
    @Column(name = "Price")
    private double price;



    /**
     * 库存
     */
    @Column(name = "Store")
    private int stock;


}
