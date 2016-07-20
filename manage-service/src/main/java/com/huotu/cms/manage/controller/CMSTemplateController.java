/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller;

import com.huotu.hotcms.service.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * <p>CMS系统模板相关</p>
 * <p>针对界面:/view/site/site.html 上的点赞，使用，预览功能</p>
 *
 * @see com.huotu.hotcms.service.entity.Template
 * @see TemplateController
 * @author wenqi
 */
@Controller
@RequestMapping("/manage/template")
public class CMSTemplateController {

    @Autowired
    private TemplateService templateService;


    /**
     * 点赞功能
     *
     * @param siteId     一个模板其实是个站点，此处对应模板的ID
     * @param name 当前登录用户名
     * @param behavior 用户行为。1表示点赞，0表示取消赞
     */
    @RequestMapping(value = "/laud/{siteId}", method = RequestMethod.PUT, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public boolean laud(@PathVariable("siteId") long siteId, @RequestParam("name") String name
            ,@RequestParam("behavior") int behavior) {
        return templateService.laud(siteId, name,behavior );
    }

    @RequestMapping(value = "/isLauded",method = RequestMethod.GET,produces = "application/json; charset=UTF-8")
    @ResponseBody
    public boolean isLauded(@RequestParam("name")String name,@RequestParam("templateSiteId")long templateSiteId){
        return templateService.isLauded(templateSiteId,name);
    }

    /**
     * 站点使用
     *
     * @param templateSiteID 模板站点ID
     * @param customerSiteId 商户站点ID
     * @param mode           使用模式：
     *                       <ul>
     *                       <li>0为追加模式</li>
     *                       <li>1为替换模式</li>
     *                       </ul>
     */
    @RequestMapping(value = "/use/{templateSiteID}/{customerSiteId}",method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void use(@PathVariable("templateSiteID") long templateSiteID
            , @PathVariable("customerSiteId") long customerSiteId, @RequestParam("mode") int mode) throws IOException {
        templateService.use(templateSiteID, customerSiteId, mode);
    }

}
