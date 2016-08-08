/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.service.impl.http;

import org.apache.http.HttpEntity;
import org.apache.http.impl.client.AbstractResponseHandler;
import org.luffy.libs.libseext.XMLUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * 用于解析XML
 *
 * @author CJ
 */
public class DocumentResponseHandler extends AbstractResponseHandler<Document> {
    @Override
    public Document handleEntity(HttpEntity entity) throws IOException {
        try {
            return XMLUtils.xml2doc(entity.getContent());
        } catch (SAXException | ParserConfigurationException e) {
            throw new IOException(e);
        }
    }
}
