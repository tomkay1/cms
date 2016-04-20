package com.huotu.hotcms.service.service;

/**
 * Created by chendeyu on 2016/3/17.
 */

import com.huotu.hotcms.service.common.ScopesType;
import com.huotu.hotcms.service.entity.WidgetMains;
import com.huotu.hotcms.service.entity.WidgetType;
import com.huotu.hotcms.service.model.WidgetList;
import com.huotu.hotcms.service.util.PageData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public  interface WidgetService {
    /**
     *
     * 获取控件分类分页信息
     * @param name
     * @param page
     * @param pageSize
     * @return
     */
    PageData<WidgetType> getWidgetTypePage(String name,int page,int pageSize);

    /**
     *
     * 获取控件主体分页信息
     * @param name
     * @param page
     * @param pageSize
     * @return
     */
    PageData<WidgetMains> getWidgetMainsPage(String name,int page,int pageSize);

    List<WidgetType> findAllWidgetType();

    /**
     *
     * 根据控件适用类型查找控件
     * @param scopesType
     * @return
     */
    List<WidgetType> findAllWidgetTypeByNoScopesType(ScopesType scopesType);

    Boolean saveWidgetType(WidgetType widgetType);

    void delWidgetType(Long id);

    void delWidgetMains(Long id);


    Boolean saveWidgetMains(WidgetMains widgetMains);

    WidgetType findWidgetTypeById(Long id);

    WidgetMains findWidgetMainsById(Long id);

    /**
     * 根据控件类型查找控件主体
     *
     * @param id
     * @return
     */
    List<WidgetMains> findWidgetMainsByWidgetTypeId(Long id);

    List<WidgetList> findList();

    List<WidgetList> findListByNoScopesType(ScopesType scopesType);
}
