/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.exception.BadCategoryInfoException;
import com.huotu.hotcms.service.model.CategoryTreeModel;
import com.huotu.hotcms.service.model.thymeleaf.foreach.CategoryForeachParam;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CategoryService {

    /**
     * 获取指定站点所有数据源
     * 应该是按照{@link Category#orderWeight}进行排序
     *
     * @param site 站点
     * @return 数据源集合
     */
    List<Category> getCategories(Site site);

    /**
     * 物理删除数据源,如果删除的是一个其他数据源的父级,那么将会删除所有旗下数据源
     * 其他联机操作不在此列
     *
     * @param category 数据源
     */
    @Transactional
    void delete(Category category);


    /**
     * 拿到一个,拿不到就报错
     *
     * @param id pk
     * @return 实例
     */
    Category get(long id);

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

    /**
     * 查找指定站点指定模型的数据源
     *
     * @param site
     * @param contentType
     * @return
     */
    Iterable<Category> getCategoriesForContentType(Site site, ContentType contentType);

    /**
     * 找到指定名字的数据源
     *
     * @param site             特定站点
     * @param name             指定的名字
     * @param parentCategoryId 父级数据源,可以为null表示不设置父级,同样需要符合模型要求
     * @param type             要求的模型
     * @return 必然会返回一个实例, 如果没有也会根据情况创建
     * @throws BadCategoryInfoException 如果指定名字的数据源已存在,并且不符合要求的父级数据源和模型
     */
    @Transactional
    Category getCategoryByNameAndParent(Site site, String name, Long parentCategoryId, ContentType type)
            throws BadCategoryInfoException;

    /**
     * 初始化一个数据源,每一个数据源只可能被初始化一次
     *
     * @param category
     */
    void init(Category category);
}
