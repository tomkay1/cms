package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.common.EnumUtils;
import com.huotu.hotcms.service.common.ModelType;
import com.huotu.hotcms.service.model.Contents;
import com.huotu.hotcms.service.repository.BaseEntityRepository;
import com.huotu.hotcms.service.service.CategoryService;
import com.huotu.hotcms.service.service.ContentsService;
import com.huotu.hotcms.service.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chendeyu on 2016/1/12.
 */
@Service
public class ContentsServiceImpl implements ContentsService {


    @Autowired
    BaseEntityRepository baseEntityRepository;

    @Autowired
    CategoryService categoryService;

    @Override
    public PageData<Contents> getPage(String title, Long siteId, Long category, int page, int pageSize) {
        PageData<Contents> data = null;
        List<Object[]> contentsList =new ArrayList<>();
        List<Object[]> contentsSize =new ArrayList<>();
        if(title==null){
            title="%"+""+"%";
        }
        else{
            title ="%"+title+"%";
        }
        if(category==-1){//当搜索条件只有站点时
            contentsList = baseEntityRepository.findAllContentsBySiteIdAndName(siteId, title,(page-1)*pageSize,pageSize);
            contentsSize =baseEntityRepository.findContentsSizeBySiteIdAndName(siteId, title);
        }
        else{
            String parentIds=categoryService.getCategoryParentIds(category);
            contentsList=baseEntityRepository.findAllContentsBySiteIdAndCategoryIdsAndName(siteId,parentIds,title,(page-1)*pageSize,pageSize);
            contentsSize =baseEntityRepository.findContentsSizeBySiteIdAndCategoryIdsAndName(siteId, parentIds, title);
        }
        List<Contents> contentsList1 = new ArrayList<>();
        for(Object[] o : contentsList) {
            Contents contents = new Contents();
            contents.setTitle((String)o[0]);
            contents.setDescription((String)o[1]);
            contents.setName((String) o[2]);
            contents.setId((Long) o[3]);
            Integer modelId=(Integer) o[4];
            if(modelId!=null) {
                contents.setModelId(modelId);
                contents.setModel(EnumUtils.valueOf(ModelType.class, modelId).toString().toLowerCase());
                contents.setModelname(EnumUtils.valueOf(ModelType.class, modelId).getValue().toString());
            }
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//定义格式，不显示毫秒
            String str = df.format(o[5]);
            contents.setCreateTime((String) str);
            contentsList1.add(contents);
        }
        int PageCount =0;
        if (contentsList.size()!=0){
            int yushu=contentsSize.size()%pageSize;
            if(yushu==0){
                PageCount =contentsSize.size()/pageSize;
            }
            else {
                PageCount =contentsSize.size()/pageSize+1;
            }
        }
        else{
             PageCount =0;
        }
        data = new PageData<Contents>();
        data.setPageCount(PageCount);//总页码
        data.setPageIndex(page);//页码
        data.setPageSize(contentsList.size());//页容量
        data.setTotal(contentsSize.size());//总数
        data.setRows((Contents[])contentsList1.toArray(new Contents[contentsList1.size()]));
        return data;
    }

}
