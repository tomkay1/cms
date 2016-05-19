package com.huotu.hotcms.service.util;

/**
 * Created by hzbc on 2016/5/18.
 */

import com.huotu.hotcms.service.entity.Site;

/**
 * Serial 生成类
 */
public class SerialUtil {

    /**
     *
     * @param serial
     * @return
     */
    public static String formartSerial(String serial){
        return "site_"+serial+"_serial";
    }

    /**
     * 根绝站点生成 相应的serial
     * @param site 相应的站点
     * @return 生成的serial
     */
    public static String formartSerial(Site site){
        return "site_"+site.getSiteId()+"_serial";
    }
}
