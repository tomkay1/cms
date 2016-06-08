package com.huotu.cms.manage.controller.decoration;

import com.huotu.hotcms.service.util.DesEncryption;
import com.huotu.hotcms.service.util.ResultOptionEnum;
import com.huotu.hotcms.service.util.ResultView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2016/4/7.
 */
@Controller
@RequestMapping("/common")
public class CommonController {
    private static final Log log = LogFactory.getLog(CommonController.class);

    @RequestMapping(value = "/getWidgetPageData",method = RequestMethod.POST)
    @ResponseBody
    public ResultView getWidgetPageData(String widget) {
        ResultView resultView = null;
        try {
            String data= DesEncryption.decryptData(widget);
            resultView=new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(),data);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            resultView=new ResultView(ResultOptionEnum.SERVERFAILE.getCode(), ResultOptionEnum.SERVERFAILE.getValue(),null);
        }
        return resultView;
    }
}
