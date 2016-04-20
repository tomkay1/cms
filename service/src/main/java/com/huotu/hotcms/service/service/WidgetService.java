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
     * <p>
     *     根据控件主体类型名称分页查找控件主体类型列表
     * </p>
     * @param name 控件主体 类型名称
     * @param page 当前请求的数据页码
     * @param pageSize 每页展示的记录数量
     * @return PageData
     * */
    PageData<WidgetType> getWidgetTypePage(String name,int page,int pageSize);

    /**
     * <p>
     *     根据控件主体名称分页查找控件主体类型列表
     * </p>
     * @param name 控件主体 类型名称
     * @param page 当前请求的数据页码
     * @param pageSize 每页展示的记录数量
     * @return PageData
     * */
    PageData<WidgetMains> getWidgetMainsPage(String name,int page,int pageSize);

    /**
     * <p>
     *     查询所有的控件主体类型
     * </p>
     * @return PageData
     * */
    List<WidgetType> findAllWidgetType();

    /**
     * <p>
     *     查找非范围的控件主体类型列表
     * </p>
     * @param scopesType 范围枚举
     * @return List
     * */
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

    /**
     * <p>
     *     查找非范围的控件主体列表
     * </p>
     * @param scopesType 范围枚举
     * @return List
     * */
    List<WidgetList> findListByNoScopesType(ScopesType scopesType);
}
