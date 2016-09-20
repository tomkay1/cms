/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.test;

import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author CJ
 */
@Controller
public class EditInTestController {

    @Autowired
    private SiteService siteService;

    @RequestMapping("/testEditIn/{name}")
    public String editIn(@PathVariable String name, long siteId, ContentType fixedType, Model model) {
        model.addAttribute("site", siteService.getSite(siteId));
        model.addAttribute("uri", "/manage/" + name + "/editIn");
        model.addAttribute("fixedType", fixedType);
        return "page/simpleEdit.html";
    }

}
