/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller.decoration;

import com.huotu.hotcms.service.repository.SiteRepository;
import com.huotu.hotcms.service.repository.TemplateRepository;
import com.huotu.hotcms.service.service.HostService;
import com.huotu.hotcms.service.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by hzbc on 2016/5/26.
 */
@Controller
@RequestMapping("/manage/template")
public class TemplatesController {
    @Autowired
    private SiteService siteService;
    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private HostService hostService;

//    @Autowired
//    private CustomPagesService customPagesService;
//
//    @RequestMapping("/use")
//    @ResponseBody
//    public ResultView useTemplate(long templateId,long siteId) {
//        Site customerSite=siteRepository.findOne(siteId);
//        try {
//            siteService.siteCopy(templateId,customerSite);
//            return new ResultView(ResultOptionEnum.OK.getCode(),ResultOptionEnum.OK.getValue(),null);
//        } catch (Exception e) {
//            return new ResultView(ResultOptionEnum.FAILE.getCode(),ResultOptionEnum.FAILE.getValue(),null);
//        }
//
//    }
//
//    @RequestMapping("/view")
//    @ResponseBody
//    public ResultView viewTemplate(long templateId){
//        Site templateSite=templateRepository.findOne(templateId).getSite();
//        String domain=hostService.getHomeDomain(templateSite);
//        String url="http://"+domain;
//        CustomPages customPages=customPagesService.findHomePages(templateSite);
//        Long pageId=-1L;
//        if(customPages!=null){
//            pageId=customPages.getId();
//        }
//        return new ResultView(ResultOptionEnum.OK.getCode(),ResultOptionEnum.OK.getValue(),url+","+pageId);
//    }


}
