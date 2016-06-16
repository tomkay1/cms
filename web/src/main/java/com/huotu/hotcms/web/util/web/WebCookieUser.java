package com.huotu.hotcms.web.util.web;

import com.huotu.hotcms.service.common.CMSEnums;
import com.huotu.hotcms.service.util.CookieHelper;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by chendeyu on 2016/4/6.
 */
@Component
public class WebCookieUser {

    private static String UNIONID="_UNIONID";

    private static String USERKEY="_CMSUSERKEY";//DES({unionid}|{ip})


    /**
     * * Cookie中读取UserId
     *
     * */
    public  String getUserId(HttpServletRequest request){
        return  CookieHelper.getCookieVal(request, CMSEnums.MallCookieKeyValue.UserId.toString());
    }

    /**
     * 把UserId放到Cookie中
     * */
    public void setUserID(HttpServletResponse response, String UnionId)
    {
        CookieHelper.setCookie(response,CMSEnums.MallCookieKeyValue.UserId.toString(),UnionId);
    }

    /**
     * 把商户ID放到Cookie中
     * */
    public void setCustomerId(HttpServletResponse response,int customerId)
    {
        CookieHelper.setCookie(response,CMSEnums.CookieKeyValue.CustomerID.toString(),String.valueOf(customerId));
    }

//    /**
//     * 把userID放到Cookie中
//     * */
//    public void userID(HttpServletResponse response, String UnionId)
//    {
//        CookieHelper.setCookie(response,CMSEnums.MallCookieKeyValue.UserId.toString(),UnionId);
//    }



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
