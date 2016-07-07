/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.test;

import com.huotu.hotcms.service.common.SiteType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.service.exception.NoHostFoundException;
import com.huotu.hotcms.service.exception.NoSiteFoundException;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.repository.OwnerRepository;
import com.huotu.hotcms.service.service.SiteService;
import com.huotu.hotcms.service.thymeleaf.service.SiteResolveService;
import com.huotu.hotcms.service.util.StringUtil;
import com.huotu.hotcms.widget.Component;
import com.huotu.hotcms.widget.ComponentProperties;
import com.huotu.hotcms.widget.InstalledWidget;
import com.huotu.hotcms.widget.config.TestConfig;
import com.huotu.hotcms.widget.controller.TestWidget;
import com.huotu.hotcms.widget.page.Empty;
import com.huotu.hotcms.widget.page.Layout;
import com.huotu.hotcms.widget.page.Page;
import com.huotu.hotcms.widget.page.PageElement;
import com.huotu.hotcms.widget.servlet.CMSFilter;
import me.jiangcai.lib.test.SpringWebTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * CMS单元测试基类
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@WebAppConfiguration
public class TestBase extends SpringWebTest{

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SiteResolveService siteResolveService;

    @Autowired
    private SiteService siteService;

    @Override
    public void createMockMVC() {
        MockitoAnnotations.initMocks(this);
        // ignore it, so it works in no-web fine.
        if (context == null)
            return;
        DefaultMockMvcBuilder builder = webAppContextSetup(context);
        builder.addFilters(new CMSFilter(context.getServletContext()));
        if (springSecurityFilter != null) {
            builder = builder.addFilters(springSecurityFilter);
        }

        if (mockMvcConfigurer != null) {
            builder = builder.apply(mockMvcConfigurer);
        }
        mockMvc = builder.build();
    }

    /**
     * 生成随机的测试{@link com.huotu.hotcms.widget.page.Page}数据
     *
     * @return
     */
    protected Page randomPage() {
//        String layout[]={"12","4,4,4","6,6","8,4","2,6,4"};//布局，可以再任意添加，保证数值相加等于12
//        String randomLayout=layout[random.nextInt(layout.length)];
//
//        if(randomLayout.equals("12")){
//
//        }else{
//            String randomLayoutArrays[]=randomLayout.split(",");
//        }
        Page page = new Page();
        page.setPageIdentity(random.nextLong());
        page.setTitle(UUID.randomUUID().toString());

        List<PageElement> pageElementList = new ArrayList<>();
        //PageElement 要么是Layout，要么是Component；二选一
        int randomNum=random.nextInt(100)+1;
        boolean isLayout=false;
        if(randomNum%2==0)
            isLayout=true;

        int nums = random.nextInt(4)+1;//生成PageElement的随机个数
        //在实际环境中，肯定先存在layout,在layout中，拖入component
//        pageElementList.add(randomLayout());
        while (nums-- > 0) {
            if(isLayout) {
                pageElementList.add(randomLayout());
                pageElementList.add(randomEmpty());
            }
            else {
                pageElementList.add(randomComponent());
                pageElementList.add(randomEmpty());
            }
        }
        page.setElements(pageElementList.toArray(new PageElement[pageElementList.size()]));

        return page;
    }

    private Empty randomEmpty(){
        return new Empty();
    }

    private Component randomComponent() {
        Component component=new Component();
        component.setPreviewHTML(UUID.randomUUID().toString());
        component.setStyleId(UUID.randomUUID().toString());
        component.setWidgetIdentity(UUID.randomUUID().toString());
        ComponentProperties componentProperties =new ComponentProperties();
        componentProperties.put(StringUtil.createRandomStr(random.nextInt(3)+1),UUID.randomUUID().toString());
        component.setProperties(componentProperties);
        InstalledWidget installedWidget=new InstalledWidget(new TestWidget());
        installedWidget.setInstallWidgetId(UUID.randomUUID().toString());
        installedWidget.setType(UUID.randomUUID().toString());
        component.setInstalledWidget(installedWidget);
        return component;
    }

    private Layout randomLayout() {
        Layout layout = new Layout();
        layout.setValue(UUID.randomUUID().toString());

        List<PageElement> pageElementList = new ArrayList<>();


        //PageElement 要么是Layout，要么是Component；二选一
        int randomNum=random.nextInt(10);
        boolean isLayout=false;
        if(randomNum%2==0)
            isLayout=true;

        int nums = random.nextInt(2);//生成PageElement的随机个数
        while (nums-- > 0) {
            if(isLayout)
                pageElementList.add(randomLayout());
            else
                pageElementList.add(randomComponent());
        }
        layout.setElements(pageElementList.toArray(new PageElement[pageElementList.size()]));
        return layout;
    }

    /**
     * @return 新建的随机Owner
     */
    protected Owner randomOwner() {
        Owner owner = new Owner();
        owner.setEnabled(true);
        owner.setCustomerId(Math.abs(random.nextInt()));
        return ownerRepository.saveAndFlush(owner);
    }

    /**
     * 建立一个随机的站点
     *
     * @param owner 所属
     * @return 站点
     */
    protected Site randomSite(Owner owner) {
        Site site = new Site();
        site.setOwner(owner);
        site.setName(UUID.randomUUID().toString());
        site.setSiteType(SiteType.SITE_PC_WEBSITE);
        site.setTitle(UUID.randomUUID().toString());
        site.setCreateTime(LocalDateTime.now());
        site.setEnabled(true);
        site.setDescription(UUID.randomUUID().toString());
        String[] domains = new String[]{"localhost"};//randomDomains();
        site = siteService.newSite(domains, domains[0], site, Locale.CHINA);
        try {
            site = siteResolveService.getCurrentSite(request);
        } catch (NoSiteFoundException e) {
            e.printStackTrace();
        } catch (NoHostFoundException e) {
            e.printStackTrace();
        }
        return site;
    }

    protected String[] randomDomains() {
        int size = 4 + random.nextInt(4);
        String[] domains = (String[]) Array.newInstance(String.class, size);
        for (int i = 0; i < domains.length; i++) {
            domains[i] = randomDomain();
        }
        return domains;
    }

    protected String randomDomain() {
        return RandomStringUtils.randomAlphabetic(random.nextInt(5) + 3)
                + "."
                + RandomStringUtils.randomAlphabetic(random.nextInt(5) + 3)
                + "."
                + RandomStringUtils.randomAlphabetic(random.nextInt(2) + 2);
    }

    protected Category randomCategory() {
        Category category = new Category();
        category.setParent(null);
        category.setSite(randomSite(randomOwner()));
        return categoryRepository.saveAndFlush(category);
    }
}
