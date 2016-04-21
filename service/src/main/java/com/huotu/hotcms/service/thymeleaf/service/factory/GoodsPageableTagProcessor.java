package com.huotu.hotcms.service.thymeleaf.service.factory;

import com.huotu.hotcms.service.thymeleaf.expression.DialectAttributeFactory;
import com.huotu.hotcms.service.widget.model.GoodsSearcher;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;
import org.thymeleaf.model.IProcessableElementTag;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by hzbc on 2016/4/21.
 */

@Component
public class GoodsPageableTagProcessor {

    private static Log log = LogFactory.getLog(GoodsPageableTagProcessor.class);

    @Autowired
    private DialectAttributeFactory dialectAttributeFactory;

    public Object process(IProcessableElementTag elementTag,HttpServletRequest request){
        GoodsSearcher goodsSearcher=null;
        try{
            goodsSearcher=dialectAttributeFactory.megerObject(elementTag, request, GoodsSearcher.class);
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        return goodsSearcher;
    }
}

