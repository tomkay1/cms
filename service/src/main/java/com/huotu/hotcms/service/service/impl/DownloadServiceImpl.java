package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.entity.Download;
import com.huotu.hotcms.service.repository.DownloadRepository;
import com.huotu.hotcms.service.service.DownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by chendeyu on 2016/1/11.
 */
@Service
public class DownloadServiceImpl implements DownloadService {
    @Autowired
    DownloadRepository downloadRepository;
    @Override
    public Boolean saveDownload(Download download) {
        downloadRepository.save(download);
        return true;
    }

    @Override
    public Download findById(Long id) {

        Download download =  downloadRepository.findOne(id);
        return download;
    }
}
