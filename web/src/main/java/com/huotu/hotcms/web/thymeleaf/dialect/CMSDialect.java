package com.huotu.hotcms.web.thymeleaf.dialect;

import com.huotu.hotcms.web.service.BaseDialectService;
import com.huotu.hotcms.web.service.ForeachDialectService;
import org.thymeleaf.dialect.IDialect;

import javax.lang.model.element.Name;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *     CMS扩展的Thymeleaf 标签
 * </p>
 * @since 1.0.0
 *
 * @author xhl
 */
public class CMSDialect {

    public static final String NAME = "huotu";
    public static final String PREFIX = "hot";//前缀
    public static final int PROCESSOR_PRECEDENCE = 800;
//    public static final String ATTR_NAME = "foreach";//属性


    public ArrayList<BaseDialect> dialectList=null;

    public CMSDialect(){
        dialectList=new ArrayList<>();
    }

    /*
    * 初始化thymeleaf 扩展的标签
    * */
    public void initDialect(){
        dialectList.add(new BaseDialect(NAME,PREFIX,"foreach",new ForeachDialectService()));//foreach标签
    }


//    private void addDialect(String name,String prefix,String attrName,BaseDialectService dialectService){
//
//    }
}