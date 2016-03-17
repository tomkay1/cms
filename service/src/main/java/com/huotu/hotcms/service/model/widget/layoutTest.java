package com.huotu.hotcms.service.model.widget;

import lombok.Data;

/**
 * Created by Administrator on 2016/3/16.
 */
@Data
public class layoutTest {
    private  String html;

    public layoutTest(String html){
        this.html=html;
    }
}
