/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.page;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.HashMap;

/**
 * 样式
 *
 * @author CJ
 */
public class StyleSheet extends HashMap<String, String> implements Serializable {

    private static final long serialVersionUID = 6796996657086009834L;

    /**
     * 将样式描述到一个html里去
     *
     * @param writer dist
     * @throws IOException
     */
    public void printHtml(Writer writer) throws IOException {
        writer.append(" style=\"");
        for (String name : keySet()) {
            writer.append(name);
            writer.append(":").append(get(name));
            writer.append(";");
        }
        writer.append("\"");
    }
}
