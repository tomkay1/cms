package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.common.ConfigInfo;
import com.huotu.hotcms.service.entity.*;
import com.huotu.hotcms.service.model.widget.WidgetDefaultPage;
import com.huotu.hotcms.service.model.widget.WidgetPage;
import com.huotu.hotcms.service.repository.*;
import com.huotu.hotcms.service.service.SiteService;
import com.huotu.hotcms.service.util.HttpUtils;
import com.huotu.hotcms.service.util.PageData;
import com.huotu.hotcms.service.util.SerialUtil;
import com.huotu.hotcms.service.widget.service.PageResolveService;
import com.huotu.hotcms.service.widget.service.StaticResourceService;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.xml.bind.JAXB;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by chendeyu on 2015/12/24.
 */
@Service
public class SiteServiceImpl implements SiteService {

    @Autowired
    SiteRepository siteRepository ;

    @Autowired
    HostRepository hostRepository;

    @Autowired
    ConfigInfo configInfo;

    @Autowired
    StaticResourceService resourceService;

    @Autowired
    PageResolveService pageResolveService;

    @Autowired TemplateRepository templateRepository;

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    CustomPagesRepository customPagesRepository;



    @Override
    public PageData<Site> getPage(Integer customerId,String name, int page, int pageSize) {
        PageData<Site> data = null;
        Specification<Site> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(name)) {
                predicates.add(cb.like(root.get("name").as(String.class), "%" + name + "%"));
            }
            predicates.add(cb.equal(root.get("deleted").as(String.class), false));
            predicates.add(cb.equal(root.get("customerId").as(Integer.class), customerId));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        Page<Site> pageData = siteRepository.findAll(specification,new PageRequest(page - 1, pageSize));
        if (pageData != null) {
            List<Site> site =pageData.getContent();
            for(Site site1 : site){
                site1.setHosts(null);
            }
            data = new PageData<Site>();
            data.setPageCount(pageData.getTotalPages());
            data.setPageIndex(pageData.getNumber());
            data.setPageSize(pageData.getSize());
            data.setTotal(pageData.getTotalElements());
            data.setRows((Site[])pageData.getContent().toArray(new Site[pageData.getContent().size()]));
        }
        return  data;
    }

    @Override
    public Site findBySiteIdAndCustomerId(Long siteId,int customerId) {
        Site site =siteRepository.findBySiteIdAndCustomerId(siteId,customerId);
        return  site;
    }


    @Override
    public Site getSite(long siteId) {
        return siteRepository.findOne(siteId);
    }

    @Override
    public Boolean save(Site site) {
        siteRepository.save(site);
        return true;
    }

    @Override
    public Site saveAndFlush(Site site) {
        return siteRepository.save(site);
    }

    @Override
    public Set<Site> findByCustomerIdAndDeleted(Integer customerId, boolean deleted) {
       Set<Site> siteList=siteRepository.findByCustomerIdAndDeleted(customerId,deleted);
        for(Site site : siteList){
            site.setHosts(null);
            site.setRegion(null);
        }
        return siteList;
    }


    @Override
    public boolean siteCopy(long templateId, Site customerSite) throws Exception {
         //根据模板ID读取到相应的站点
        Template template=templateRepository.findOne(templateId);
        Site templateSite=template.getSite();
        List<CustomPages> customPages=customPagesRepository.findBySite(templateSite);
        for(CustomPages customPage:customPages){
            String templateResourceConfigUrl=configInfo.getResourcesConfig(templateSite.getCustomerId()
                    ,templateSite.getSiteId())+"/"+customPage.getId()+".xml";
            if(templateResourceConfigUrl!=null){
                URI url= resourceService.getResource(templateResourceConfigUrl);
                InputStream inputStream = HttpUtils.getInputStreamByUrl(url.toURL());
                WidgetPage widgetPage = JAXB.unmarshal(inputStream, WidgetPage.class);
                pageResolveService.createPageAndConfigByWidgetPage(widgetPage,customerSite.getCustomerId()
                        ,customerSite.getSiteId(),false);
            }
        }
        createDefaultWidgetPage(templateSite, customerSite);
        deepCopy(templateSite, customerSite);
        return false;
    }

    /**
     * 深度复制   站点下属类目全部复制
     * @param templateSite  模板站点
     * @param customerSite  商户站点
     */
    private void deepCopy(Site templateSite, Site customerSite) {
        /*对Category的复制*/
        customerSite=siteRepository.findOne(customerSite.getSiteId());//确保查询的数据是完备的

        List<Category> categories=categoryRepository.findBySite(templateSite);
        for(Category category:categories){
            Category newCategory=new Category();
            newCategory.setSerial(SerialUtil.formartSerial(customerSite));
            newCategory.setSite(customerSite);
            newCategory.setSite(customerSite);
            newCategory.setCustomerId(customerSite.getCustomerId());
            newCategory.setParent(category.getParent());
            newCategory.setName(category.getName());
            newCategory.setCustom(category.isCustom());
            newCategory.setDeleted(category.isDeleted());
            newCategory.setRoute(category.getRoute());
            category=categoryRepository.save(newCategory);
            //itemsCopy(category,customerSite);
        }
        /*自定义页面*/

        List<CustomPages> customPages=customPagesRepository.findBySite(templateSite);
        for(CustomPages customPage:customPages){
            customPage.setSerial(SerialUtil.formartSerial(customerSite));
            customPage.setId(null);
            customPage.setCustomerId(customerSite.getCustomerId());
            customPagesRepository.save(customPage);
        }

    }

    /**
     * 其他数据源的深度复制
     * @param category 复制后的栏目
     * @param customerSite 用户站点
     */
    private void itemsCopy(Category category,Site customerSite) {
        //文章复制
       List<Article> articles=articleRepository.findByCategory(category);
        for(Article article:articles){
            article.setId(null);
            article.setCustomerId(customerSite.getCustomerId());
            article.setCategory(category);
            articleRepository.save(article);
        }
    }

    /** 创建公共界面
     * <p>
     *     <em>目前默认公共界面如下：</em>
     *     <ul>
     *         <li>公共头部 {@link com.huotu.hotcms.service.model.widget.WidgetDefaultPage#head }</li>
     *         <li>搜索结果界面 {@link com.huotu.hotcms.service.model.widget.WidgetDefaultPage#search }</li>
     *         <li>公共尾部 {@link com.huotu.hotcms.service.model.widget.WidgetDefaultPage#bottom }</li>
     *     </ul>
     * </p>
     * @param templateSite 模板站点
     * @param customerViewOrUserSite  模板预览或者使用站点
     * @throws IOException 其他异常
     * @throws URISyntaxException 其他异常
     *
     * @since  v2.0
     */
    private void createDefaultWidgetPage(Site templateSite, Site customerViewOrUserSite)
            throws IOException, URISyntaxException {

        for(WidgetDefaultPage widgetDefaultPage:WidgetDefaultPage.values()){
            WidgetPage defaultWidgetPage=pageResolveService.getWidgetPageByConfig(widgetDefaultPage.name()+".xml", templateSite);
            if(defaultWidgetPage!=null){
                pageResolveService.createDefaultPageConfigByWidgetPage(defaultWidgetPage,customerViewOrUserSite.getCustomerId()
                        ,customerViewOrUserSite.getSiteId(),widgetDefaultPage.name());
            }
        }
    }
}
