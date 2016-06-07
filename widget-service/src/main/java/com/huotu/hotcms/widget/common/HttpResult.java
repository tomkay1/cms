/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.http.HttpEntity;

/**
 * Created by elvis on 2016-6-7.
 */
@Data
@AllArgsConstructor
public class HttpResult {
    private int httpStatus;
    private HttpEntity httpEntity;
}
