package com.huotu.hotcms.admin.annoation;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2016/4/28.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthorizeRole {
    public enum Role{Customer,Supper};
    Role roleType() default Role.Customer;
}
