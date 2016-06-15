/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

/**
 * Created by CJ on 6/14/16.
 */


var widgetInstances = [];

widgetInstances[0] = {
    id: 1,
    properties: {
        url1: "",
        url2: ""
    }
};

function widgetProperties() {
    return widgetInstances[0].properties;
}

///////////////


////控件 需要改变url1
// widgetProperties(..)["url1"]="";