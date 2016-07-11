/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller;

import com.huotu.cms.manage.ManageTest;
import com.huotu.cms.manage.page.AdminPage;
import com.huotu.cms.manage.page.WidgetEditPage;
import com.huotu.cms.manage.page.WidgetPage;
import com.huotu.hotcms.service.entity.WidgetInfo;
import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.widget.repository.WidgetInfoRepository;
import com.huotu.hotcms.widget.service.WidgetFactoryService;
import me.jiangcai.lib.resource.service.ResourceService;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 后台控件管理的测试
 *
 * @author CJ
 */
public class WidgetInfoControllerTest extends ManageTest {

    @Autowired
    private WidgetInfoRepository widgetInfoRepository;
    @Autowired
    private WidgetFactoryService widgetFactoryService;
    @Autowired
    private ResourceService resourceService;

    @Test
    @Transactional
    public void flow() throws Exception {
        AdminPage adminPage = loginAsManage();

        WidgetPage page = adminPage.toWidget();

        // 以不同的方式添加控件
        WidgetInfo widgetInfo1 = randomWidgetInfoValue(0);
        page = page.addWidgetWithoutOwnerAndJar(widgetInfo1);

        assertThat(widgetInfoRepository.getOne(widgetInfo1.getIdentifier()))
                .isNotNull();
        assertThat(widgetInfoRepository.getOne(widgetInfo1.getIdentifier()).getType())
                .isEqualTo(widgetInfo1.getType());

        // 设置了专享商户的
        WidgetInfo widgetInfo2 = randomWidgetInfoValue(1);
        Owner owner = randomOwner();
        page.refresh();
        page = page.addWidgetWithOwner(widgetInfo2, owner);
        assertThat(widgetInfoRepository.getOne(widgetInfo2.getIdentifier()))
                .isNotNull();
        assertThat(widgetInfoRepository.getOne(widgetInfo2.getIdentifier()).getType())
                .isEqualTo(widgetInfo2.getType());
        assertThat(widgetInfoRepository.getOne(widgetInfo2.getIdentifier()).getOwner())
                .isEqualTo(owner);

        // 既设置了专享商户 又上传了jar包的
//        WidgetInfo widgetInfo3 = randomWidgetInfoValue(2);
//        Owner owner3 = randomOwner();
//        page.refresh();
//        byte[] jarData = new byte[random.nextInt(4000)+4000];
//        random.nextBytes(jarData);
//        ByteArrayInputStream buffer = new ByteArrayInputStream(jarData);
////        ByteBuffer buffer = ByteBuffer.wrap(jarData);
//        page = page.addWidgetWithOwnerAndJar(widgetInfo3,owner3,buffer);
//
//        System.out.println(driver.getPageSource());
//
//        assertThat(widgetInfoRepository.getOne(widgetInfo3.getIdentifier()))
//                .isNotNull();
//        assertThat(widgetInfoRepository.getOne(widgetInfo3.getIdentifier()).getType())
//                .isEqualTo(widgetInfo3.getType());
//        assertThat(widgetInfoRepository.getOne(widgetInfo3.getIdentifier()).getOwner())
//                .isEqualTo(owner3);
//        // 文件
//        buffer.reset();
//        assertThat(resourceService.getResource(widgetInfoRepository.getOne(widgetInfo3.getIdentifier()).getPath())
//                .getInputStream()).hasSameContentAs(buffer);

        // ok 接下来是
        // 编辑, 编辑的话 可以调整的是owner,type和enable 其他内容都是不可调整的

        //选择一个作为测试
        List<WidgetInfo> widgetInfoList = widgetInfoRepository.findAll();
        WidgetInfo widgetInfo = widgetInfoList.get(random.nextInt(widgetInfoList.size()));
        WidgetEditPage editPage = page.clickElementInTable(WidgetEditPage.class
                , ele -> widgetInfo.getIdentifier().toString().equals(ele.getAttribute("data-id")));

        editPage.assertObject(widgetInfo);

        String newType = randomDomain();
        boolean enabled = random.nextBoolean();
        page = editPage.change(null, newType, enabled);
        assertThat(widgetInfo.getType())
                .isEqualTo(newType);
        assertThat(widgetInfo.isEnabled())
                .isEqualTo(enabled);

        //找一个没在运行的家伙
        WidgetInfo idleWidgetInfo = widgetInfoList.stream()
                .filter(widgetInfo3 -> widgetFactoryService.installedStatus(widgetInfo3).isEmpty())
                .findAny().orElseThrow(() -> new AssertionError("需要存在一个没运行的控件"));

        page = page.consumeElementInTable(WidgetPage.class
                , ele -> idleWidgetInfo.getIdentifier().toString().equals(ele.getAttribute("data-id"))
                , row -> {
                    WebElement toPlayer = row.findElement(By.className("fa-play"));
                    assertThat(toPlayer.isDisplayed())
                            .isTrue();
                    toPlayer.click();
                });

        // 页面上来看应该是在运行了
        page.consumeElementInTable(WidgetPage.class
                , ele -> idleWidgetInfo.getIdentifier().toString().equals(ele.getAttribute("data-id"))
                , row -> assertThat(row.findElements(By.className("fa-play")))
                        .isEmpty());

        assertThat(widgetFactoryService.installedStatus(idleWidgetInfo))
                .isNotEmpty();

    }

    private WidgetInfo randomWidgetInfoValue(Integer seed) {
        String[][] widgets = new String[][]{
                new String[]{
                        "com.huotu.hotcms.widget.pagingWidget",
                        "pagingWidget"
                },
                new String[]{
                        "com.huotu.hotcms.widget.picCarousel",
                        "picCarousel"
                },
                new String[]{
                        "com.huotu.hotcms.widget.productList",
                        "productList"
                },
                new String[]{
                        "com.huotu.hotcms.widget.picBanner",
                        "picBanner"
                }
        };
        String[] widgetInfo;
        if (seed == null) {
            widgetInfo = widgets[random.nextInt(widgets.length)];
        } else {
            widgetInfo = widgets[seed];
        }
        WidgetInfo info = new WidgetInfo();
        // com.huotu.hotcms.widget.pagingWidget  pagingWidget 1.0-SNAPSHOT
        info.setGroupId(widgetInfo[0]);
        info.setArtifactId(widgetInfo[1]);
        info.setVersion("1.0-SNAPSHOT");
        info.setType(randomDomain());
        return info;
    }

}