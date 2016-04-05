package com.huotu.hotcms.service.widget;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.huotu.hotcms.service.model.widget.TestModel;
import com.huotu.hotcms.service.model.widget.WidgetBase;
import com.huotu.hotcms.service.model.widget.WidgetPage;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXB;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/15.
 */
@Service
public class XmlTestService {
    public void testXmlJAXB(){
//        mapper.readTree()

//        TestModel model = JAXB.unmarshal(getClass().getResourceAsStream("/test.xml"), TestModel.class);
        WidgetPage model = JAXB.unmarshal(getClass().getResourceAsStream("/text.xml"), WidgetPage.class);
        System.out.println(model);
//        model.setValue("laoxie");
        StringWriter stringWriter = new StringWriter();
        JAXB.marshal(model,stringWriter);
        System.out.println(stringWriter.toString());
    }
    public void testJacksonXML() throws XMLStreamException, IOException {
        XmlMapper mapper = new XmlMapper();


        XMLStreamReader reader = XMLInputFactory.newFactory().createXMLStreamReader(getClass().getResourceAsStream("/text.xml"));
        Map map=new HashMap<>();
//        map.put("name","测试哦");
//        map.put("Rows",);
//        WidgetBase widgetBase=new WidgetBase();
//        widgetBase
//        TestModel model1 =mapper.readValue(reader,TestModel.class);
        WidgetPage model =mapper.readValue(reader,WidgetPage.class);

        System.out.println(model);
//        model.setValue("laoxie");
        StringWriter stringWriter = new StringWriter();
        mapper.writeValue(stringWriter,model);
//        mapper.
        System.out.println(stringWriter.toString());
    }

    public void test(Map map) throws XMLStreamException, IOException{
        XmlMapper mapper = new XmlMapper();
//        XMLStreamReader reader = XMLInputFactory.newFactory().createXMLStreamReader(getClass().getResourceAsStream("/text.xml"));
//        WidgetPage model =mapper.readValue(reader,WidgetPage.class);
//
//        System.out.println(model);
        StringWriter stringWriter = new StringWriter();
        mapper.writeValue(stringWriter,map);
        System.out.println(stringWriter.toString());
    }
}
