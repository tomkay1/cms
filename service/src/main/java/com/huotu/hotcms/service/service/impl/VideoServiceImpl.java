package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.entity.Video;
import com.huotu.hotcms.service.repository.VideoRepository;
import com.huotu.hotcms.service.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by chendeyu on 2016/1/11.
 */
@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    VideoRepository videoRepository;

    @Override
    public Boolean saveVideo(Video video) {
        videoRepository.save(video);
        return true;
    }

    @Override
    public Video findById(Long id) {
        Video video =  videoRepository.findOne(id);
        return video;
    }
}
