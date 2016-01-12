package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.Download;

/**
 * Created by chendeyu on 2016/1/11.
 */
public interface DownloadService {
    Boolean saveDownload(Download download);
    Download findById(Long id);
}
