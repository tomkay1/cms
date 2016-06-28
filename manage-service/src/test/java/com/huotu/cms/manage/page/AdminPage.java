/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.page;

import com.huotu.hotcms.service.entity.login.Owner;
import org.openqa.selenium.WebDriver;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 管理员登录所看到的
 *
 * @author CJ
 */
public class AdminPage extends AbstractFrameParentPage {
    public AdminPage(WebDriver webDriver) {
        super(webDriver);
    }


    @Override
    public void validatePage() {
        assertThat(webDriver.getTitle())
                .contains("后台管理");
    }

    public ManageMainPage toMainPage(Owner owner) {
        beforeDriver();
        webDriver.get("http://localhost/manage/supper/as/" + owner.getId());

        return initPage(ManageMainPage.class);
    }

    public OwnerPage toOwner() {
        beforeDriver();
        findMenuLiByClass("fa-home").click();
        return initPage(OwnerPage.class);
    }

}
