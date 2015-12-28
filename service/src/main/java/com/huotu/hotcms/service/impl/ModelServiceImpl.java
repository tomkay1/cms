package com.huotu.hotcms.service.impl;

import com.huotu.hotcms.entity.DataModel;
import com.huotu.hotcms.repository.ModelRepository;
import com.huotu.hotcms.service.ModelService;
import com.huotu.hotcms.util.PageData;
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
 * Created by Administrator on 2015/12/25.
 */
@Service
public class ModelServiceImpl implements ModelService {
    @Autowired
    private ModelRepository modelRepository;

    @Override
    public PageData<DataModel> getPage(String name,int page,int pageSize) {
        PageData<DataModel> data = new PageData<DataModel>();
        Specification<DataModel> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(name)) {
                predicates.add(cb.like(root.get("name").as(String.class), "%" + name + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        Page<DataModel> pageData = modelRepository.findAll(specification,new PageRequest(page - 1, pageSize));
        data=data.ConvertPageData(pageData,new DataModel[pageData.getContent().size()]);
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

//    @Override
//    public Page<DataModel> findByName(String name, int page, int pageSize) {
//        return modelRepository.findByName(name,new PageRequest(page - 1, pageSize));
//    }

//    public Page<Orders> getPage(int pageNo, int pageSize, int supplierId, SearchCondition searchCondition) {
//        Specification<Order> specification = (root, criteriaQuery, criteriaBuilder) -> {
//            List<Predicate> predicates = new ArrayList<>();
//            predicates.add(criteriaBuilder.equal(root.get("supplierId").as(Integer.class), supplierId));
//            if (!StringUtils.isEmpty(searchCondition.getTxtOrderId())) {
//                predicates.add(criteriaBuilder.like(root.get("orderId").as(String.class), "%" + searchCondition.getTxtOrderId() + "%"));
//            }
//            if (!StringUtils.isEmpty(searchCondition.getTxtShipName())) {
//                predicates.add(criteriaBuilder.like(root.get("shipName").as(String.class), "%" + searchCondition.getTxtShipName() + "%"));
//            }
//            if (!StringUtils.isEmpty(searchCondition.getTxtShipMobile())) {
//                predicates.add(criteriaBuilder.like(root.get("shipMobile").as(String.class), "%" + searchCondition.getTxtShipMobile() + "%"));
//            }
//            if (searchCondition.getDdlPayStatus() != -1) {
//                predicates.add(criteriaBuilder.equal(root.get("payStatus").as(OrderEnum.PayStatus.class),
//                        EnumHelper.getEnumType(OrderEnum.PayStatus.class, searchCondition.getDdlPayStatus())));
//            }
//            if (searchCondition.getDdlShipStatus() != -1) {
//                predicates.add(criteriaBuilder.equal(root.get("shipStatus").as(OrderEnum.ShipStatus.class),
//                        EnumHelper.getEnumType(OrderEnum.ShipStatus.class, searchCondition.getDdlShipStatus())));
//            }
//            if (!StringUtils.isEmpty(searchCondition.getTxtBeginTime())) {
//                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createTime").as(LocalDateTime.class),
//                        LocalDateTime.parse(searchCondition.getTxtBeginTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
//            }
//            if (!StringUtil.isEmpty(searchCondition.getTxtEndTime())) {
//                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createTime").as(LocalDateTime.class),
//                        LocalDateTime.parse(searchCondition.getTxtEndTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
//            }
//            if (!StringUtil.isEmpty(searchCondition.getTxtBeginPaytime())) {
//                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("payTime").as(LocalDateTime.class),
//                        LocalDateTime.parse(searchCondition.getTxtBeginPaytime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
//            }
//            if (!StringUtil.isEmpty(searchCondition.getTxtEndPaytime())) {
//                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("payTime").as(LocalDateTime.class),
//                        LocalDateTime.parse(searchCondition.getTxtEndPaytime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
//            }
//            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
//        };
//        //排序
//        Sort sort;
//        Sort.Direction direction = searchCondition.getRaSortType() == 0 ? Sort.Direction.DESC : Sort.Direction.ASC;
//        switch (searchCondition.getDdlOrderByField()) {
//            case 1:
//                //按支付时间
//                sort = new Sort(direction, "payTime");
//                break;
//            case 2:
//                sort = new Sort(direction, "finalAmount");
//                break;
//            default:
//                sort = new Sort(direction, "createTime");
//                break;
//        }
//        return ordersRe.findAll(specification, new PageRequest(searchCondition.getPageNoStr()-1, pageSize, sort));
//    }
}
