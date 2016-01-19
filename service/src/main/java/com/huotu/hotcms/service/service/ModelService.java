package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.DataModel;
import com.huotu.hotcms.service.util.PageData;

/**
 * Created by Administrator xhl 2015/12/25.
 */
public interface ModelService {
     PageData<DataModel> getPage(String name,int page,int pageSize);

}
