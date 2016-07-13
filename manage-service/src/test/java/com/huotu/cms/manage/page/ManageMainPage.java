/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.page;

import com.huotu.cms.manage.page.support.AbstractContentPage;
import com.huotu.cms.manage.page.support.AbstractFrameParentPage;
import com.huotu.cms.manage.page.support.BodyId;
import com.huotu.hotcms.service.entity.Site;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.springframework.core.annotation.AnnotationUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 商户管理主页面
 *
 * @author CJ
 */
public class ManageMainPage extends AbstractFrameParentPage {
    private static final Log log = LogFactory.getLog(ManageMainPage.class);

    public ManageMainPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public void validatePage() {
        assertThat(webDriver.getTitle())
                .contains("内容管理");
    }

    /**
     * 去指定页面
     *
     * @param pageClazz 页面的类型
     * @param <T>       类型参数
     * @return 新页面实例
     */
    public <T extends AbstractContentPage> T toPage(Class<? extends T> pageClazz) {
        beforeDriver();
        try {
            clickMenuByClass(AnnotationUtils.findAnnotation(pageClazz, BodyId.class).value());
        } catch (NullPointerException ex) {
            throw new IllegalStateException("必须标注BodyId 否则找不到相对的链接:" + pageClazz);
        }
        T page = initPage(pageClazz);
        page.setMainPage(this);
        return page;
    }

    public SitePage toSite() {
        beforeDriver();
        clickMenuByClass("fa-puzzle-piece");
//        clickMenuByClass("fa-sitemap");
        return initPage(SitePage.class);
    }

    public PageInfoPage toPageInfo() {
        beforeDriver();
        clickMenuByClass("fa-sitemap");
        return initPage(PageInfoPage.class);
    }


    public RoutePage toRoute() {
        beforeDriver();
        clickMenuByClass("fa-retweet");
        return initPage(RoutePage.class);
    }

    public CategoryPage toCategory() {
        beforeDriver();
        clickMenuByClass("fa-bars");
        return initPage(CategoryPage.class);
    }

    public void switchSite(Site site) {
        beforeDriver();
        // UI去点 可能会有Ajax 异步问题
        webDriver.get("http://localhost/manage/switch/" + site.getSiteId());
        webDriver.get("http://localhost/manage/main");
        reloadPageInfo();
    }

    /**
     * 从主页跳转编辑界面
     *
     * @return
     */
    public EditPage toEditPage(long pageId) {
        beforeDriver();
        webDriver.get("http://localhost/manage/edit/" + pageId);
        return initPage(EditPage.class);
    }


}
