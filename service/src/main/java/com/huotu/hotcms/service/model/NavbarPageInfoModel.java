package com.huotu.hotcms.service.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by lhx on 2016/7/21.
 * 导航控件显示pageInfo模型
 */
@Getter
@Setter
public class NavbarPageInfoModel {
    private Long id; //pageInfo.pageId
    private String name; //pageInfo.title
    private String pagePath;
    private Long pid;
}
