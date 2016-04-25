package com.huotu.hotcms.service.widget.service;

/**
 * <p>
 * PC商城、PC官网静态资源准备服务,用于{@link com.huotu.hotcms.service.thymeleaf.templateresource;}
 * </p>
 *
 * @author xhl
 * @since 1.0
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
     *     获得控件主体使用的静态资源的引用
     * </p>
     * @param rootUrl 控件主体根Uri
     * @param version 版本号
     * @return 返回控件主体引用的静态资源(css,js)等
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
     * @return 返回最终的浏览视图的一个Html容器模版
     * */
    String getBrowseHtmlBox(String contentPath);

    /**
     * <p>
     *     获得最终浏览视图额外需要的静态资源引用
     * </p>
     * @param rootUrl 控件主体根Uri
     * @param version 版本号
     * @return 返回最终预览视图引用的静态资源(css,js),
     *         一般该静态资源用于重写组件的样式，把编辑的相关样式重写以达到最佳视觉效果
     * */
    String getBrowseResources(String rootUrl,String version);
}
