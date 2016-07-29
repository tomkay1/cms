/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.service;

import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Gallery;
import com.huotu.hotcms.service.entity.GalleryItem;
import com.huotu.hotcms.service.entity.Link;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 控件获取数据源的接口
 * Created by lhx on 2016/6/28.
 */
@Service
public interface CMSDataSourceService {

    /**
     * <p>查询 当前站点下<b>可用数据源（category）且为图库类型</b></p>
     * <p>下的所有图库模型（Gallery）</p>
     *
     * @return 返回当前站点的所有可用的图库模型
     */
    List<Gallery> findGallery();

    /**
     * <p>查询图库模型下的所有可用图库列表</p>
     *
     * @param galleryId 图库id
     * @return 返回当前站点指定图库模型的图库列表
     */
    List<GalleryItem> findGalleryItem(Long galleryId);

    /**
     * <p>查询当前站点下所有可用的链接模型数据源</p>
     *
     * @return 返回当前站点下所有链接模型的栏目（数据源）
     */
    List<Category> findLinkCategory();

    /**
     * <p>查询当前站点下所有可用的文章模型，且为一级分类的数据源</p>
     *
     * @return 返回当前站点下所有一级 文章模型的数据源
     */
    List<Category> findParentArticleCategory();

    /**
     * <p>查询当前站点下所有可用的文章模型，且为指定数据源的（包括间接）子级</p>
     *
     * @param parentId 父元素id
     * @return 子元素列表 json;如果没有有效数据则返回空字符串（不是null）
     */
    String findChildrenArticleCategory(Long parentId);

    /**
     * <p>查询当前站点可用page列表</p>
     *
     * @return 返回当前站点json Page
     */
    String findSitePage();

    /**
     * <p>查询指定链接数据源下的所有链接模型</p>
     *
     * @param categoryId 栏目id（链接模型的数据源id）
     * @return 返回指定链接数据源下的全部链接模型
     */
    List<Link> findLink(Long categoryId);

}
