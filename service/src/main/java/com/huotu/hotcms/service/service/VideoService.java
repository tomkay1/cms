package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.Video;
import com.huotu.hotcms.service.model.thymeleaf.foreach.VideoForeachParam;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * Created by chendeyu on 2016/1/11.
 */
@Service
public interface VideoService {
    Boolean saveVideo(Video video);
    Video findById(Long id);

    Page<Video> getVideoList(VideoForeachParam videoForeachParam);
}
