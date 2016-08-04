/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller;

import com.huotu.hotcms.service.entity.login.Login;
import com.huotu.hotcms.service.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

/**
 * <p>CMS系统模板相关</p>
 * <p>针对界面:/view/site/site.html 上的点赞，使用，预览功能</p>
 *
 * @author wenqi
 * @see com.huotu.hotcms.service.entity.Template
 * @see TemplateController
 */
@Controller
@RequestMapping("/manage/template")
public class CMSTemplateController {

    @Autowired
    private TemplateService templateService;


    /**
     * 点赞功能
     *
     * @param templateId 一个模板其实是个站点，此处对应模板的ID
     * @param login      当前身份
     * @param behavior   用户行为。1表示点赞，0表示取消赞
     */
    @RequestMapping(value = "/laud/{templateId}", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public boolean laud(@PathVariable("templateId") long templateId, @AuthenticationPrincipal Login login
            , @RequestBody int behavior) {
        return templateService.laud(templateId, login.currentOwnerId(), behavior);
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
    @RequestMapping(value = "/use/{templateSiteID}/{customerSiteId}/{mode}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void use(@PathVariable("templateSiteID") long templateSiteID
            , @PathVariable("customerSiteId") long customerSiteId, @PathVariable("mode") int mode) throws IOException {
        templateService.use(templateSiteID, customerSiteId, mode);
    }

}
