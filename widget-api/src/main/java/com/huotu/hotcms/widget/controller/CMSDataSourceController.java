/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * 查询数据源接口
 * Created by lhx on 2016/7/26.
 */
@Controller("/dataSource")
public class CMSDataSourceController {

    /**
     * 根据parent 的contentType 来决定查询的数据类型 {@link com.huotu.hotcms.widget.service.CMSDataSourceService}
     * @param parentId 数据源id
     * @return json 返回当前parentId 的所有子级元素
     *          例如{code=200,message="Success",data=[...]},{code=403,message="fail",data=[]}
     */
    @RequestMapping(value = "/findGallery/{parentId}")
    @ResponseBody
    public void findAllChildren(Long parentId, Model model, HttpServletResponse response){

    }
}
