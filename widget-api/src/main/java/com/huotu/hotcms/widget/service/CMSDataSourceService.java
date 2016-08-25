/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.service;

import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.entity.AbstractContent;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Notice;
import com.huotu.hotcms.service.model.BaseModel;
import com.huotu.hotcms.service.model.GalleryItemModel;
import com.huotu.hotcms.service.model.LinkModel;
import com.huotu.hotcms.service.model.widget.VideoModel;
import com.huotu.hotcms.widget.entity.PageInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 控件获取数据源的接口
 *
 * Created by lhx on 2016/6/28.
 */
@Service
public interface CMSDataSourceService {
    /**
     * <p>查询当前站点下所有可用的公告模型数据源</p>
     *
     * @return 返回当前站点下所有链接模型的栏目（数据源）
     */
    List<Category> findNoticeCategory();

    /**
     * <p>查询当前站点下所有可用的公告模型数据源</p>
     *
     * @return 返回当前站点下所有链接模型的栏目（数据源）
     * @param serial
     * @param count
     */
    List<Notice> findNoticeContent(String serial, int count);

    /**
     * <p>查询当前站点下所有可用的链接模型数据源</p>
     *
     * @return 返回当前站点下所有链接模型的栏目（数据源）
     */
    List<Category> findLinkCategory();


    /**
     * <p>查询指定链接数据源下的所有链接模型</p>
     *
     * @param serial （链接模型的数据源serial）
     * @return 返回指定链接数据源下的全部链接模型
     */
    List<LinkModel> findLinkContent(String serial);

    /**
     * <p>查询当前站点下所有可用的图库数据源</p>
     *
     * @return 返回当前站点下所有链接模型的栏目（数据源）
     */
    List<Category> findGalleryCategory();

    /**
     * <p>查询当前站点下所有可用的图库数据源</p>
     *
     * @return 返回当前站点下所有链接模型的栏目（数据源）
     */
    List<GalleryItemModel> findGalleryItems(String serial, int count);


    /**
     * 查询当前站点下视频数据源列表
     * @return 视频数据源列表
     */
    List<Category> findVideoCategory();

    /**
     * 查询当前站点下，指定数据源serial的 视频内容列表
     *
     * @param serial 数据源的serial
     * @return 视频内容列表
     */
    List<VideoModel> findVideoContent(String serial);

    /**
     * 查询当前站点下文章数据源列表
     *
     * @return 视频数据源列表
     */
    List<Category> findArticleCategory();

    /**
     * 查询当前站点下，指定数据源serial的 视频内容列表
     *
     * @param serial 数据源的serial
     * @param count 数量上限,缺省为10
     * @return 视频内容列表
     */
    List<BaseModel> findArticleContent(String serial, int count);

    /**
     * 查找内容
     *
     * @param contentType 内容类型
     * @param pageable    分页根据
     * @param search      内容可能依赖的模糊查询
     * @return 分组查询的结果
     */
    Page<? extends AbstractContent> findContent(ContentType contentType, Pageable pageable, String search);


    /**
     * 根据serial 返回当前站点的pageInfo
     *
     * @param serial serial
     * @return pageInfo
     */
    PageInfo findPageInfoContent(String serial);
}
