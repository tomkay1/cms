package com.huotu.hotcms.service.util;

/**
 * 当前登录的用户相关信息
 * @since 1.0.0
 * @author xhl
 * @time 2015/15/25
 */
public class UserInfo {

    public UserInfo()
    {}

    /*
    * 商户ID
    * */
    public int customerId;

    public int ownerId;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    /*
            * 是否超级管理员登录
            * */
    public boolean isSuperManage;

    public void setIsSuperManage(boolean isSuperManage) {
        this.isSuperManage = isSuperManage;
    }
}
