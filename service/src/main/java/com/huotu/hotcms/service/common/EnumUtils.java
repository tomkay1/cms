package com.huotu.hotcms.service.common;

/**
 * 枚举工具类
 *
 * @author CJ
 */
public class EnumUtils {

    /**
     * 按枚举的{@link CommonEnum#getCode() code}获取枚举
     *
     * @param tClass 枚举类
     * @param code   需要获取枚举的code
     * @param <T>    枚举类
     * @return 指定code的枚举实例, 没有找到会返回null
     */
    public static <T extends CommonEnum> T valueOf(Class<T> tClass, Object code) {
        for (T t : tClass.getEnumConstants()) {
            if (t.getCode().equals(code))
                return t;
        }
        return null;
    }

}
