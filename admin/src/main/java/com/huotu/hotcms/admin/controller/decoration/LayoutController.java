package com.huotu.hotcms.admin.controller.decoration;

import com.huotu.hotcms.service.common.EnumUtils;
import com.huotu.hotcms.service.common.LayoutEnum;
import com.huotu.hotcms.service.model.widget.WidgetResult;
import com.huotu.hotcms.service.util.ResultOptionEnum;
import com.huotu.hotcms.service.util.ResultView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

/**
 * <p>
 * 布局控制器
 * </p>
 *
 * @version 1.2
 * @since xhl
 */
@Controller
@RequestMapping("/layout")
public class LayoutController {
    private static final Log log = LogFactory.getLog(LayoutController.class);

    @RequestMapping("/{id}")
    @ResponseBody
    public ResultView widgetTypeList(@PathVariable("id") Integer id) {
        ResultView resultView = null;
        try {
            LayoutEnum layoutEnum = EnumUtils.valueOf(LayoutEnum.class, id);
            String layoutId=UUID.randomUUID().toString();
            if (layoutEnum == null) {
                resultView = new ResultView(ResultOptionEnum.NOFIND.getCode(), ResultOptionEnum.NOFIND.getValue(), null);
            }else {
                String layoutTemplate=layoutEnum.getLayoutTemplate(null,true,layoutId);
                WidgetResult widgetResult=new WidgetResult();
                widgetResult.setId(layoutId);
                widgetResult.setTemplate(layoutTemplate);
                widgetResult.setModuleCount(layoutEnum.getModuleCount());
                resultView = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), widgetResult);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return resultView;
    }
}
