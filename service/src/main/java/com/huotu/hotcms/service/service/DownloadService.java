package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.Download;
import com.huotu.hotcms.service.model.thymeleaf.foreach.DownloadForeachParam;

import java.util.List;

/**
 * Created by chendeyu on 2016/1/11.
 */
public interface DownloadService {
    Boolean saveDownload(Download download);
    Download findById(Long id);

    List<Download> getSpecifyDownloads(String[] specifyIds);

    List<Download> getDownloadList(DownloadForeachParam downloadForeachParam);
}
