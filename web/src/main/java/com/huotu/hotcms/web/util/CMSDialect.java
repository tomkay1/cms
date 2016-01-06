package com.huotu.hotcms.web.util;

import com.huotu.hotcms.web.thymeleaf.dialect.ArticleDialect;
import com.huotu.hotcms.web.thymeleaf.dialect.CategoryDialect;
import com.huotu.hotcms.web.thymeleaf.dialect.LinkDialect;
import com.huotu.hotcms.web.thymeleaf.dialect.SiteDialect;
import org.thymeleaf.dialect.AbstractProcessorDialect;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *     CMS扩展的Thymeleaf 标签
 * </p>
 * @since 1.0.0
 *
 * @author xhl
 */
public class CMSDialect {

    private static List<AbstractProcessorDialect> dialectList = new ArrayList<>();


    public static List<AbstractProcessorDialect> getDialectList() {
        initDialect();
        return dialectList;
    }

    /*
    * 初始化thymeleaf 扩展的标签
    * */
    public static void initDialect(){
        dialectList.add(new ArticleDialect());
        dialectList.add(new LinkDialect());
        dialectList.add(new SiteDialect());
        dialectList.add(new CategoryDialect());
    }
}
