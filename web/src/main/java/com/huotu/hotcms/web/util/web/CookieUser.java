package com.huotu.hotcms.web.util.web;

import com.huotu.hotcms.service.common.CMSEnums;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by chendeyu on 2016/4/6.
 */
@Component
public class CookieUser {

    private static String UNIONID="_UNIONID";

    private static String USERKEY="_CMSUSERKEY";//DES({unionid}|{ip})


    /**
     * * Cookie中读取UnionID
     *
     * */
    public  String getUnionId(HttpServletRequest request){
        return  CookieHelper.getCookieVal(request, CMSEnums.MallCookieKeyValue.UnionID.toString());
    }

    /**
     * 把UnionID放到Cookie中
     * */
    public void setUnionID(HttpServletResponse response, String UnionId)
    {
        CookieHelper.setCookie(response,CMSEnums.MallCookieKeyValue.UnionID.toString(),UnionId);
    }


    /**
     * 检查登录
     * */
    public  boolean checkLogin(HttpServletRequest request,HttpServletResponse response)
    {

        String unionId = getUnionId(request);
        boolean loginIndex=false;
        if(unionId!=null&&!"".equals(unionId))
        {
          loginIndex=true;
        }
        return  loginIndex;
    }

}
