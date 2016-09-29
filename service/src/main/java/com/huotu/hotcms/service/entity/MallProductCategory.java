package com.huotu.hotcms.service.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 商城产品数据源
 * Created by lhx on 2016/9/28.
 */
@Entity
@Table(name = "cms_mall_product_category")
@Setter
@Getter
public class MallProductCategory extends ProductCategory {

}
