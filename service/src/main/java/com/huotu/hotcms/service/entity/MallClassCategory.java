package com.huotu.hotcms.service.entity;

import com.huotu.hotcms.service.model.MallClassCategoryModel;
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
     * 商城产品类目数据源
     */
    private List<MallProductCategory> categories;

    /**
     * 推荐链接数据源
     */
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
