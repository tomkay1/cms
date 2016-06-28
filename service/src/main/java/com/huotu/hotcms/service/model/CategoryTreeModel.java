/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.model;

import com.huotu.hotcms.service.entity.Category;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator xhl 2016/1/8.
 */
@Getter
@Setter
public class CategoryTreeModel extends Category {

    public String time;

    public Boolean expanded;

    /**
     * 所属路由模型
     * */
    public Integer routeType;
    private List<CategoryTreeModel> children;

    public CategoryTreeModel(){
        children=new ArrayList<CategoryTreeModel>();
    }

    public static CategoryTreeModel ConvertToCategoryTreeModel(Category category){
        CategoryTreeModel categoryTreeModel = new CategoryTreeModel();
        categoryTreeModel.setCreateTime(category.getCreateTime());
        categoryTreeModel.setCustom(category.isCustom());
        categoryTreeModel.setDeleted(category.isDeleted());
        categoryTreeModel.setId(category.getId());
        categoryTreeModel.setModelId(category.getModelId());
        categoryTreeModel.setName(category.getName());
        categoryTreeModel.setOrderWeight(category.getOrderWeight());
        categoryTreeModel.setParent(category.getParent());
        categoryTreeModel.setParentIds(category.getParentIds());
//        if(category.getRoute()!=null&&category.getRoute().getRouteType()!=null) {
//            categoryTreeModel.setRouteType(category.getRoute().getRouteType().getCode());
//        }
//        categoryTreeModel.setRouteType(category.getRoute()!=null?category.getRoute().getRouteType():null);
//        categoryTreeModel.setRoute(null);
//        categoryTreeModel.setSite(null);
        categoryTreeModel.setExpanded(true);
        categoryTreeModel.setTime(category.getCreateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        categoryTreeModel.setUpdateTime(category.getUpdateTime());
        return categoryTreeModel;
    }

    public static List<CategoryTreeModel> setEmptyCategoryTreeModel(List<CategoryTreeModel> categoryTreeModelList){
        for(CategoryTreeModel categoryTreeModel:categoryTreeModelList){
            if(categoryTreeModel!=null)
            {
//                if(categoryTreeModel.getRoute()!=null) {
//                    categoryTreeModel.setRouteType(categoryTreeModel.getRoute().getRouteType().getCode());
//                }
//                categoryTreeModel.setRoute(null);
                categoryTreeModel.setSite(null);
                if(categoryTreeModel.getChildren()!=null&&categoryTreeModel.getChildren().size()>=0){
                   categoryTreeModel.setChildren(setEmptyCategoryTreeModel(categoryTreeModel.getChildren()));
                }
                if(categoryTreeModel.getParent()!=null && categoryTreeModel.getParent()!=null){
                    categoryTreeModel.getParent().setSite(null);
//                    categoryTreeModel.getParent().setRoute(null);
//                    categoryTreeModel.setParent(setEmptyCategoryTreeModel(categoryTreeModel.getParent()));
                }
            }
        }
        return categoryTreeModelList;
    }

    public void addChildren(Category category) {
        if (category != null) {
            CategoryTreeModel categoryTreeModel = new CategoryTreeModel();
            categoryTreeModel.setCreateTime(category.getCreateTime());
            categoryTreeModel.setCustom(category.isCustom());
            categoryTreeModel.setDeleted(category.isDeleted());
            categoryTreeModel.setId(category.getId());
            categoryTreeModel.setModelId(category.getModelId());
            categoryTreeModel.setName(category.getName());
            categoryTreeModel.setOrderWeight(category.getOrderWeight());
            categoryTreeModel.setParent(category.getParent());
            categoryTreeModel.setParentIds(category.getParentIds());
//            if (category.getRoute() != null && category.getRoute().getRouteType() != null) {
//                categoryTreeModel.setRouteType(category.getRoute().getRouteType().getCode());
//            }
//            categoryTreeModel.setRoute(null);
//            categoryTreeModel.setSite(null);
            categoryTreeModel.setExpanded(true);
            categoryTreeModel.setUpdateTime(category.getUpdateTime());
//            String time=category.getCreateTime().getYear()+"-"+category.getCreateTime().getMonthValue()+"-"+category.getCreateTime().getDayOfMonth();
            categoryTreeModel.setTime(category.getCreateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            this.children.add(categoryTreeModel);
        }
    }
//
//    public static CategoryTreeModel setEmpty(CategoryTreeModel categoryTreeModel){
//        if(categoryTreeModel!=null){
//            categoryTreeModel.setRoute(null);
//            categoryTreeModel.setSite(null);
//            if(categoryTreeModel.getChildren()!=null&&categoryTreeModel.getChildren().size()>=){
//
//            }
//        }
//        return null;
//    }
}
