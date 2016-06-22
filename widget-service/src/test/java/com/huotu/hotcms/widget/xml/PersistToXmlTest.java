package com.huotu.hotcms.widget.xml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.huotu.hotcms.widget.page.Page;
import com.huotu.hotcms.widget.test.TestBase;
import org.junit.Test;

import javax.xml.bind.JAXB;
import java.io.StringWriter;

/**
 * Created by hzbc on 2016/6/22.
 */


/**
 * 测试一系列将页面信息保存到xml 和 把xml解析成相应的类的过程
 */
public class PersistToXmlTest extends TestBase {


    @Test
    public void testToXml() throws JsonProcessingException {
        Page page=randomPage();
        XmlMapper xmlMapper=new XmlMapper();
        String xmlString=xmlMapper.writeValueAsString(page);
        System.out.println("xmlString:"+xmlString);
    }

    @Test
    public void testPrint(){
        System.out.println(11);
    }

}
