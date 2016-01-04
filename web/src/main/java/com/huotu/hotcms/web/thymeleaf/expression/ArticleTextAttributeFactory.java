package com.huotu.hotcms.web.thymeleaf.expression;

import org.thymeleaf.model.IProcessableElementTag;

/**
 * <P>
 *     Thymeleaf 自定义方言,文章模型输出参数解析
 * </P>
 * @since 1.0.0
 *
 * @author xhl
 */
public class ArticleTextAttributeFactory implements IDialectAttributeFactory{
    private static ArticleTextAttributeFactory instance=new ArticleTextAttributeFactory();

    /*
    * 单例出口
    * */
    public static ArticleTextAttributeFactory getInstance(){
        if(instance==null)
            instance=new ArticleTextAttributeFactory();
        return  instance;
    }


    @Override
    public String getHtml5Attr(IProcessableElementTag elementTag, String name) {
        return null;
    }

    @Override
    public Object getHtml5Attr(IProcessableElementTag elementTag) {
        return null;
    }
}
