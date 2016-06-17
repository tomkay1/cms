/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.admin.controller;

import com.huotu.hotcms.admin.config.TestAdminConfig;
import com.huotu.hotcms.service.entity.Region;
import com.huotu.hotcms.service.repository.RegionRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by cwb on 2016/1/20.
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestAdminConfig.class)
@WebAppConfiguration
@Transactional
public class SiteControllerTest extends WebTestBase {

    @Autowired
    private RegionRepository regionRepository;

    @Test
    public void testShowSiteList() throws Exception {
        mockMvc.perform(get("/site/siteList").cookie(cookie)
                .param("ownerId", "4539")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("/view/web/siteList.html"));
    }

    @Test
    public void testAddSitePage() throws Exception {
        MvcResult result =  mockMvc.perform(get("/site/addSite").cookie(cookie)
                .param("ownerId", "4539")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("/view/web/addSite.html"))
                .andExpect(model().attributeExists("regions"))
                .andReturn();
        List<Region> expectRegions =  regionRepository.findAll();
        List<Region> actualRegions = (List<Region>)result.getModelAndView().getModel().get("regions");
        Assert.assertArrayEquals(expectRegions.toArray(),actualRegions.toArray());

    }

}
