/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.CategoryTreeModel;
import com.huotu.hotcms.service.model.thymeleaf.foreach.CategoryForeachParam;

import java.util.List;

/**
 * Created by chendeyu on 2015/12/31.
 */
public interface CategoryService {
    Category getCategoryById(Long id);

    Boolean save(Category category);

    List<Category> getCategoryBySiteAndDeletedAndNameContainingOrderByOrderWeightDesc(Site site, Boolean deleted, String name);

    /**
     * 把栏目列表转换成栏目节点树形结构
     *
     * @param categories 栏目列表
     * @return
     */
    List<CategoryTreeModel> ConvertCategoryTreeByCategory(List<Category> categories);

    /**
     * 栏目删除
     *
     * @param category 栏目对象
     * @return
     */
    Boolean deleteCategory(Category category);

    /**
     * 设置栏目path 路径
     *
     * @param category 栏目对象
     * @return
     */
    Boolean CategorySetParents(Category category);

    List<Category> getSpecifyCategories(String[] specifyIds);

    List<Category> getSubCategories(Long parenId, int size);

    /**
     * 根据父节点获得子栏目列表
     *
     * @param parenId 栏目父节点
     * @return
     */
    List<Category> getSubCategories(Long parenId);

    List<Category> getCategoryList(Category parent);

    /**
     * 根据categoryForeachParam 参数获得栏目列表
     *
     * @param categoryForeachParam
     * @return
     */
    List<Category> findByRouteTypeAndParentId(CategoryForeachParam categoryForeachParam);

    List<Category> findByParentIdsLike(String parentId);

    String getCategoryParentIds(Long parentId);
}
