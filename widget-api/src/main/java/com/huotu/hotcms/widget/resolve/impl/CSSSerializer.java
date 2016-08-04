/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.resolve.impl;

import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.standard.serializer.IStandardCSSSerializer;
import org.thymeleaf.standard.serializer.StandardSerializers;
import org.unbescape.css.CssEscape;

import java.io.IOException;
import java.io.Writer;

/**
 * 鉴于Thymeleaf 无差别的对所有字符串进行 Identifier 处理;其实很多情况下很多字符串仅仅只是一个value,比如颜色值#101010
 * <p>
 * 这个类名应该作为一个配置信息以{@link StandardSerializers#STANDARD_CSS_SERIALIZER_ATTRIBUTE_NAME}放入配置信息中。
 *
 * @author CJ
 * @see org.unbescape.css.CssEscape#escapeCssIdentifier(String)
 * @see org.unbescape.css.CssEscape#escapeCssString(String)
 */
public class CSSSerializer implements IStandardCSSSerializer {
    @Override
    public void serializeValue(Object object, Writer writer) {
        try {
            if (object == null) {
                writeNull(writer);
                return;
            }
            if (object instanceof CharSequence) {
                writeString(writer, object.toString());
                return;
            }
            if (object instanceof Character) {
                writeString(writer, object.toString());
                return;
            }
            if (object instanceof Number) {
                writeNumber(writer, (Number) object);
                return;
            }
            if (object instanceof Boolean) {
                writeBoolean(writer, (Boolean) object);
                return;
            }
            writeString(writer, object.toString());
        } catch (IOException ex) {
            throw new TemplateProcessingException(
                    "An exception was raised while trying to serialize object to CSS", ex);
        }
    }

    private void writeBoolean(Writer writer, boolean bool) throws IOException {
        writer.write("" + bool);
    }

    private void writeNumber(Writer writer, Number number) throws IOException {
        writer.write(number.toString());
    }

    private void writeString(Writer writer, String string) throws IOException {
        writer.write(CssEscape.escapeCssString(string));
    }

    private void writeNull(Writer writer) throws IOException {
        writer.write(""); // There isn't really a 'null' token in CSS
    }
}
