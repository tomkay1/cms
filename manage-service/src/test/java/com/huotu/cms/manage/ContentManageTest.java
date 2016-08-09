/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage;

import com.huotu.cms.manage.controller.support.CRUDHelper;
import com.huotu.cms.manage.controller.support.CRUDTest;
import com.huotu.cms.manage.page.ManageMainPage;
import com.huotu.cms.manage.page.support.AbstractCMSContentPage;
import com.huotu.cms.manage.page.support.AbstractCRUDPage;
import com.huotu.hotcms.service.ResourcesOwner;
import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.entity.AbstractContent;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.repositoryi.AbstractContentRepository;
import com.huotu.hotcms.service.service.CategoryService;
import com.huotu.hotcms.service.service.ContentService;
import me.jiangcai.lib.resource.service.ResourceService;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.UUID;
import java.util.function.BiConsumer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 所有正文管理测试的公用基类
 *
 * @author CJ
 */
@ActiveProfiles({"test", "no_ck"})
public abstract class ContentManageTest<T extends AbstractContent> extends SiteManageTest {

    private final ContentType contentType;
    private final Class<? extends AbstractCMSContentPage<T>> pageClass;
    @Autowired
    protected ContentService contentService;
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private AbstractContentRepository<T> abstractContentRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ResourceService resourceService;

    private boolean categoryCreation;

    protected ContentManageTest(ContentType contentType, Class<? extends AbstractCMSContentPage<T>> pageClass) {
        this.contentType = contentType;
        this.pageClass = pageClass;
    }

    protected T randomValue(Site site) {
        @SuppressWarnings("unchecked")
        T value = (T) contentService.newContent(contentType);
        value.setTitle(UUID.randomUUID().toString());
        value.setDescription(UUID.randomUUID().toString());
        normalRandom(value, site);

        if (categoryCreation) {
            value.setCategory(randomCategoryData(site, contentType));
        } else {
            value.setCategory(randomCategory(site));
            // 更换为正确的Type
            Category category = value.getCategory();
            while (true) {
                category.setContentType(contentType);
                category = category.getParent();
                if (category == null)
                    break;
            }
        }

        return value;
    }

    protected abstract void normalRandom(T value, Site site);

    protected abstract void assertCreation(T entity, T data);


    @Test
    public void flow() throws Exception {
        Site site = loginAsSite();

        ManageMainPage manageMainPage = initPage(ManageMainPage.class);

        AbstractCMSContentPage<T> page = manageMainPage.toPage(pageClass);

        ContentTest contentTest = new ContentTest(site);

        // 尝试使用不同的方式创建
        // 1 建立新数据源
        // 2 选择已有数据源
        categoryCreation = true;
        CRUDHelper.flow(page, contentTest);
        categoryCreation = false;
        CRUDHelper.flow(page, contentTest);
    }


    class ContentTest implements CRUDTest<T> {

        final Site site;

        public ContentTest(Site site) {
            this.site = site;
        }

        @Override
        public boolean modify() {
            // 修改的时候 并不允许更换数据源
            return true;
        }

        @Override
        public Collection<T> list() throws Exception {
            return abstractContentRepository.findByCategory_SiteAndDeletedFalse(site);
        }

        @Override
        public T randomValue() throws Exception {
            return ContentManageTest.this.randomValue(site);
        }

        @Override
        public BiConsumer<AbstractCRUDPage<T>, T> customAddFunction() throws Exception {
            return null;
        }

        @Override
        public void assertResourcePage(AbstractCRUDPage<T> page, T entity) throws Exception {
            AbstractCMSContentPage<T> contentPage = (AbstractCMSContentPage<T>) page;
            WebElement form = contentPage.getForm();
            // title 对不对
            contentPage.assertInputText(form, "title", entity.getTitle());
            // description
            contentPage.assertResourcePage(entity);
        }

        @Override
        public void assertCreation(T entity, T data) throws Exception {
            Category category = entity.getCategory();
            Category excepted = categoryService.getCategoryByNameAndParent(site, data.getCategory().getName()
                    , data.getCategory().getParent() == null ? null : data.getCategory().getParent().getId()
                    , contentType);
            assertThat(category)
                    .isEqualTo(excepted);

            // TODO 不是所有页面都有这个玩意儿 所以暂时忽略
//            assertThat(entity.getDescription())
//                    .isEqualTo(data.getDescription());
            assertThat(entity.getTitle())
                    .isEqualTo(data.getTitle());
            assertThat(entity.getSerial())
                    .isNotEmpty();

            // 如果提交了资源 那么就校验资源
            if (entity instanceof ResourcesOwner) {
                ResourcesOwner entityResources = (ResourcesOwner) entity;
                ResourcesOwner dataResources = (ResourcesOwner) data;

                for (int i = 0; i < dataResources.getResourcePaths().length; i++) {
                    String srcPath = dataResources.getResourcePaths()[i];
                    if (StringUtils.isEmpty(srcPath)) {
                        assertThat(entityResources.getResourcePaths()[i])
                                .isNull();
                    } else {
                        assertThat(resourceService.getResource(entityResources.getResourcePaths()[i])
                                .getInputStream())
                                .hasSameContentAs(resourceService.getResource(srcPath).getInputStream());
                    }
                }

            }

            ContentManageTest.this.assertCreation(entity, data);
        }
    }


}
