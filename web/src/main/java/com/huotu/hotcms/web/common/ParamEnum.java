/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.web.common;

import com.huotu.hotcms.service.model.thymeleaf.foreach.ArticleForeachParam;
import com.huotu.hotcms.service.model.thymeleaf.foreach.VideoForeachParam;


/**
 * Created by cwb on 2016/1/5.
 */
public enum  ParamEnum {
    ARTICLE {
        public Object getForeachParams(){
            return ArticleForeachParam.class;
        }
    },
    LINK {
        public Object getForeachParams(){
            return ArticleForeachParam.class;
        }
    },
    SITE{
        public Object getForeachParams(){return ArticleForeachParam.class;}
    },
    DOWNLOAD{
        public Object getForeachParams(){return ArticleForeachParam.class;}
    },
    GALLERY{
        public Object getForeachParams(){return ArticleForeachParam.class;}
    },
    NOTICE{
        public Object getForeachParams(){return ArticleForeachParam.class;}
    },
    CATEGORY{
        public Object getForeachParams(){return ArticleForeachParam.class;}
    },
    VIDEO{
        public Object getForeachParams() {return VideoForeachParam.class;}
    };
    public static final String PARAM_PREFIX = "param";
    public abstract Object getForeachParams();
}
