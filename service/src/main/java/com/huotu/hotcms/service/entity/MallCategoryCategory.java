package com.huotu.hotcms.service.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;
import java.util.Map;

/**
 * 商城类目数据源
 * 可以以此过滤一批商城类目，而且这个过程可以再来。
 * Created by lhx on 2016/9/29.
 */
@Entity
@Table(name = "cms_mall_category_category")
@Setter
@Getter
public class MallCategoryCategory extends Category {

    /**
     * 商城类目或品牌数据源id
     * map key商城类目id，value 类目名称
     */
    private List<Map<Long, String>> mallCategory;

    /**
     * 类目商品数量
     */
    @Column(name = "goodCount")
    private Integer goodCount;

}
