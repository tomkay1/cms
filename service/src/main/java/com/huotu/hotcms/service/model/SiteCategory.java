package com.huotu.hotcms.service.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by chendeyu on 2016/1/7.
 */

@Setter
@Getter
public class SiteCategory {

    private Long siteId;

    private Long categoryId;

    private String siteName;

    private String categoryName;

}
