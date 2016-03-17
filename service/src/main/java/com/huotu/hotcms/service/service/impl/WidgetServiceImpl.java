package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.WidgetMains;
import com.huotu.hotcms.service.entity.WidgetType;
import com.huotu.hotcms.service.repository.WidgetMainsRepository;
import com.huotu.hotcms.service.repository.WidgetTypeRepository;
import com.huotu.hotcms.service.service.WidgetService;
import com.huotu.hotcms.service.util.PageData;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
        Page<WidgetType> pageData = widgetTypeRepository.findAll(specification,new PageRequest(page - 1, pageSize));
        data=data.ConvertPageData(pageData,new WidgetType[pageData.getContent().size()]);
        return  data;
    }

    @Override
    public Boolean saveWidgetType(WidgetType widgetType) {
        widgetTypeRepository.save(widgetType);
        return true;
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
    public Boolean saveWidgetMains(WidgetType widgetType) {
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

}
