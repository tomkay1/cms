package com.huotu.hotcms.web.service;

import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.web.common.DialectDataSourcesEnum;
import com.huotu.hotcms.web.common.DialectHtml5AttrEnum;
import com.huotu.hotcms.web.model.ForeachDialectModel;
import com.huotu.hotcms.web.model.Seo;
import com.huotu.hotcms.web.thymeleaf.expression.ForeachDialectAttributeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;

import javax.servlet.http.HttpServletRequest;

/**
 * <P>
 *    Thymeleaf 自定义方言循环数据解析服务
 * </P>
 * @since 1.0.0
 *
 * @author xhl
 *
 */
@Service
public class ForeachDialectService extends BaseDialectService {

    public ForeachDialectService() {
        super();
    }

    /*
    * 根据ForeachDialect对象解析数据
    * */
    @Override
    public Object resolveDataByAttr(HttpServletRequest request,IProcessableElementTag elementTag,AttributeName attributeName){
        ForeachDialectModel model=ForeachDialectAttributeFactory.getInstance().getHtml5Attr(elementTag);//
        String dialectType=attributeName.getPrefix();
        if(model!=null)//
        {
            if(dialectType.equals(DialectDataSourcesEnum.DATA_SOURCES_ARTICLE.getValue().toString()))
            {
                //TODO:测试数据服务
                Seo[] site=new Seo[]{
                        new Seo("文章一"),
                        new Seo("文章二"),
                        new Seo("文章三"),
                        new Seo("文章四"),
                        new Seo("文章五"),
                };
                return site;
            }
            if(dialectType.equals(DialectDataSourcesEnum.DATA_SOURCES_LINK.getValue().toString()))
            {
                //TODO:测试数据服务
                Seo[] site=new Seo[]{
                        new Seo("链接一"),
                        new Seo("链接二"),
                        new Seo("链接三"),
                        new Seo("链接四"),
                        new Seo("链接五"),
                };
                return site;
            }
            //解析数据服务
        }
        return  null;
    }
}
