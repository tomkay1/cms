/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.common.ScopesType;
import com.huotu.hotcms.service.entity.WidgetMains;
import com.huotu.hotcms.service.entity.WidgetType;
import com.huotu.hotcms.service.model.WidgetList;
import com.huotu.hotcms.service.repository.WidgetMainsRepository;
import com.huotu.hotcms.service.repository.WidgetTypeRepository;
import com.huotu.hotcms.service.service.WidgetService;
import com.huotu.hotcms.service.util.PageData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class WidgetServiceImpl implements WidgetService {
    private static Log log = LogFactory.getLog(WidgetServiceImpl.class);

    @Autowired
    private WidgetMainsRepository widgetMainsRepository;

    @Autowired
    private WidgetTypeRepository widgetTypeRepository;

    @Override
    public PageData<WidgetType> getWidgetTypePage(String name, int page, int pageSize) {
        PageData<WidgetType> data = new PageData<>();
        Specification<WidgetType> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(name)) {
                predicates.add(cb.like(root.get("name").as(String.class), "%" + name + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        Page<WidgetType> pageData = widgetTypeRepository.findAll(specification, new PageRequest(page - 1, pageSize, new Sort(Sort.Direction.DESC, "orderWeight")));
        data = data.ConvertPageData(pageData, new WidgetType[pageData.getContent().size()]);
        return data;
    }

    @Override
    public PageData<WidgetMains> getWidgetMainsPage(String name, int page, int pageSize, Long widgetTypeId) {
        PageData<WidgetMains> data = new PageData<>();
        Specification<WidgetMains> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(name)) {
                predicates.add(cb.like(root.get("name").as(String.class), "%" + name + "%"));
            }
            if (widgetTypeId != 0) {
                predicates.add(cb.equal(root.get("widgetType").get("id").as(Long.class), widgetTypeId));
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        Page<WidgetMains> pageData = widgetMainsRepository.findAll(specification, new PageRequest(page - 1, pageSize, new Sort(Sort.Direction.DESC, "orderWeight")));
        data = data.ConvertPageData(pageData, new WidgetMains[pageData.getContent().size()]);
        return data;
    }

    @Override
    public List<WidgetType> findAllWidgetType() {
        return widgetTypeRepository.findAll();
    }

    @Override
    public List<WidgetType> findAllWidgetTypeByNoScopesType(ScopesType scopesType) {
        return widgetTypeRepository.findAllByScopesTypeNot(scopesType.getCode());
    }

    @Override
    public void delWidgetType(Long id) {
        widgetTypeRepository.delete(id);
    }

    @Override
    public void delWidgetMains(Long id) {
        widgetMainsRepository.delete(id);

    }

    @Override
    public Boolean saveWidgetMains(WidgetMains widgetMains) {
        widgetMainsRepository.save(widgetMains);
        return true;
    }

    @Override
    public Boolean saveWidgetType(WidgetType widgetType) {
        widgetTypeRepository.save(widgetType);
        return true;
    }

    @Override
    public WidgetType findWidgetTypeById(Long id) {
        return widgetTypeRepository.findOne(id);
    }

    @Override
    public WidgetMains findWidgetMainsById(Long id) {
        return widgetMainsRepository.findOne(id);
    }

    @Override
    public List<WidgetMains> findWidgetMainsByWidgetTypeId(Long id) {
        return widgetMainsRepository.findWidgetMainsByWidgetTypeId(id);
    }

    @Override
    public List<WidgetList> findList() {
        List<WidgetList> widgetList = new ArrayList<>();
        try {
            List<WidgetType> widgetTypeList = findAllWidgetType();
            typeType(widgetList, widgetTypeList);
        } catch (Exception ex) {
            log.error(ex);
        }
        return widgetList;
    }

    private void typeType(List<WidgetList> widgetList, List<WidgetType> widgetTypeList) {
        for (WidgetType widgetType : widgetTypeList) {
            WidgetList widget = new WidgetList();
            widget.setName(widgetType.getName());
            widget.setTypeId(widgetType.getId());
            widget.setWidgetMainsList(findWidgetMainsByWidgetTypeId(widgetType.getId()));
            widgetList.add(widget);
        }
    }

    @Override
    public List<WidgetList> findListByNoScopesType(ScopesType scopesType) {
        List<WidgetList> widgetList = new ArrayList<>();
        try {
            List<WidgetType> widgetTypeList = findAllWidgetTypeByNoScopesType(scopesType);
            typeType(widgetList, widgetTypeList);
        } catch (Exception ex) {
            log.error(ex);
        }
        return widgetList;
    }
}
