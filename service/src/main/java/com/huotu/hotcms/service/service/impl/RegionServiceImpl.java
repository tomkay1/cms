
/*版权所有
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.entity.Region;
import com.huotu.hotcms.service.repository.RegionRepository;
import com.huotu.hotcms.service.service.RegionService;
import com.huotu.hotcms.service.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwb on 2015/12/24.
 */
@Service
public class RegionServiceImpl implements RegionService {

    @Autowired
    private RegionRepository regionRepository;

    @Override
    public Region getRegion(String area) {
        return regionRepository.findByRegionCodeIgnoreCase(area);
    }

    @Override
    public PageData<Region> getPage(String name,int page,int pageSize) {
        PageData<Region> data = new PageData<Region>();
        Specification<Region> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(name)) {
                predicates.add(cb.like(root.get("regionName").as(String.class), "%" + name + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        Page<Region> pageData = regionRepository.findAll(specification,new PageRequest(page - 1, pageSize));
        data=data.ConvertPageData(pageData,new Region[pageData.getContent().size()]);
//        if (pageData != null) {
//            data = new PageData<DataModel>();
//            data.setPageCount(pageData.getTotalPages());
//            data.setPageIndex(pageData.getNumber());
//            data.setPageSize(pageData.getSize());
//            data.setTotal(pageData.getTotalElements());
//            data.setRows((DataModel[])pageData.getContent().toArray(new DataModel[pageData.getContent().size()]));
//        }
        return  data;
    }
}
