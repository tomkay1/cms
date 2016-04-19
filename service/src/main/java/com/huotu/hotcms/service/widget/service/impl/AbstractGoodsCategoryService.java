package com.huotu.hotcms.service.widget.service.impl;

import com.alibaba.fastjson.JSON;
import com.huotu.hotcms.service.service.HttpService;
import com.huotu.hotcms.service.util.ApiResult;
import com.huotu.hotcms.service.widget.model.GoodsCategory;
import com.huotu.hotcms.service.widget.service.GoodsCategoryService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * Created by cwb on 2016/4/10.
 */
public abstract class AbstractGoodsCategoryService implements GoodsCategoryService {

    protected String host;
    protected Integer port;
    public static final String REQUEST_URI = "/categories/search/dataByMerchantId";

    @Autowired
    private HttpService httpService;

    @Override
    public List<GoodsCategory> getGoodsCategories(int customerId){
        ApiResult<String> apiResult = invokeGoodsCatgProce(customerId);
        if(apiResult.getCode()!=200) {
            return new ArrayList<>();
        }
        return JSON.parseArray(apiResult.getData(), GoodsCategory.class);
    }

    private ApiResult<String> invokeGoodsCatgProce(Integer customerId) {
        Map<String,Object> params = new TreeMap<>();
        params.put("merchantId", customerId);
//        return httpService.httpGet_prod("http", host, port, REQUEST_URI, params);
        return httpService.httpGet_prod(REQUEST_URI,params);
    }

}
