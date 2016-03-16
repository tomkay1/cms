/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.common;

import com.huotu.hotcms.service.model.thymeleaf.foreach.PageableForeachParam;


/**
 * Created by cwb on 2016/1/5.
 */
public enum  ParamEnum {
    ARTICLE {
        public Object getForeachParams(){
            return PageableForeachParam.class;
        }
    },
    LINK {
        public Object getForeachParams(){
            return PageableForeachParam.class;
        }
    },
    SITE{
        public Object getForeachParams(){return PageableForeachParam.class;}
    },
    DOWNLOAD{
        public Object getForeachParams(){return PageableForeachParam.class;}
    },
    GALLERY{
        public Object getForeachParams(){return PageableForeachParam.class;}
    },
    NOTICE{
        public Object getForeachParams(){return PageableForeachParam.class;}
    },
    CATEGORY{
        public Object getForeachParams(){return PageableForeachParam.class;}
    },
    VIDEO{
        public Object getForeachParams() {return PageableForeachParam.class;}
    },
    TIME{
        public Object getForeachParams() {return PageableForeachParam.class;}
    };
    public static final String PARAM_PREFIX = "param";
    public abstract Object getForeachParams();
}
