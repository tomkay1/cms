package com.huotu.hotcms.web.common;


/**
 * Created by xhl on 2015/12/23.
 */
public class ConfigInfo {
//    @Value("${web.rootTemplate}")
    private static final String rootTemplate="/template/%s";

    /**
     * 获得本地模版根目录
     * */
    public static String getRootTemplate(Integer customerId){
        return String.format(rootTemplate,customerId);
    }

}
