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

    public CategoryTreeModel(){
        children=new ArrayList<CategoryTreeModel>();
    }

    private List<CategoryTreeModel> children;

    public void addChildren(Category category){
        if(category!=null) {
            CategoryTreeModel categoryTreeModel = new CategoryTreeModel();
            categoryTreeModel.setCreateTime(category.getCreateTime());
            categoryTreeModel.setCustomerId(category.getCustomerId());
            categoryTreeModel.setCustom(category.isCustom());
            categoryTreeModel.setDeleted(category.isDeleted());
            categoryTreeModel.setId(category.getId());
            categoryTreeModel.setModelId(category.getModelId());
            categoryTreeModel.setName(category.getName());
            categoryTreeModel.setOrderWeight(category.getOrderWeight());
            categoryTreeModel.setParent(category.getParent());
            categoryTreeModel.setParentIds(category.getParentIds());
            categoryTreeModel.setRoute(null);
            categoryTreeModel.setSite(null);
            categoryTreeModel.setUpdateTime(category.getUpdateTime());
//            String time=category.getCreateTime().getYear()+"-"+category.getCreateTime().getMonthValue()+"-"+category.getCreateTime().getDayOfMonth();
            categoryTreeModel.setTime(category.getCreateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            this.children.add(categoryTreeModel);
        };
        return;
    }

    public static CategoryTreeModel ConvertToCateboryTreeModel(Category category){
        CategoryTreeModel categoryTreeModel = new CategoryTreeModel();
        categoryTreeModel.setCreateTime(category.getCreateTime());
        categoryTreeModel.setCustomerId(category.getCustomerId());
        categoryTreeModel.setCustom(category.isCustom());
        categoryTreeModel.setDeleted(category.isDeleted());
        categoryTreeModel.setId(category.getId());
        categoryTreeModel.setModelId(category.getModelId());
        categoryTreeModel.setName(category.getName());
        categoryTreeModel.setOrderWeight(category.getOrderWeight());
        categoryTreeModel.setParent(category.getParent());
        categoryTreeModel.setParentIds(category.getParentIds());
        categoryTreeModel.setRoute(null);
        categoryTreeModel.setSite(null);
//        category.getCreateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
//        String time=category.getCreateTime().getYear()+"-"+category.getCreateTime().getMonthValue()+"-"+category.getCreateTime().getDayOfMonth();
        categoryTreeModel.setTime(category.getCreateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        categoryTreeModel.setUpdateTime(category.getUpdateTime());
        return categoryTreeModel;
    }
}
