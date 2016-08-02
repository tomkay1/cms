/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller.support;

import com.huotu.cms.manage.exception.RedirectException;
import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.entity.AbstractContent;
import com.huotu.hotcms.service.entity.Download;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Login;
import com.huotu.hotcms.service.exception.BadCategoryInfoException;
import com.huotu.hotcms.service.model.ContentExtra;
import com.huotu.hotcms.service.service.CategoryService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

/**
 * 所有内容管理的控制器
 *
 * @param <T>  我们要管理内容的类型
 * @param <ED> extra model 提交数据（包括新增,修改）MVC系统额外数据
 * @author CJ
 */
public abstract class ContentManageController<T extends AbstractContent, ED extends ContentExtra>
        extends SiteManageController<T, Long, ED, ED> {

    private static final Log log = LogFactory.getLog(ContentManageController.class);

    @Autowired
    private CategoryService categoryService;

    /**
     * @return 这个内容的模型
     */
    protected abstract ContentType contentType();

    @Override
    protected Specification<T> prepareIndex(Login login, Site site, Model model, RedirectAttributes attributes)
            throws RedirectException {
        forCategoryList(site, model);
        return (root, query, cb) -> cb.equal(root.get("category").get("site").as(Site.class), site);
    }

    private void forCategoryList(Site site, Model model) {
        model.addAttribute("categories", categoryService.getCategoriesForContentType(site, contentType()));
    }

    @Override
    protected void prepareOpen(Login login, T data, Model model, RedirectAttributes attributes)
            throws RedirectException {
        forCategoryList(data.getCategory().getSite(), model);
        super.prepareOpen(login, data, model, attributes);
    }

    @Override
    protected T preparePersist(Login login, Site site, T data, ED extra, RedirectAttributes attributes)
            throws RedirectException {
        try {
            data.setCategory(categoryService.getCategoryByNameAndParent(site, extra.getCategoryName()
                    , extra.getParentCategoryId(), contentType()));
            if (!(data instanceof Download)) {//下载模型不能简单使用图片的处理操作
                uploadTempImageToOwner(data, extra.getTempPath());
            }
        } catch (BadCategoryInfoException e) {
            throw new RedirectException(rootUri(), "该数据源已存在，并且不符合你要求。", e);
        } catch (IOException e) {
            log.warn("图片转存异常：" + e.getMessage());
            throw new RedirectException(rootUri(), "图片转存异常：" + e.getMessage(), e);
        }
        return preparePersistContext(login, site, data, extra, attributes);
    }

    protected abstract T preparePersistContext(Login login, Site site, T data, ED extra, RedirectAttributes attributes)
            throws RedirectException;
}
