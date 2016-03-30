package com.huotu.hotcms.admin.controller.decoration;

import com.huotu.hotcms.service.common.LayoutEnum;
import com.huotu.hotcms.service.model.widget.WidgetBase;
import com.huotu.hotcms.service.util.ResultOptionEnum;
import com.huotu.hotcms.service.util.ResultView;
import com.huotu.hotcms.service.widget.service.PageResourceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 组件模版控制器
 * </p>
 *
 * @version 1.2
 * @since xhl
 */
@Controller
@RequestMapping("/widgetTemplate")
public class WidgetTemplateController {
    private static final Log log = LogFactory.getLog(WidgetTemplateController.class);

    @Autowired
    private PageResourceService pageResourceService;

    @RequestMapping("/{id}")
    @ResponseBody
    public ResultView getWidgetTemplate(@RequestParam("url") String url, @PathVariable("id") Long id) {
        ResultView resultView = null;
        try {
            WidgetBase widgetBase=new WidgetBase();
            widgetBase.setId(id);
            widgetBase.setWidgetUri(url);
            String html=pageResourceService.getWidgetTemplateByWidgetBase(widgetBase);
            resultView=new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), html);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            resultView=new ResultView(ResultOptionEnum.SERVERFAILE.getCode(), ResultOptionEnum.SERVERFAILE.getValue(),null);
        }
        return resultView;
    }
}
