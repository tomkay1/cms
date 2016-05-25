package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.config.ServiceTestConfig;
import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.Template;
import com.huotu.hotcms.service.model.widget.WidgetPage;
import com.huotu.hotcms.service.repository.ArticleRepository;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.repository.SiteRepository;
import com.huotu.hotcms.service.repository.TemplateRepository;
import com.huotu.hotcms.service.util.HttpUtils;
import com.huotu.hotcms.service.util.SerialUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.JAXB;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by fawzi on 2016/5/12.
 * 用于site相关测试
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ServiceTestConfig.class)
@WebAppConfiguration
@Transactional
public class SiteTest {
    @Autowired SiteService siteService;

    @Autowired
    SiteRepository siteRepository;
    /**
     * 站点复制测试
     */
    @Test
    public void testSiteCopy() throws Exception {
        long templateID=3;
        long siteId=71;
        Site customerSite=siteRepository.findOne(siteId);
        siteService.siteCopy(templateID,customerSite);
    }


    /**
     * 注意：需要先部署整个项目才可以启动该单元测试。不然无法复制资源文件
     */
    @Autowired
    ArticleRepository articleRepository;
    @Test
    public void testQuery(){
        long siteID=4471;
        Article templateSiteArticle=articleRepository.findArticleBySiteId(siteID);
        long id=templateSiteArticle.getId();
    }

    @Qualifier("templateRepository")
    @Autowired
    TemplateRepository templateRepository;
    @Autowired
    CategoryRepository categoryRepository;


    @Test
    public void testDeepCopy(){
        long templateID=3;
        long siteId=71;
        Site customerSite=siteRepository.findOne(siteId);
        Template template=templateRepository.findOne(templateID);
        Site templateSite=template.getSite();

        List<Category> categories=categoryRepository.findBySite(templateSite);
        try{
            for(Category category:categories){
                category.setSerial(SerialUtil.formartSerial(customerSite));
                category.setSite(customerSite);
                category.setId(null);
                category.setSite(customerSite);
                category.setCustomerId(customerSite.getCustomerId());
                category=categoryRepository.save(category);
            }
        }catch (Throwable e){
            e.printStackTrace();
        }

    }

}
