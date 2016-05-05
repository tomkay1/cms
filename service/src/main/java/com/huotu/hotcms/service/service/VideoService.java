package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.Video;
import com.huotu.hotcms.service.model.thymeleaf.current.VideoCurrentParam;
import com.huotu.hotcms.service.model.thymeleaf.foreach.PageableForeachParam;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * Created by chendeyu on 2016/1/11.
 */
@Service
public interface VideoService {
    Boolean saveVideo(Video video);
    Video findById(Long id);

    Page<Video> getVideoList(PageableForeachParam pageableForeachParam);

    Video getVideoByParam(VideoCurrentParam videoCurrentParam);

    Video setVideoResourcesPath(Video video);
}
