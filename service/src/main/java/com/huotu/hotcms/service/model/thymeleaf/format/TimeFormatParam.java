package com.huotu.hotcms.service.model.thymeleaf.format;

import com.huotu.hotcms.service.model.thymeleaf.foreach.Rename;
import lombok.Getter;
import lombok.Setter;

/**
 * <P>
 *  时间格式化参数
 * </P>
 *
 * Created by xhl on 2016/1/22.
 */
@Getter
@Setter
public class TimeFormatParam {
    @Rename("format")
    public String format;
}
