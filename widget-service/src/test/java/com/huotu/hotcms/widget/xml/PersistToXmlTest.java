/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.xml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.huotu.hotcms.widget.page.Page;
import com.huotu.hotcms.widget.test.TestBase;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * 测试一系列将页面信息保存到xml 和 把xml解析成相应的类的过程
 * <b>但是xml对其他数据类型比如array的支持不如json</b>
 */
public class PersistToXmlTest extends TestBase {


    @Test
    public void testToXml() throws IOException {
        Page page=randomPage();
        ObjectMapper objectMapper=new ObjectMapper();
        String json=objectMapper.writeValueAsString(page);
    }
}
