package com.huotu.hotcms.widget.page;

import com.huotu.hotcms.widget.Component;

/**
 * Created by hzbc on 2016/6/23.
 */
public class PageElementDeserializer extends CMSDeserializer<PageElement> {
    @Override
    public Class<?> getImplementationClass() {
        return Component.class;
    }
}
