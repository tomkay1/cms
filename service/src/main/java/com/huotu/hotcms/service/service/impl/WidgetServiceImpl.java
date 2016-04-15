package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.common.ScopesType;
import com.huotu.hotcms.service.entity.Site;
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

/**
 * Created by chendeyu on 2016/3/17.
 */
@Service
public class WidgetServiceImpl implements WidgetService {
    private static Log log = LogFactory.getLog(WidgetServiceImpl.class);

    @Autowired
    private WidgetMainsRepository widgetMainsRepository;

    @Autowired
    private WidgetTypeRepository widgetTypeRepository;

    @Override
    public PageData<WidgetType> getWidgetTypePage(String name, int page, int pageSize) {
        PageData<WidgetType> data = new PageData<WidgetType>();
        Specification<Site> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(name)) {
                predicates.add(cb.like(root.get("name").as(String.class), "%" + name + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        Page<WidgetType> pageData = widgetTypeRepository.findAll(specification,new PageRequest(page - 1, pageSize, new Sort(Sort.Direction.DESC, "orderWeight")));
        data=data.ConvertPageData(pageData,new WidgetType[pageData.getContent().size()]);
        return  data;
    }

    @Override
    public PageData<WidgetMains> getWidgetMainsPage(String name, int page, int pageSize) {
        PageData<WidgetMains> data = new PageData<WidgetMains>();
        Specification<Site> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(name)) {
                predicates.add(cb.like(root.get("name").as(String.class), "%" + name + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        Page<WidgetMains> pageData = widgetMainsRepository.findAll(specification,new PageRequest(page - 1, pageSize, new Sort(Sort.Direction.DESC, "orderWeight")));
        data=data.ConvertPageData(pageData,new WidgetMains[pageData.getContent().size()]);
        return  data;
    }

    @Override
    public List<WidgetType> findAllWidgetType() {
        List<WidgetType> widgetTypes = widgetTypeRepository.findAll();
        return widgetTypes;
    }

    @Override
    public List<WidgetType> findAllWidgetTypeByNoScopesType(ScopesType scopesType) {
        List<WidgetType> widgetTypes=widgetTypeRepository.findAllByScopesTypeNot(scopesType.getCode());
        return widgetTypes;
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
        WidgetType widgetType = widgetTypeRepository.findOne(id);
        return widgetType;
    }

    @Override
    public WidgetMains findWidgetMainsById(Long id) {
        WidgetMains widgetMains = widgetMainsRepository.findOne(id);
        return widgetMains;
    }

    @Override
    public List<WidgetMains> findWidgetMainsByWidgetTypeId(Long id) {
        return widgetMainsRepository.findWidgetMainsByWidgetTypeId(id);
    }

    @Override
    public List<WidgetList> findList() {
        List<WidgetList> widgetList=new ArrayList<>();
        try{
            List<WidgetType> widgetTypeList=findAllWidgetType();
            for (WidgetType widgetType:widgetTypeList){
                WidgetList widget=new WidgetList();
                widget.setName(widgetType.getName());
                widget.setTypeId(widgetType.getId());
                widget.setWidgetMainsList(findWidgetMainsByWidgetTypeId(widgetType.getId()));
                widgetList.add(widget);
            }
        }catch (Exception ex){
            log.error(ex);
        }
        return widgetList;
    }

    @Override
    public List<WidgetList> findListByNoScopesType(ScopesType scopesType) {
        List<WidgetList> widgetList=new ArrayList<>();
        try{
            List<WidgetType> widgetTypeList=findAllWidgetTypeByNoScopesType(scopesType);
            for (WidgetType widgetType:widgetTypeList){
                WidgetList widget=new WidgetList();
                widget.setName(widgetType.getName());
                widget.setTypeId(widgetType.getId());
                widget.setWidgetMainsList(findWidgetMainsByWidgetTypeId(widgetType.getId()));
                widgetList.add(widget);
            }
        }catch (Exception ex){
            log.error(ex);
        }
        return widgetList;
    }
}
