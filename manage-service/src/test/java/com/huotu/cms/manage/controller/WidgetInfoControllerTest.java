/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.huotu.cms.manage.ManageTest;
import com.huotu.cms.manage.controller.support.CRUDHelper;
import com.huotu.cms.manage.controller.support.CRUDTest;
import com.huotu.cms.manage.page.AdminPage;
import com.huotu.cms.manage.page.WidgetEditPage;
import com.huotu.cms.manage.page.WidgetPage;
import com.huotu.cms.manage.page.support.AbstractCRUDPage;
import com.huotu.hotcms.service.entity.WidgetInfo;
import com.huotu.hotcms.service.entity.login.Login;
import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.widget.InstalledWidget;
import com.huotu.hotcms.widget.repository.WidgetInfoRepository;
import com.huotu.hotcms.widget.service.WidgetFactoryService;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 后台控件管理的测试
 *
 * @author CJ
 */
public class WidgetInfoControllerTest extends ManageTest {

    private static final Log log = LogFactory.getLog(WidgetInfoControllerTest.class);

    @Autowired
    private WidgetInfoRepository widgetInfoRepository;
    @Autowired
    private WidgetFactoryService widgetFactoryService;

    @Test
    @Transactional
    public void flow() throws Exception {
        AdminPage adminPage = loginAsManage();

        WidgetPage page = adminPage.toPage(WidgetPage.class);
        addWidgetInfo(page);
        addWidgetInfo(page);

        page.refresh();
//        WidgetPage page = adminPage.toWidget();

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

    private void addWidgetInfo(WidgetPage widgetPage) throws Exception {
        CRUDHelper.flow(widgetPage, new CRUDTest<WidgetInfo>() {
            @Override
            public Collection<WidgetInfo> list() {
                return widgetInfoRepository.findAll();
            }

            @Override
            public WidgetInfo randomValue() {
                for (int i = 0; i < widgets.length; i++) {
                    WidgetInfo info = randomWidgetInfoValue(i);
                    if (widgetInfoRepository.findOne(info.getIdentifier()) == null)
                        return info;
                }
                throw new IllegalStateException("没有可用的控件了!");
            }

            @Override
            public BiConsumer<AbstractCRUDPage<WidgetInfo>, WidgetInfo> customAddFunction() {
                // 是否要上传jar?
                return null;
            }

            @Override
            public void assertCreation(WidgetInfo entity, WidgetInfo data) {
                assertThat(entity.getType())
                        .isEqualTo(data.getType());
                assertThat(entity.getCreateTime())
                        .isNotNull();
                assertThat(entity.getArtifactId())
                        .isEqualTo(data.getArtifactId());
                assertThat(entity.getGroupId())
                        .isEqualTo(data.getGroupId());
                assertThat(entity.getOwner())
                        .isEqualTo(data.getOwner());
                assertThat(entity.getVersion())
                        .isEqualTo(data.getVersion());
            }
        });
    }

    private JsonNode assertMvcArrayNotEmpty(String uri) throws Exception {
        return objectMapper.readTree(
                mockMvc.perform(get(uri)
                        .session(session)
                        .accept(MediaType.APPLICATION_JSON)
                )
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$").isArray())
                        .andExpect(jsonPath("$.[0]").isMap())
                        .andReturn().getResponse().getContentAsString());
    }


    /**
     * 对widget json进行校验
     *
     * @throws Exception
     * @see WidgetInfoController#getWidgetInfo(Locale, Login)
     */
    @Test
    @Transactional
    public void testGetWidgets() throws Exception {

        Owner owner = randomOwner();
        loginAsOwner(owner);

        widgetFactoryService.installWidgetInfo(null, "com.huotu.hotcms.widget.copyright", "copyright"
                , "1.0-SNAPSHOT", "普通");
        widgetFactoryService.installWidgetInfo(null, "com.huotu.hotcms.widget.copyright", "copyright"
                , "1.0.2-SNAPSHOT", "普通");

        /*先确保存在已安装的控件*/
        List<InstalledWidget> installedWidgets = widgetFactoryService.widgetList(null);
//        if (installedWidgets.size() == 0) {
//            widgetFactoryService.installWidgetInfo(null, "com.huotu.hotcms.widget.picCarousel", "picCarousel"
//                    , "1.0-SNAPSHOT", "picCarousel");
//        }

        JsonNode widgets = assertMvcArrayNotEmpty("/manage/widget/widgets");
        assertSimilarJsonArray(widgets, new ClassPathResource("web/public/assets/js/data/widget.json")
                .getInputStream());

        MvcResult result = mockMvc.perform(get("/manage/widget/widgets").session(session))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String widgetJson = result.getResponse().getContentAsString();
        log.info("获取到的json：" + widgetJson);
        assertThat(widgetJson)
                .isNotEmpty();
        //identity的格式:<groupId>-<widgetId>:<version>
        //此处校验逻辑为：先检索出所有的identity，如果存在groupId和widgetId 一致，但有两个版本号的，视为bug！
        List<String> identities = JsonPath.read(widgetJson, "$..identity");
        assertThat(identities)
                .isNotEmpty()
                .containsOnlyOnce("com.huotu.hotcms.widget.copyright-copyright:1.0.2-SNAPSHOT");

    }

}