package com.huotu.hotcms.service.repository;

import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by chendeyu on 2015/12/31.
 */
public interface CategoryRepository extends JpaRepository<Category, Long>,JpaSpecificationExecutor {
   List<Category> findBySiteAndDeletedOrderByOrderWeightDesc(Site site, Boolean deleted);
   List<Category> findBySite_SiteIdAndDeletedAndIdNotOrderByOrderWeightDesc(long siteId, Boolean deleted, long categoryId);
   Set<Category> findByCustomerId(Integer customerId);

   List<Category> findByIdInAndDeletedOrderByOrderWeightDesc(Collection<Long> categoryIds,Boolean deleted);
}
