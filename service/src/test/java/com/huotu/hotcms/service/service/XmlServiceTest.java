/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.huotu.hotcms.service.config.ServiceTestConfig;
import com.huotu.hotcms.service.model.widget.TestModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.xml.bind.JAXB;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2016/3/15.
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ServiceTestConfig.class)
@WebAppConfiguration
public class XmlServiceTest {

    //    @Test
    public void testXmlJAXB(){
//        mapper.readTree()


        TestModel model = JAXB.unmarshal(getClass().getResourceAsStream("/test.xml"), TestModel.class);
        System.out.println(model);
//        model.setValue("laoxie");
        StringWriter stringWriter = new StringWriter();
        JAXB.marshal(model,stringWriter);
        System.out.println(stringWriter.toString());
    }

    //    @Test
    public void testJacksonXML() throws XMLStreamException, IOException {
        XmlMapper mapper = new XmlMapper();

        XMLStreamReader reader = XMLInputFactory.newFactory().createXMLStreamReader(getClass().getResourceAsStream("/text.xml"));
        TestModel model =mapper.readValue(reader,TestModel.class);
        System.out.println(model);
//        model.setValue("laoxie");
        StringWriter stringWriter = new StringWriter();
        mapper.writeValue(stringWriter,model);
//        mapper.
        System.out.println(stringWriter.toString());
    }

    @Test
    public void testLocal() {
        PrettyTime p = new PrettyTime(new Locale("ZH_CN"));
        System.out.println(p.format(new Date()));
    }

    @Test
    public void testMinutesFromNow() throws Exception {
        PrettyTime t = new PrettyTime(new Date(0));
        System.out.println(t.format(new Date(1000 * 60 * 12)));
    }

    @Test
    public void testMomentsAgo() throws Exception {
        PrettyTime t = new PrettyTime(new Date(6000));
        System.out.println(t.format(new Date(0)));
    }

    @Test
    public void testMinutesAgo() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
        Date date = format.parse("2012-07-18 23:42:05");
        Date now = new Date();
        PrettyTime t = new PrettyTime(now);
        System.out.println(t.format(date));
    }

}
