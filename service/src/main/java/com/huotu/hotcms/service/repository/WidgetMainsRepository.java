package com.huotu.hotcms.service.repository;

import com.huotu.hotcms.service.entity.WidgetMains;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by chendeyu on 2016/3/17.
 */
@Repository
public interface WidgetMainsRepository extends JpaRepository<WidgetMains, Long>,JpaSpecificationExecutor {
    List<WidgetMains> findWidgetMainsByWidgetTypeId(Long id);
}
