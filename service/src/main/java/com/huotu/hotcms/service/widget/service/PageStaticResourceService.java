package com.huotu.hotcms.service.widget.service;

/**
 * Created by Administrator on 2016/4/25.
 */
public interface PageStaticResourceService {
    /**
     * <p>
     *     获得编辑时的Html容器html，里面包含编辑时包含的一些静态资源和必要的元素
     * </p>
     * */
    String getEditHtmlBox();

    /**
     * <p>
     *     获得控件主体使用的静态资源引用
     * </p>
     * */
    String getWidgetResources(String rootUrl,String version);

    /**
     * <p>
     *     获得编辑时使用的js引用入口
     * </p>
     * */
    String getEditScript();

    /**
     * <p>
     *     获得最终预览时的Html容器html，里面包含预览时包含的一些静态资源和必要的元素
     * </p>
     * @param contentPath 根地址
     * */
    String getBrowseHtmlBox(String contentPath);

    /**
     * <p>
     *     获得最终浏览视图额外需要的静态资源引用
     * </p>
     * */
    String getBrowseResources(String rootUrl,String version);
}
