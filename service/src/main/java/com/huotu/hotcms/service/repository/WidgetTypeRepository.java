package com.huotu.hotcms.service.repository;

import com.huotu.hotcms.service.entity.WidgetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by chendeyu on 2016/3/17.
 */
@Repository
public interface WidgetTypeRepository extends JpaRepository<WidgetType, Long>,JpaSpecificationExecutor {
}
