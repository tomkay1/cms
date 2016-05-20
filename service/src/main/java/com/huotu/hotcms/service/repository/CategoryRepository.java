package com.huotu.hotcms.service.repository;

import com.huotu.hotcms.service.common.RouteType;
import com.huotu.hotcms.service.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Set;

/**
 * Created by chendeyu on 2015/12/31.
 */
public interface CategoryRepository extends JpaRepository<Category, Long>,JpaSpecificationExecutor {

   /**
    *  根据序列号和站点信息进行查询
    * @param serial 序列号
    * @param site 站点 {@link com.huotu.hotcms.service.entity.Site}
    * @return category {@link com.huotu.hotcms.service.entity.Category}
    */
   Category findBySerialAndSite(String serial,Site site);

   /**
    * 获取某站点下的所有栏目
    * @param site 站点
    * @return 所有栏目
    */
   List<Category> findBySite(Site site);

   /**
    * 根据站点查询栏目
    */
   List<Category> findBySiteAndDeletedOrderByOrderWeightDesc(Site site, Boolean deleted);

   /**
    * 根据站点,以及栏目名查询栏目
    */
   List<Category> findBySiteAndDeletedAndNameContainingOrderByOrderWeightDesc(Site site,Boolean deleted,String name);

   /**
    * 根据商户,站点查询栏目
    */
   List<Category> findByCustomerIdAndSite_SiteIdAndDeletedAndModelIdNotNullOrderByOrderWeightDesc(Integer customerId,Long siteId,boolean deleted);

   /**
    * 根据商户,模型,以及栏目名查询栏目
    */
   Set<Category> findByCustomerIdAndModelId(Integer customerId,Integer modelType);

   /**
    * 根据站点查询栏目
    */
   List<Category> findBySiteAndDeletedAndModelIdNotNullOrderByOrderWeightDesc(Site site,boolean deleted);

   /**
    * 根据路由查询栏目
    */
   Category findByRoute(Route route);

   /**
    * 根据父栏目查询栏目
    */
   List<Category> findByParentOrderByOrderWeightDesc(Category superCategory);

   /**
    * 根据站点,路由类型,父栏目查询栏目
    */
   List<Category> findBySite_SiteIdAndRoute_RouteTypeAndDeletedAndParentOrderByOrderWeightDesc(Long siteid, RouteType routetype, boolean b, Category parent);

   /**
    * 根据父栏目查询栏目
    */
   List<Category> findByParent_Id(Long parenId);

    /**
     * 根据站点,路由类型,父栏目查询栏目
     */
   List<Category> findBySite_SiteIdAndRoute_RouteTypeAndDeletedOrderByOrderWeightDesc(Long siteid, RouteType routetype, boolean b);

    /**
     * 根据父栏目查询栏目
     */
   List<Category> findByParentIdsContainingAndDeleted(String parentId,boolean deleted);
}
