package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.common.RouteType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Route;
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

    List<Category>  getCategoryBySiteAndDeletedAndNameContainingOrderByOrderWeightDesc(Site site,Boolean deleted,String name);

    /**
     * 把栏目列表转换成栏目节点树形结构
     *
     * @param categories 栏目列表
     * @return
     */
    List<CategoryTreeModel> ConvertCategoryTreeByCategory(List<Category> categories);

    /**
     * 保存更新栏目
     * @param category 栏目信息对象
     * @param route 路由规则
     * @param template 模版资源
     * @param routeType 路由类型
     * @return
     * @throws Exception
     */
    Boolean saveCategoryAndRoute(Category category,String route,String template,RouteType routeType) throws Exception;

    /**
     * 更新栏目路由规则
     * @param category 栏目对象
     * @param rule 路由规则
     * @param template 路由模版资源
     * @param noRule
     * @param routeType 路由类型
     * @return
     */
    Boolean updateCategoryAndRoute(Category category, String rule, String template,String noRule,RouteType routeType);

    /**
     * 栏目删除
     * @param category 栏目对象
     * @return
     */
    Boolean deleteCategory(Category category);

    /**
     * 根据路由来获得栏目信息对象
     *
     * @param route 路由信息对象
     * @return
     */
    Category getCategoryByRoute(Route route);

    /**
     * 设置栏目path 路径
     *
     * @param category 栏目对象
     * @return
     */
    Boolean CategorySetParents(Category category);

    List<Category> getSpecifyCategories(String[] specifyIds);

    List<Category> getGivenTypeCategories(CategoryForeachParam param);

    List<Category> getSubCategories(Long parenId,int size);

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

    List<Category> getHeaderCategoryList(CategoryForeachParam param);

    List<Category> findByParentIdsLike(String parentId);

    String getCategoryParentIds(Long parentId);
}
