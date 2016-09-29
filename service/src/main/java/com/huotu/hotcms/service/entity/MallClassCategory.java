package com.huotu.hotcms.service.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

/**
 * 商城类目归纳数据源
 * 可以以此过滤一批商城类目，而且这个过程可以再来。
 * Created by lhx on 2016/9/29.
 */
@Entity
@Table(name = "cms_mall_class_category")
@Setter
@Getter
public class MallClassCategory extends Category {

    /**
     * 商城类目或品牌数据源id
     * map key商城类目id，value 类目名称
     */
    private List<MallProductCategory> categories;
    // 电脑 办公

    /**
     *
     */
    private Category recommendCategory;



}
