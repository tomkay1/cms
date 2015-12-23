package com.huotu.hotcms.entity.others;

/**
 * Created by Administrator on 2015/12/23.
 */
public class UserInfo {

    public UserInfo()
    {}

    public int customerId;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public boolean isSuperManage;

    public boolean isSuperManage() {
        return isSuperManage;
    }

    public void setIsSuperManage(boolean isSuperManage) {
        this.isSuperManage = isSuperManage;
    }
}
