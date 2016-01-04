/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.web.service;

import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by cwb on 2016/1/4.
 */
public class BaseProcessorService {
    public Object  resolveDataByAttr(IProcessableElementTag elementTag,HttpServletRequest request){
        return null;
    }

    public String resolveDataByAttr(HttpServletRequest request,IProcessableElementTag elementTag,AttributeName attributeName,String attributeValue)
    {
        return  null;
    }
}
