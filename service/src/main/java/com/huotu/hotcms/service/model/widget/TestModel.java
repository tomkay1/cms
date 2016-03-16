package com.huotu.hotcms.service.model.widget;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * Created by Administrator on 2016/3/15.
 */
@Data
public class TestModel {
    @JacksonXmlProperty
    private String url;

    private String[] layout;

}
