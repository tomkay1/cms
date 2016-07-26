package com.huotu.hotcms.service.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhx on 2016/7/21.
 * 导航控件显示pageInfo模型
 */
@Getter
@Setter
public class NavbarPageInfoModel {
    private Long pageId; //pageInfo.pageId
    private String text; //pageInfo.title
    private String href; //pageInfo.pagePath
    private Long parentId;
    private List<NavbarPageInfoModel> nodes = new ArrayList<>();//子模型
}
