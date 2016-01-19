package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.Video;
import com.huotu.hotcms.service.model.thymeleaf.current.VideoCurrentParam;
import com.huotu.hotcms.service.model.thymeleaf.foreach.PageableForeachParam;
import com.huotu.hotcms.service.repository.VideoRepository;
import com.huotu.hotcms.service.service.CategoryService;
import com.huotu.hotcms.service.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by chendeyu on 2016/1/11.
 */
@Service
public class VideoServiceImpl implements VideoService {

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
        Video video =  videoRepository.findOne(id);
        return video;
    }

    @Override
    public Page<Video> getVideoList(PageableForeachParam videoForeachParam) {
        int pageIndex = videoForeachParam.getPageno()-1;
        int pageSize = videoForeachParam.getPagesize();
        Sort sort = new Sort(Sort.Direction.DESC, "orderWeight");
        if(!StringUtils.isEmpty(videoForeachParam.getSpecifyids())) {
            return getSpecifyVideos(videoForeachParam.getSpecifyids(), pageIndex, pageSize, sort);
        }
        if(!StringUtils.isEmpty(videoForeachParam.getCategoryid())) {
            return getVideos(videoForeachParam, pageIndex, pageSize, sort);
        }else {
            return getAllVideo(videoForeachParam, pageIndex, pageSize, sort);
        }
    }

    private Page<Video> getAllVideo(PageableForeachParam params, int pageIndex, int pageSize, Sort sort) {
        List<Category> subCategories =  categoryService.getSubCategories(params.getParentcid());
        if(subCategories.size()==0) {
            try {
                throw new Exception("父栏目节点没有子栏目");
            }catch (Exception e) {
                e.printStackTrace();//TODO 日志处理
            }
        }
        Specification<Article> specification = (root, criteriaQuery, cb) -> {
            List<Predicate> p1 = new ArrayList<>();
            for(Category category : subCategories) {
                p1.add(cb.equal(root.get("category").as(Category.class), category));
            }
            Predicate predicate = cb.or(p1.toArray(new Predicate[p1.size()]));
            List<Predicate> predicates = new ArrayList<>();
            if(!StringUtils.isEmpty(params.getExcludeids())) {
                List<String> ids = Arrays.asList(params.getExcludeids());
                List<Long> articleIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
                predicates = articleIds.stream().map(id -> cb.notEqual(root.get("id").as(Long.class), id)).collect(Collectors.toList());
            }
            predicates.add(cb.equal(root.get("deleted").as(Boolean.class), false));
            predicates.add(predicate);
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        return videoRepository.findAll(specification,new PageRequest(pageIndex,pageSize,sort));
    }

    private Page<Video> getVideos(PageableForeachParam params, int pageIndex, int pageSize, Sort sort) {
        Specification<Article> specification = (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(!StringUtils.isEmpty(params.getExcludeids())) {
                List<String> ids = Arrays.asList(params.getExcludeids());
                List<Long> articleIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
                predicates = articleIds.stream().map(id -> cb.notEqual(root.get("id").as(Long.class), id)).collect(Collectors.toList());
            }
            predicates.add(cb.equal(root.get("deleted").as(Boolean.class),false));
            predicates.add(cb.equal(root.get("category").get("id").as(Long.class), params.getCategoryid()));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        return videoRepository.findAll(specification,new PageRequest(pageIndex,pageSize,sort));
    }

    private Page<Video> getSpecifyVideos(String[] specifyIds, int pageIndex, int pageSize, Sort sort) {
        List<String> ids = Arrays.asList(specifyIds);
        List<Long> articleIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
        Specification<Article> specification = (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = articleIds.stream().map(id -> cb.equal(root.get("id").as(Long.class), id)).collect(Collectors.toList());
            return cb.or(predicates.toArray(new Predicate[predicates.size()]));
        };
        return videoRepository.findAll(specification,new PageRequest(pageIndex,pageSize,sort));
    }

    @Override
    public Video getVideoByParam(VideoCurrentParam videoCurrentParam) {
        Video video=null;
        if (videoCurrentParam != null) {
            if (videoCurrentParam.getId() != null) {
                video= videoRepository.getOne(videoCurrentParam.getId());
            } else {
                video= videoRepository.getOne(videoCurrentParam.getDefaultid());
            }
        }
        ///图片资源处理
        video=setVideoResourcesPath(video);
        return video;
    }

    @Override
    public Video setVideoResourcesPath(Video video) {
        if(video!=null){
            Category category=video.getCategory();
            if(category!=null) {
                Site site = category.getSite();
                if(site!=null&&!StringUtils.isEmpty(site.getResourceUrl())){
                    if(!StringUtils.isEmpty(video.getVideoUrl())){
                        video.setVideoUrl(site.getResourceUrl()+video.getVideoUrl());
                    }
                    if(!StringUtils.isEmpty(video.getThumbUri())) {
                        video.setThumbUri(site.getResourceUrl() + video.getThumbUri());
                    }
                    if(!StringUtils.isEmpty(video.getOutLinkUrl())){
                        video.setVideoUrl(site.getResourceUrl()+video.getOutLinkUrl());
                    }
                }
            }
        }
        return video;
    }
}
