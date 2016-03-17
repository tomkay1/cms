package com.huotu.hotcms.service.service;

/**
 * Created by chendeyu on 2016/3/17.
 */

import com.huotu.hotcms.service.entity.WidgetMains;
import com.huotu.hotcms.service.entity.WidgetType;
import com.huotu.hotcms.service.util.PageData;
import org.springframework.stereotype.Service;

@Service
public  interface WidgetService {

    PageData<WidgetType> getWidgetTypePage(String name,int page,int pageSize);

    Boolean saveWidgetType(WidgetType widgetType);

    void delWidgetType(Long id);

    void delWidgetMains(Long id);


    Boolean saveWidgetMains(WidgetType widgetType);

    WidgetType findWidgetTypeById(Long id);

    WidgetMains findWidgetMainsById(Long id);
}
