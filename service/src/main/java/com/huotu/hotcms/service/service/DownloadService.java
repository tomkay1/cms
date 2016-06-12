package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.Download;
import com.huotu.hotcms.service.model.thymeleaf.foreach.PageableForeachParam;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by chendeyu on 2016/1/11.
 */
public interface DownloadService {
    Boolean saveDownload(Download download);

    Download findById(Long id);

    /**
     * 标签解析时,获取download信息
     */
    List<Download> getSpecifyDownloads(String[] specifyIds);

//    List<Download> getDownloadList(NormalForeachParam downloadForeachParam);

    /**
     * 根据pageableForeachParam 实体类来获得分页链接模型数据列表,
     * pageableForeachParam 该实体类是通过编写的参数标签或者当前http 上下文 request中获得参数实体
     *
     * @param pageableForeachParam 该实体类是通过编写的参数标签或者当前http 上下文 request中获得参数实体
     * @return
     * @throws Exception
     */
    Page<Download> getDownloadList(PageableForeachParam pageableForeachParam) throws Exception;
}
