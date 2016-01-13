package com.huotu.hotcms.service.repository;

import com.huotu.hotcms.service.common.RouteType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Route;
import com.huotu.hotcms.service.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Set;

/**
 * Created by chendeyu on 2015/12/31.
 */
public interface CategoryRepository extends JpaRepository<Category, Long>,JpaSpecificationExecutor {
   List<Category> findBySiteAndDeletedOrderByOrderWeightDesc(Site site, Boolean deleted);
   List<Category> findBySite_SiteIdAndDeletedAndIdNotOrderByOrderWeightDesc(long siteId, Boolean deleted, long categoryId);
   List<Category> findBySiteAndDeletedAndNameContainingOrderByOrderWeightDesc(Site site,Boolean deleted,String name);


   Set<Category> findByCustomerIdAndDeletedAndModelIdNotNullOrderByOrderWeightDesc(Integer customerId,boolean deleted);
   Set<Category> findByCustomerIdAndModelId(Integer customerId,Integer modelType);
   List<Category> findBySiteOrderById(Site site);
   List<Category> findBySite_SiteIdAndDeletedOrderByOrderWeightDesc(long siteId, boolean deleted);

   List<Category> findBySite_SiteIdAndDeletedAndModelIdOrderByOrderWeightDesc(long siteId, boolean deleted,Integer modelType);

   Category findByRoute(Route route);

   List<Category> findByParentOrderByOrderWeightDesc(Category superCategory);

   List<Category> findBySite_SiteIdAndRoute_RouteTypeAndDeletedOrderByOrderWeightDesc(Long siteid, RouteType routetype, boolean b);

   List<Category> findByParent_Id(Long parenId);

}
