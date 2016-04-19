package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.Download;
import com.huotu.hotcms.service.model.thymeleaf.foreach.NormalForeachParam;

import java.util.List;

/**
 * Created by chendeyu on 2016/1/11.
 */
public interface DownloadService {
    Boolean saveDownload(Download download);

    Download findById(Long id);

    /**
     * 标签解析时,获取信息
     */
    List<Download> getSpecifyDownloads(String[] specifyIds);

    List<Download> getDownloadList(NormalForeachParam downloadForeachParam);
}
