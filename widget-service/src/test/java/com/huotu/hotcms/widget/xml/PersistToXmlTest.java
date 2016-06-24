package com.huotu.hotcms.widget.xml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.huotu.hotcms.widget.page.Page;
import com.huotu.hotcms.widget.test.TestBase;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;


/**
 * Created by hzbc on 2016/6/22.
 */


/**
 * 测试一系列将页面信息保存到xml 和 把xml解析成相应的类的过程
 */
public class PersistToXmlTest extends TestBase {


    @Test
    public void testToXml() throws IOException {
        Page page=randomPage();
        XmlMapper xmlMapper=new XmlMapper();
        String xmlString1=xmlMapper.writeValueAsString(page);
        System.out.println("xmlString:"+xmlString1);

        Page getPage=xmlMapper.readValue(xmlString1,Page.class);

        String xmlString2=xmlMapper.writeValueAsString(getPage);
        System.out.println("xmlString:"+xmlString2);

        Assert.assertEquals(xmlString1,xmlString2);


    }
}
