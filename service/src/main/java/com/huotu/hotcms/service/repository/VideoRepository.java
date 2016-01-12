package com.huotu.hotcms.service.repository;

import com.huotu.hotcms.service.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by chendeyu on 2016/1/11.
 */
public interface VideoRepository extends JpaRepository<Video, Long>,JpaSpecificationExecutor {
}
