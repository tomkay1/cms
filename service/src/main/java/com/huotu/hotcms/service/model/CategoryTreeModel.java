package com.huotu.hotcms.service.model;

import com.huotu.hotcms.service.entity.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator xhl 2016/1/8.
 */
@Getter
@Setter
public class CategoryTreeModel extends Category {

    public CategoryTreeModel(){
        children=new ArrayList<CategoryTreeModel>();
    }

    private List<CategoryTreeModel> children;

    public void addChildren(CategoryTreeModel categoryTreeModel){
        this.children.add(categoryTreeModel);
    }

}
