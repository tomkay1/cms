package com.huotu.hotcms.service.widget.service.impl;

import com.huotu.hotcms.service.widget.service.PageStaticResourceService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * PC商城静态资源准备服务,用于{@link com.huotu.hotcms.service.thymeleaf.templateresource;}
 * </p>
 *
 * @author xhl
 * @since 1.2
 */
@Service
public class ShopPageStaticResourceService implements PageStaticResourceService {

    @Override
    public String getEditHtmlBox() {
        return "<!DOCTYPE html><html>\n" +
                "<head>\n" +
                "    <title>店铺装修-可视化编辑</title>\n" +
                "    <meta charset=\"UTF-8\" content=\"text/html\"/>\n" +
                "    <meta name=\"widget\" content=\"{config_widgetJson}\"/>\n" +
                "    <meta name=\"exists\" content=\"{config_existsPage}\"/>\n" +
                " <link  href=\"/assets/css/main.css\"  type=\"text/css\" rel=\"stylesheet\"/>\n%s" +
                " <link href=\"/assets/libs/layer/skin/layer.css\" rel=\"stylesheet\"/>" +
                "    <script src=\"/assets/seajs/sea.js\"></script>\n" +
                "    <script src=\"/assets/seajs/config.js\"></script>\n" +
                "</head>\n" +
                "<body th:style=\"'background-color:'+${pageBackGround}+'; background: url('+${pageBackImage}+')'+((${pageBackRepeat}=='no-repeat')?' no-repeat':(' '+${pageHorizontalDistance}+${pageHorizontalUnit}+' '+${pageVerticalDistance}+${pageVerticalUnit}+' '+${pageBackRepeat}))\">" +
                "%s" +
                "<div class=\"layout-area HOT-layout-add js-layout js-layout-add\" id=\"insertToLayout\">\n" +
                "    <div class=\"layout\">\n" +
                "      <a href=\"javascript:;\" class=\"link-add-layout\" id=\"addLayout\">添加布局</a>\n" +
                "    </div>\n" +
                "  </div>" +
                "%s" +
                "</body>" +
                "</html>";
    }

    @Override
    public String getWidgetResources(String rootUrl,String version) {
        String result="<link rel=\"stylesheet\" type=\"text/css\" href=\"{PREFIX}/css/mall.base.css?v={version}\"/>\n" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"{PREFIX}/css/mall.set.css?v={version}\"/>\n" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"{PREFIX}/css/mall.layout.css?v={version}\"/>\n" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"{PREFIX}/css/mall.design.css?v={version}\"/>\n" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"{PREFIX}/css/Advanced-search.css?v={version}\"/>";
        result = result.replace("{PREFIX}", rootUrl);
        result= result.replace("{version}", version);
        return result;
    }

    @Override
    public String getEditScript() {
        return  "<script>seajs.use([\"widgetTooBar\",\"cmsQueue\",\"widgetData\"]);</script>";
    }

    @Override
    public String getBrowseHtmlBox(String contentPath) {
        return "<!DOCTYPE html><html>\n" +
                "<head>\n" +
                "    <title th:text=\"(${widgetPage.pageName}==null?${site.title}:(${widgetPage.pageName}+'_'+${site.title}))\">商城首页</title>\n" +
                "    <meta charset=\"UTF-8\" content=\"text/html\"/>\n" +
                "    <meta name=\"keywords\" th:content=\"(${widgetPage.pageKeyWords}==null?${site.keywords}:${widgetPage.pageKeyWords})\" />\n" +
                "    <meta name=\"description\" th:content=\"(${widgetPage.pageDescription}==null?${site.description}:${widgetPage.pageDescription})\"/>"+
                "    <script src=\""+contentPath+"/assets/seajs/sea.js\"></script>\n" +
                "    <script src=\"\"+contentPath+\"/assets/seajs/config.js\"></script>\n" +
                "%s\n" +
                "</head>" +
                "<body th:style=\"'background-color:'+${pageBackGround}+';'+((${pageBackImage}==null)?'':'background: url('+${pageBackImage}+')'+((${pageBackRepeat}=='no-repeat')?' no-repeat':(' '+${pageHorizontalDistance}+${pageHorizontalUnit}+' '+${pageVerticalDistance}+${pageVerticalUnit}+' '+${pageBackRepeat})))\">" +
                "%s" +
                "<script>seajs.use([\"main\"]);</script>" +
                "</body>" +
                "</html>";
    }

    @Override
    public String getBrowseResources(String rootUrl,String version) {
        String result="<link rel=\"stylesheet\" type=\"text/css\" href=\"{PREFIX}/css/mall.global.css?v={version}\"/>";
        result = result.replace("{PREFIX}", rootUrl);
        result= result.replace("{version}", version);
        return result;
    }
}
