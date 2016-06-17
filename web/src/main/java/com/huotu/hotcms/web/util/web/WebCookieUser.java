/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.web.util.web;

import com.huotu.hotcms.service.common.CMSEnums;
import com.huotu.hotcms.service.util.CookieHelper;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chendeyu on 2016/4/6.
 */
@Component
public class WebCookieUser {

    /**
     * * Cookie中读取UserId
     *
     * */
    public  String getUserId(HttpServletRequest request){
        return  CookieHelper.getCookieVal(request, CMSEnums.MallCookieKeyValue.UserId.toString());
    }

    /**
     * 检查登录
     * */
    public  boolean checkLogin(HttpServletRequest request)
    {

        String useId = getUserId(request);
        boolean loginIndex=false;
        if(useId!=null&&!"".equals(useId))
        {
          loginIndex=true;
        }
        return  loginIndex;
    }

}
