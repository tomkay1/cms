package com.huotu.hotcms.service;

import com.huotu.hotcms.entity.DataModel;
import com.huotu.hotcms.util.PageData;
import org.springframework.data.domain.Page;

/**
 * Created by Administrator xhl 2015/12/25.
 */
public interface ModelService {
//    Page<DataModel> getPage(String name,int page,int pageSize);
     PageData<DataModel> getPage(String name,int page,int pageSize);

//     Page<DataModel> findByName(String name,int page,int pageSize);
}
