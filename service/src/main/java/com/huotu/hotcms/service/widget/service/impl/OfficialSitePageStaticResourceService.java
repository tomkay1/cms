package com.huotu.hotcms.service.widget.service.impl;

import com.huotu.hotcms.service.widget.service.PageStaticResourceService;

/**
 * <p>
 *     PC 官网静态资源准备服务,用于{@link com.huotu.hotcms.service.thymeleaf.templateresource;}
 * </p>
 * @since 1.2
 * @author xhl
 */
public class OfficialSitePageStaticResourceService implements PageStaticResourceService {

    @Override
    public String getEditHtmlBox() {
        return null;
    }

    @Override
    public String getWidgetResources(String rootUrl, String version) {
        return null;
    }

    @Override
    public String getEditScript() {
        return null;
    }

    @Override
    public String getBrowseHtmlBox(String contentPath) {
        return null;
    }

    @Override
    public String getBrowseResources(String rootUrl, String version) {
        return null;
    }
}
