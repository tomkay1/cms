package com.huotu.hotcms.service.model.widget;


import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import java.util.List;


/**
 * <p>
 *   页面模型
 * </p>
 * @since 1.2
 *
 * @author xhl
 *
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class WidgetPage {

    /**
     * 页面标题名称
     * */
    @XmlAttribute(name = "pageName")
    private String pageName;

    /**
     * 页面关键词
     * */
    @XmlAttribute(name="pageKeyWords")
    private String pageKeyWords;

    @XmlAttribute(name = "页面描述信息")
    private String pageDescription;

    /**
    * 页面请求地址,不包含参数信息
    * **/
    @XmlAttribute(name = "url")
    private String url;

    /**
     * 页面背景颜色
     * */
    @XmlAttribute(name="pageBackGround")
    private String pageBackGround;

    /**
     * 页面背景图片
     * */
    @XmlAttribute(name = "pageBackImage")
    private String pageBackImage;

    /**
     * 页面背景图片平铺方式
     * */
    @XmlAttribute(name = "pageBackRepeat")
    private String pageBackRepeat;


    /**
     * 页面背景图片左右对齐方式
     * */
    @XmlAttribute(name="pageBackAlign")
    private String pageBackAlign;

    /**
     * 页面背景图片垂直对齐方式
     * */
    @XmlAttribute(name = "pageBackVertical")
    private String pageBackVertical;

    /**
     * 是否取用店铺头部
     * */
    @XmlAttribute(name="pageEnabledHead")
    private Boolean pageEnabledHead;

    /**
    * 页面中的布局列表
    * **/
    private List<WidgetLayout> layout;
}
