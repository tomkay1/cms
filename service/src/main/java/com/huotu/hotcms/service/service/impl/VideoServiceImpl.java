/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.entity.BaseEntity;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.Video;
import com.huotu.hotcms.service.model.thymeleaf.current.VideoCurrentParam;
import com.huotu.hotcms.service.model.thymeleaf.foreach.PageableForeachParam;
import com.huotu.hotcms.service.repository.VideoRepository;
import com.huotu.hotcms.service.service.CategoryService;
import com.huotu.hotcms.service.service.VideoService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by chendeyu on 2016/1/11.
 */
@Service
public class VideoServiceImpl implements VideoService {

    private static Log log = LogFactory.getLog(VideoServiceImpl.class);

    @Autowired
    VideoRepository videoRepository;

    @Autowired
    private CategoryService categoryService;

    @Override
    public Boolean saveVideo(Video video) {
        videoRepository.save(video);
        return true;
    }

    @Override
    public Video findById(Long id) {
        Video video = videoRepository.findOne(id);
        return video;
    }

    @Override
    public Page<Video> getVideoList(PageableForeachParam videoForeachParam) {
        int pageIndex = videoForeachParam.getPageNo() - 1;
        int pageSize = videoForeachParam.getPageSize();
        Sort sort = new Sort(Sort.Direction.DESC, "orderWeight");
        if (!StringUtils.isEmpty(videoForeachParam.getSpecifyIds())) {
            return getSpecifyVideos(videoForeachParam.getSpecifyIds(), pageIndex, pageSize, sort);
        }
        if (!StringUtils.isEmpty(videoForeachParam.getCategoryId())) {
            return getVideos(videoForeachParam, pageIndex, pageSize, sort);
        } else {
            return getAllVideo(videoForeachParam, pageIndex, pageSize, sort);
        }
    }

    private Page<Video> getAllVideo(PageableForeachParam params, int pageIndex, int pageSize, Sort sort) {
        List<Category> subCategories = categoryService.getSubCategories(params.getParentcId());
        if (subCategories.size() == 0) {
            try {
                throw new Exception("父栏目节点没有子栏目");
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        Specification<Video> specification = BaseEntity.Specification(params, subCategories);
        return videoRepository.findAll(specification, new PageRequest(pageIndex, pageSize, sort));
    }

    private Page<Video> getVideos(PageableForeachParam params, int pageIndex, int pageSize, Sort sort) {
        Specification<Video> specification = BaseEntity.Specification(params);
        return videoRepository.findAll(specification, new PageRequest(pageIndex, pageSize, sort));
    }

    private Page<Video> getSpecifyVideos(String[] specifyIds, int pageIndex, int pageSize, Sort sort) {
        List<String> ids = Arrays.asList(specifyIds);
        List<Long> articleIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
        Specification<Video> specification = (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = articleIds.stream().map(id -> cb.equal(root.get("id").as(Long.class), id)).collect(Collectors.toList());
            return cb.or(predicates.toArray(new Predicate[predicates.size()]));
        };
        return videoRepository.findAll(specification, new PageRequest(pageIndex, pageSize, sort));
    }

    @Override
    public Video getVideoByParam(VideoCurrentParam videoCurrentParam) {
        Video video = null;
        if (videoCurrentParam != null) {
            if (videoCurrentParam.getId() != null) {
                video = videoRepository.getOne(videoCurrentParam.getId());
            } else {
                video = videoRepository.getOne(videoCurrentParam.getDefaultid());
            }
        }
        ///图片资源处理
        video = setVideoResourcesPath(video);
        return video;
    }

    @Override
    public Video setVideoResourcesPath(Video video) {
        if (video != null) {
            Category category = video.getCategory();
            if (category != null) {
                Site site = category.getSite();
                if (site != null && !StringUtils.isEmpty(site.getResourceUrl())) {
                    if (!StringUtils.isEmpty(video.getVideoUrl())) {
                        video.setVideoUrl(site.getResourceUrl() + video.getVideoUrl());
                    }
                    if (!StringUtils.isEmpty(video.getThumbUri())) {
                        video.setThumbUri(site.getResourceUrl() + video.getThumbUri());
                    }
//                    if(!StringUtils.isEmpty(video.getOutLinkUrl())){
//                        video.setVideoUrl(site.getResourceUrl()+video.getOutLinkUrl());
//                    }
                }
            }
        }
        return video;
    }
}
