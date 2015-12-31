package com.huotu.hotcms.web.thymeleaf.dialect;

import com.huotu.hotcms.web.common.DialectAttrNameEnum;
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

    private static List<BaseDialect> dialectList = new ArrayList<>();


    public static List<BaseDialect> getDialectList() {
        initDialect();
        return dialectList;
    }

    /*
    * 初始化thymeleaf 扩展的标签
    *
    * */
    public static void initDialect(){
        dialectList.add(new BaseDialect(NAME,PREFIX, DialectAttrNameEnum.FOREACH.getValue().toString(),new ForeachDialectService()));//foreach标签
    }
}
