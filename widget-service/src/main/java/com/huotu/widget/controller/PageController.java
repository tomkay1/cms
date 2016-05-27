package com.huotu.widget.controller;

import com.huotu.widget.model.ResultModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by hzbc on 2016/5/27.
 */
@Controller
@RequestMapping("/pages")
public interface PageController {
    /**
     * <p>获取页面{@link com.huotu.widget.model.Page}</p>
     * @param customerId 商户ID 暂定
     * @return API返回数据模型 {@link com.huotu.widget.model.ResultModel}
     */
    ResultModel getPage(long customerId);

    /**
     * <p>保存界面{@link com.huotu.widget.model.Page}</p>
     * @return API返回数据模型 {@link com.huotu.widget.model.ResultModel}
     */
    ResultModel savePage();

    /**
     * <p>添加页面{@link com.huotu.widget.model.Page}</p>
     * @param customerId 商户ID
     * @return API返回数据模型 {@link com.huotu.widget.model.ResultModel}
     */
    ResultModel addPage(long customerId);

    /**
     * <p>删除界面{@link com.huotu.widget.model.Page}</p>
     * @param pageId 页面ID
     * @return API返回数据模型 {@link com.huotu.widget.model.ResultModel}
     */
    ResultModel deletePage(long pageId);
}
