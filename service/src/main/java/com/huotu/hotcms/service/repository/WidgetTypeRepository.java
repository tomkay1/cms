package com.huotu.hotcms.service.repository;

import com.huotu.hotcms.service.common.ScopesType;
import com.huotu.hotcms.service.entity.WidgetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by chendeyu on 2016/3/17.
 */
@Repository
public interface WidgetTypeRepository extends JpaRepository<WidgetType, Long>,JpaSpecificationExecutor {

    /**
     * <p>
     *     查找非指定范畴的控件主体类型列表
     * </p>
     * @param scopesType 控件主体范围类型
     * @return WidgetType列表对象
     * */
    @Query(value = "select * from cms_widgetType u where u.scenes<>?1",nativeQuery = true)
    List<WidgetType> findAllByScopesTypeNot(Integer scopesType);

}
