package com.huotu.hotcms.service.repository;

import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;

/**
 * Created by chendeyu on 2015/12/31.
 */
public interface CategoryRepository extends JpaRepository<Category, Long>,JpaSpecificationExecutor {
   Set<Category> findBySiteAndDeleted(Site site,Boolean deleted);
   Set<Category> findByCustomerId(Integer customerId);
}
