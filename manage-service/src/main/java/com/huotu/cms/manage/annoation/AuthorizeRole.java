package com.huotu.cms.manage.annoation;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2016/4/28.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthorizeRole {
    /**
     * 角色权限
     */
    public enum Role {
        /**
         * 商户基本权限
         */
        Customer,
        /**
         * 超级管理员权限
         */
        Supper
    };

    Role roleType() default Role.Customer;
}
