package com.huotu.hotcms.service.model;

import com.huotu.hotcms.service.entity.WidgetMains;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by xhl on 2016/3/28.
 */
@Setter
@Getter
public class WidgetList {
    /**
     * 控件主体类型ID
     * */
    private Long typeId;

    /**
     * 控件主体类型名称
     * */
    private String name;

    private List<WidgetMains> widgetMainsList;
}
