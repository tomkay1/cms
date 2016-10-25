/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.entity;

import com.huotu.hotcms.service.model.MallClassCategoryModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
     * 商城产品类目数据源
     */
    @OneToMany
    private List<MallProductCategory> categories;

    /**
     * 推荐链接数据源
     */
    @ManyToOne
    private Category recommendCategory;

    /**
     * 是否为其他类目父级
     */
    private boolean parentFlag;

    public MallClassCategoryModel toMallClassCategoryModel() {
        MallClassCategoryModel mallClassCategoryModel = new MallClassCategoryModel();
        mallClassCategoryModel.setCategories(this.categories);
        mallClassCategoryModel.setParentFlag(this.parentFlag);
        mallClassCategoryModel.setParent(this.getParent());
        mallClassCategoryModel.setName(this.getName());
        mallClassCategoryModel.setCreateTime(this.getCreateTime());
        mallClassCategoryModel.setRecommendCategory(this.recommendCategory);
        mallClassCategoryModel.setSerial(this.getSerial());
        mallClassCategoryModel.setContentType(this.getContentType());
        mallClassCategoryModel.setId(this.getId());
        mallClassCategoryModel.setOrderWeight(this.getOrderWeight());
        mallClassCategoryModel.setSite(this.getSite());
        return mallClassCategoryModel;
    }



}
