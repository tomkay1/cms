package com.huotu.hotcms.service.util;

/**
 * Created by hzbc on 2016/6/16.
 */
public class StaticResource {

    public static boolean isStaticResc(String path){
        String staticRescSuffix[]={".js",".css",".png",".jpg",".jpeg",".gif"};
        for(String s:staticRescSuffix){
            if(path.endsWith(s)){
                return true;
            }
        }
        return false;
    }
}
