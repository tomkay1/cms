package com.huotu.hotcms.service.service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.huotu.hotcms.service.config.ServiceTestConfig;
import com.huotu.hotcms.service.model.widget.TestModel;
import org.junit.Test;
import org.junit.runner.RunWith;
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

/**
 * Created by Administrator on 2016/3/15.
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ServiceTestConfig.class)
@WebAppConfiguration
public class XmlServiceTest {
    @Test
    public void testXmlJAXB(){
//        mapper.readTree()


        TestModel model = JAXB.unmarshal(getClass().getResourceAsStream("/test.xml"), TestModel.class);
        System.out.println(model);
//        model.setValue("laoxie");
        StringWriter stringWriter = new StringWriter();
        JAXB.marshal(model,stringWriter);
        System.out.println(stringWriter.toString());
    }
    @Test
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

}
