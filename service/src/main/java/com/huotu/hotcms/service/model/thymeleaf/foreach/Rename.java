package com.huotu.hotcms.service.model.thymeleaf.foreach;

import java.lang.annotation.*;

/**
 * <p>
 *     定义给实体类属性重命名注解,用于兼容html中的标签使用
 * </p>
 *
 * @since 1.2
 *
 * @author xhl
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Rename {
    String value() default "";
}
