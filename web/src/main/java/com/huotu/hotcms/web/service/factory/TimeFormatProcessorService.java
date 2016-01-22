package com.huotu.hotcms.web.service.factory;

import com.huotu.hotcms.service.model.thymeleaf.format.TimeFormatParam;
import com.huotu.hotcms.web.thymeleaf.expression.DialectAttributeFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IProcessableElementTag;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 *     时间格式化解析服务
 * </p>
 *
 * @author xhl
 *
 * @since 1.0.0
 */
public class TimeFormatProcessorService {
    private static final Log log = LogFactory.getLog(TimeFormatProcessorService.class);

    private static TimeFormatProcessorService instance;

    private TimeFormatProcessorService() {
    }

    public static TimeFormatProcessorService getInstance() {
        if(instance == null) {
            instance = new TimeFormatProcessorService();
        }
        return instance;
    }

    public Object resolveDataByAttr(IProcessableElementTag tab,ITemplateContext context,Object expressResult) {
        try {
            TimeFormatParam timeFormatParam = DialectAttributeFactory.getInstance().getForeachParam(tab, TimeFormatParam.class);
            String formatterExpress=null;
            if(timeFormatParam!=null){
                formatterExpress=timeFormatParam.getFormat();
            }
            if (expressResult instanceof LocalDateTime) {
                if(StringUtils.isEmpty(formatterExpress)){
                    return ((LocalDateTime) expressResult).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                }else{
                    return ((LocalDateTime) expressResult).format(DateTimeFormatter.ofPattern(formatterExpress));
                }
            }
            if (expressResult instanceof LocalDate) {
                if(StringUtils.isEmpty(formatterExpress)){
                    return ((LocalDate) expressResult).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                }else{
                    return ((LocalDate) expressResult).format(DateTimeFormatter.ofPattern(formatterExpress));
                }
            }
            if (expressResult instanceof LocalTime) {
                if(StringUtils.isEmpty(formatterExpress)){
                    return ((LocalTime) expressResult).format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                }else{
                    return ((LocalTime) expressResult).format(DateTimeFormatter.ofPattern(formatterExpress));
                }
            }
            return "";
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return null;
    }
}
