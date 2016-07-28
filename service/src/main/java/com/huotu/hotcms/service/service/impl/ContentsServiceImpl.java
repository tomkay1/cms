/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.entity.AbstractContent;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.repository.ContentRepository;
import com.huotu.hotcms.service.service.CategoryService;
import com.huotu.hotcms.service.service.ContentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;

@Service
public class ContentsServiceImpl implements ContentsService {


    @Autowired
    ContentRepository contentRepository;

    @Autowired
    CategoryService categoryService;


    @Override
    public Iterable<AbstractContent> list(String title, Site site, Long category, Pageable pageable) {
        Specification<AbstractContent> specification = (root, query, cb) -> {
            Predicate predicate = cb.equal(root.get("category").get("site"), site);
            if (title != null) {
                Predicate titlePredicate = cb.or(cb.like(root.get("title"), "%" + title + "%")
                        , cb.like(root.get("description"), "%" + title + "%"));
                predicate = cb.and(titlePredicate, predicate);
            }

            if (category != null) {
                predicate = cb.and(cb.equal(root.get("category").get("id"), category), predicate);
            }
            return predicate;
        };

        if (pageable == null)
            return contentRepository.findAll(specification);
        return contentRepository.findAll(specification, pageable);
    }

    @Override
    public AbstractContent findById(Long contentId) {
        return contentRepository.findOne(contentId);
    }


//    @Override
//    public PageData<Contents> getPage(String title, Long siteId, Long category, int page, int pageSize) {
//        PageData<Contents> data = null;
//        List<Object[]> contentsList =new ArrayList<>();
//        List<Object[]> contentsSize =new ArrayList<>();
//        if(title==null){
//            title="%"+""+"%";
//        }
//        else{
//            title ="%"+title+"%";
//        }
//        if(category==-1){//当搜索条件只有站点时
//            contentsList = baseEntityRepository.findAllContentsBySiteIdAndName(siteId, title,(page-1)*pageSize,pageSize);
//            contentsSize =baseEntityRepository.findContentsSizeBySiteIdAndName(siteId, title);
//        }
//        else{
//            String parentIds=categoryService.getCategoryParentIds(category);
//            contentsList=baseEntityRepository.findAllContentsBySiteIdAndCategoryIdsAndName(siteId,parentIds,title,(page-1)*pageSize,pageSize);
//            contentsSize =baseEntityRepository.findContentsSizeBySiteIdAndCategoryIdsAndName(siteId, parentIds, title);
//        }
//        List<Contents> contentsList1 = new ArrayList<>();
//        for(Object[] o : contentsList) {
//            Contents contents = new Contents();
//            contents.setTitle((String)o[0]);
//            contents.setDescription((String)o[1]);
//            contents.setName((String) o[2]);
//            contents.setId((Long) o[3]);
//            Integer modelId=(Integer) o[4];
//            if(modelId!=null) {
//                contents.setModelId(modelId);
//                contents.setModel(EnumUtils.valueOf(ModelType.class, modelId).toString().toLowerCase());
//                contents.setModelname(EnumUtils.valueOf(ModelType.class, modelId).getValue().toString());
//            }
//            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//定义格式，不显示毫秒
//            String str = df.format(o[5]);
//            contents.setCreateTime((String) str);
//            contentsList1.add(contents);
//        }
//        int PageCount =0;
//        if (contentsList.size()!=0){
//            int yushu=contentsSize.size()%pageSize;
//            if(yushu==0){
//                PageCount =contentsSize.size()/pageSize;
//            }
//            else {
//                PageCount =contentsSize.size()/pageSize+1;
//            }
//        }
//        else{
//             PageCount =0;
//        }
//        data = new PageData<Contents>();
//        data.setPageCount(PageCount);//总页码
//        data.setPageIndex(page);//页码
//        data.setPageSize(contentsList.size());//页容量
//        data.setTotal(contentsSize.size());//总数
//        data.setRows((Contents[])contentsList1.toArray(new Contents[contentsList1.size()]));
//        return data;
//    }

}
