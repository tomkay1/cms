/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller;

import com.huotu.cms.manage.SiteManageTest;
import com.huotu.cms.manage.page.ManageMainPage;
import com.huotu.cms.manage.page.PageInfoPage;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.PageInfo;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.repository.PageInfoRepository;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author CJ
 */
public class PageInfoControllerTest extends SiteManageTest {

    @Autowired
    private PageInfoRepository pageInfoRepository;

    @Test
    @Transactional
    public void flow() throws Exception {
        Site site = loginAsSite();

        // 添加一个数据源 用于测试
        Category category = random.nextBoolean() ? randomCategory(site) : null;
        List<PageInfo> oldPageInfoList = pageInfoRepository.findBySite(site);

        ManageMainPage manageMainPage = initPage(ManageMainPage.class);

        PageInfoPage page = manageMainPage.toPageInfo();

        PageInfo randomPageInfoValue = randomPageInfoValue();
        PageInfoPage listPage = page.addPage(randomPageInfoValue, category);

        // 也能找到这个页面
        // 1 是确保站点的数量 2 是确保站点的信息 所以必须逐个检查。
        List<PageInfo> pageInfoList = pageInfoRepository.findBySite(site);

        assertThat(pageInfoList)
                .hasSize(oldPageInfoList.size() + 1);

        List<WebElement> list = listPage.listTableRows();
        assertThat(list)
                .hasSize(pageInfoList.size());

        for (PageInfo pageInfo : pageInfoList) {
            WebElement rowElement = list.stream()
                    .filter(listPage.findRow(pageInfo))
                    .findAny().orElseThrow(() -> {
                        listPage.printThisPage();
                        return new IllegalStateException("应该显示该页面" + pageInfo.getPageId());
                    });
            assertThat(rowElement)
                    .is(listPage.rowCondition(pageInfo));
        }


    }


}