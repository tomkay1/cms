package com.huotu.hotcms.service.model.widget;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * Created by Administrator on 2016/4/6.
 */
//@XmlRootElement(name = "WidgetListProperty")
public class WidgetListProperty{
    private String name;
    private List<WidgetProperty> list;

    @XmlElement(name = "property")
    public List<WidgetProperty> getList() {
        return list;
    }

    public void setList(List<WidgetProperty> list) {
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
