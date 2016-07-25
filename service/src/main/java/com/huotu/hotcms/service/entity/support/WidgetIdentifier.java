/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.entity.support;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * 控件唯一识别符
 *
 * @author CJ
 */
@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class WidgetIdentifier implements Serializable {

    private static final long serialVersionUID = -5647085640723572029L;
    private String groupId;
    private String artifactId;
    private String version;

    /**
     * @param identify groupId-widgetId:version 也可能是Base64加密以后的{@link #toURIEncoded()}
     * @return
     * @throws IllegalArgumentException identify不符合预定规则
     */
    public static WidgetIdentifier valueOf(String identify) throws IllegalArgumentException {
        try {
            byte[] data = Base64.getUrlDecoder().decode(identify);
            identify = new String(data, "UTF-8");
        } catch (IllegalArgumentException ignored) {
            //无所谓 那么继续
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
        try {
            String[] args = identify.split(":");
            String[] groupIdAndWidgetId = args[0].split("-");
            return new WidgetIdentifier(groupIdAndWidgetId[0], groupIdAndWidgetId[1], args[1]);
        } catch (Exception e) {
            throw new IllegalArgumentException("参数不符合预定规则");
        }
    }

    @Override
    public String toString() {
        return groupId + "-" + artifactId + ":" + version;
    }

    /**
     * @return <pre>Base64.getUrlEncoder().encodeToString(data)</pre>
     * @throws UnsupportedEncodingException
     */
    public String toURIEncoded() throws UnsupportedEncodingException {
        byte[] data = toString().getBytes("UTF-8");
        return Base64.getUrlEncoder().encodeToString(data);
//        return URLEncoder.encode(toString(),"UTF-8");
    }
}
