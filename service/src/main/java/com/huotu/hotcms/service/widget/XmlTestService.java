package com.huotu.hotcms.service.widget;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.huotu.hotcms.service.model.widget.*;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public void test1(Map<String,Object> map){
        StringWriter stringWriter = new StringWriter();
        JAXB.marshal(map,stringWriter);
        System.out.println(stringWriter.toString());
    }

    public void test3(List<WidgetProperty> properties){
        StringWriter stringWriter = new StringWriter();
        JAXB.marshal(properties,stringWriter);
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

    public  void test2(WidgetPage widgetPage) throws IOException {
        XmlMapper mapper = new XmlMapper();
//        XMLStreamReader reader = XMLInputFactory.newFactory().createXMLStreamReader(getClass().getResourceAsStream("/text.xml"));
//        WidgetPage model =mapper.readValue(reader,WidgetPage.class);
//
//        System.out.println(model);
        StringWriter stringWriter = new StringWriter();
        mapper.writeValue(stringWriter,widgetPage);
        System.out.println(stringWriter.toString());
    }

    public void testWidgetBase(WidgetBase widgetBase){
        StringWriter stringWriter = new StringWriter();
        JAXB.marshal(widgetBase, stringWriter);
        System.out.println(stringWriter.toString());
    }

//    public void test5(WidgetListProperty<WidgetProperty> properties){
//        WidgetProperty person = new WidgetProperty();
//        person.setName("ceshi ");
//        person.setValue("sss");
//
//        List<WidgetProperty> personList = new ArrayList<WidgetProperty>();
//        personList.add(person);
//
//        ListBean<WidgetProperty> listBean = new ListBean<WidgetProperty>();
//        listBean.setList(personList);
//
//        try {
//            JAXBContext context = JAXBContext.newInstance(WidgetListProperty.class, WidgetProperty.class);
//            Marshaller m = context.createMarshaller();
//            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            m.marshal(properties,outputStream);
//            System.out.println(outputStream);
//        } catch (JAXBException e) {
//            e.printStackTrace();
//        }
//    }
}

@XmlRootElement(name = "ListBean")
class  ListBean<T>{
    private String name;
    private List<T> list;

    @XmlElement(name = "Domain")
    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}