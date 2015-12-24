package com.huotu.hotcms.util;

import java.util.regex.Pattern;

/**
 * 当前登录的用户相关信息
 * Created by xhl on 2015/12/23.
 */
public class UserInfo {

    public UserInfo()
    {}

    /*
    * 商户ID
    * */
    public int customerId;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /*
    * 是否超级管理员登录
    * */
    public boolean isSuperManage;

    public boolean isSuperManage() {
        return isSuperManage;
    }

    public void setIsSuperManage(boolean isSuperManage) {
        this.isSuperManage = isSuperManage;
    }
}
