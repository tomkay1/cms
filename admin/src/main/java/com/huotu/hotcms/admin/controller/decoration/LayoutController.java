package com.huotu.hotcms.admin.controller.decoration;

import com.huotu.hotcms.service.common.LayoutEnum;
import com.huotu.hotcms.service.util.ResultOptionEnum;
import com.huotu.hotcms.service.util.ResultView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
            LayoutEnum layoutEnum = LayoutEnum.valueOf(id);
            if (layoutEnum == null) {
                resultView = new ResultView(ResultOptionEnum.NOFIND.getCode(), ResultOptionEnum.NOFIND.getValue(), null);
            }else {
                resultView = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), layoutEnum.getLayoutTemplate(null,true));
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return resultView;
    }
}
