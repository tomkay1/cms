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
   List<Category> findBySiteAndDeletedAndNameContainingOrderByOrderWeightDesc(Site site,Boolean deleted,String name);

   List<Category> findBySiteAndDeletedOrderByOrderWeightDesc(Site site,Boolean deleted);

   Set<Category> findByCustomerId(Integer customerId);

   Category findByRoute(Route route);

   List<Category> findByParentOrderByOrderWeightDesc(Category superCategory);

   List<Category> findBySite_SiteIdAndRoute_RouteTypeAndDeletedOrderByOrderWeightDesc(Long siteId, RouteType routeType, boolean b);

   List<Category> findByParent_Id(Long parenId);
}
