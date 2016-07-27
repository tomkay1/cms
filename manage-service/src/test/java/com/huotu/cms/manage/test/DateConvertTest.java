/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.test;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * Created by wenqi on 2016/7/26.
 */
public class DateConvertTest {

    @Test
    public void testConvert() throws ParseException {
        String text="2016-01-02";
        Date date=new SimpleDateFormat("yyyy-mm-dd").parse(text);
        Instant instant = Instant.ofEpochMilli(date.getTime());
        LocalDateTime localDateTime=LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }
}
