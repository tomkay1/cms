/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.common.RouteType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.exception.BadCategoryInfoException;
import com.huotu.hotcms.service.model.CategoryTreeModel;
import com.huotu.hotcms.service.model.thymeleaf.foreach.CategoryForeachParam;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.service.CategoryService;
import com.huotu.hotcms.service.service.TemplateService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Log log = LogFactory.getLog(CategoryServiceImpl.class);

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getCategories(Site site) {
        return categoryRepository.findBySiteAndDeletedFalseOrderByOrderWeightDesc(site);
    }


    @Override
    @Transactional
    public Boolean deleteCategory(Category category) {
        category.setDeleted(true);
        categoryRepository.save(category);
        return true;
    }

    @Override
    public void delete(Category category) {
        categoryRepository.findByParent_Id(category.getId())
                .forEach(this::delete);
        categoryRepository.delete(category);
    }

    @Override
    public Category get(long id) {
        return categoryRepository.getOne(id);
    }

    @Override
    public Category get(Site site, String serial) {
        return categoryRepository.findBySerialAndSite(serial, site);
    }

    @Override
    public List<Category> getCategoryBySiteAndDeletedAndNameContainingOrderByOrderWeightDesc(Site site, Boolean deleted, String name) {
        List<Category> categories = null;
        if (StringUtils.isEmpty(name)) {
            categories = categoryRepository.findBySiteAndDeletedOrderByOrderWeightDesc(site, deleted);
        } else {
            categories = categoryRepository.findBySiteAndDeletedAndNameContainingOrderByOrderWeightDesc(site, deleted, name);
        }
        for (Category category : categories) {
            if (category != null) {
                category.setSite(null);
                category = setReleationEmpty(category);
            }
        }
        return categories;
    }

    private Category setReleationEmpty(Category category) {
        if (category != null) {
            Category parentCategory = category.getParent();
            if (parentCategory != null) {
                parentCategory.setSite(null);
//                parentCategory.setRoute(null);
                category.setParent(parentCategory);
                if (parentCategory.getParent() != null) {
                    return setReleationEmpty(parentCategory.getParent());
                } else {
                    return category;
                }
            }
        }
        return category;
    }


    @Override
    public List<Category> getSpecifyCategories(String[] specifyIds) {
        List<String> ids = Arrays.asList(specifyIds);
        List<Long> categoryIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
        Specification<Category> specification = (root, query, cb) -> {
            List<Predicate> predicates = categoryIds.stream().map(id -> cb.equal(root.get("id").as(Long.class), id)).collect(Collectors.toList());
            return cb.or(predicates.toArray(new Predicate[predicates.size()]));
        };
        return categoryRepository.findAll(specification, new Sort(Sort.Direction.DESC, "orderWeight"));
    }

    @Override
    public List<Category> findByRouteTypeAndParentId(CategoryForeachParam param) {
        Category parent = categoryRepository.findOne(param.getParentId());
        Sort sort = new Sort(Sort.Direction.DESC, "orderWeight");
        Specification<Category> specification = (root, query, cb) -> {
            List<Predicate> predicates;
            if (!StringUtils.isEmpty(param.getExcludeIds())) {
                List<String> ids = Arrays.asList(param.getExcludeIds());
                List<Long> categoryIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
                predicates = categoryIds.stream().map(id -> cb.notEqual(root.get("id").as(Long.class), id)).collect(Collectors.toList());
            } else {
                predicates = new ArrayList<>();
            }
            predicates.add(cb.equal(root.get("site").get("siteId").as(Long.class), param.getSiteId()));
            predicates.add(cb.equal(root.get("route").get("routeType").as(RouteType.class), param.getRouteType()));
            predicates.add(cb.equal(root.get("deleted").as(Boolean.class), false));
            predicates.add(cb.equal(root.get("parent").as(Category.class), parent));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        return categoryRepository.findAll(specification, new PageRequest(0, param.getSize(), sort)).getContent();
    }

    @Override
    public Iterable<Category> getCategoriesForContentType(Site site, ContentType contentType) {
        return categoryRepository.findBySiteAndContentTypeAndDeletedFalse(site, contentType);
    }

    @Override
    public Category getCategoryByNameAndParent(Site site, String name, Long parentCategoryId, ContentType type)
            throws BadCategoryInfoException {
        List<Category> list = categoryRepository.findBySiteAndDeletedAndNameContainingOrderByOrderWeightDesc(site
                , false, name);

        if (list.isEmpty()) {
            Category category = new Category();
            category.setContentType(type);
            category.setSite(site);
            category.setCreateTime(LocalDateTime.now());
            category.setName(name);
            if (parentCategoryId != null) {
                Category parent = categoryRepository.getOne(parentCategoryId);
                if (!parent.getSite().equals(site)) {
                    throw new BadCategoryInfoException(parent, (Category) null);
                }
                if (parent.getContentType() != type)
                    throw new BadCategoryInfoException(parent, type);
                category.setParent(parent);
            }
            init(category);
            return categoryRepository.save(category);
        }

        if (list.size() > 1 && log.isWarnEnabled()) {
            log.warn(site + " has more than 1 category for name:" + name);
        }

        Category category = list.get(0);

        if (parentCategoryId == null) {
            if (category.getParent() != null)
                throw new BadCategoryInfoException(category, (Category) null);
        } else {
            Category parent = categoryRepository.getOne(parentCategoryId);
            if (!parent.equals(category.getParent()))
                throw new BadCategoryInfoException(category, parent);
        }

        if (category.getContentType() != type)
            throw new BadCategoryInfoException(category, type);

        return category;
    }

    @Override
    public void init(Category category) {
        category.setSerial(UUID.randomUUID().toString().replace("-", ""));
    }

    @Override
    public Category copyTo(Category src, Site to) {
        Category newOne = src.copy();

        newOne.setSite(to);

        //检查to是否本来已经存在
        String append = "";
        while (categoryRepository.findBySerialAndSite(newOne.getSerial() + append, to) != null) {
            append = append + TemplateService.DuplicateAppend;
        }

        newOne.setSerial(newOne.getSerial() + append);

        if (src.getParent() != null) {
            Category parent = categoryRepository.findBySerialAndSite(src.getParent().getSerial(), to);
            newOne.setParent(parent);
        }
        return categoryRepository.save(newOne);
    }

    @Override
    public List<Category> getSubCategories(Long parenId, int size) {
        List<Category> categoryList = categoryRepository.findByParent_Id(parenId);
        int origionSize = categoryList.size();
        if (size > origionSize - 1) {
            size = origionSize;
        }
        return categoryList.subList(0, size);
    }

    @Override
    public List<Category> getSubCategories(Long parenId) {
        return categoryRepository.findByParent_Id(parenId);
    }


    @Override
    public List<CategoryTreeModel> ConvertCategoryTreeByCategory(List<Category> categories) {
        List<CategoryTreeModel> categoryTreeModels = new ArrayList<CategoryTreeModel>();
        if (categories != null && categories.size() > 0) {
            for (Category category : categories) {
                if (category != null) {
                    if (category.getParent() == null) {//父节点为空时
                        if (!isExistsCategory(categoryTreeModels, category)) {
                            categoryTreeModels.add(CategoryTreeModel.ConvertToCategoryTreeModel(category));
                        }
                    } else {//父节点不为空
                        Long index = getCategoryTreeId(categoryTreeModels, category.getParent());
                        if (index != null) {//父节点存在则把该节点加入到父节点下面
                            List<CategoryTreeModel> categoryTreeModels1 = insertCategoryTreeById(categoryTreeModels, index, CategoryTreeModel.ConvertToCategoryTreeModel(category));
                            if (categoryTreeModels1 != null) {
                                categoryTreeModels = categoryTreeModels1;
                            }
                        } else {
                            CategoryTreeModel categoryTreeModel = CategoryTreeModel.ConvertToCategoryTreeModel(category.getParent());
                            categoryTreeModel.addChildren(category);
                            categoryTreeModels.add(categoryTreeModel);
                        }
                    }
                }
            }
        }
        return categoryTreeModels;
    }

    private Boolean isExistsCategory(List<CategoryTreeModel> categoryTreeModelList, Category category) {
        if (categoryTreeModelList != null) {
            for (CategoryTreeModel categoryTreeModel : categoryTreeModelList) {
                if (categoryTreeModel != null && categoryTreeModel.getId().equals(category.getId())) {
                    return true;
                } else {
                    boolean Flag = isExistsCategory(categoryTreeModel.getChildren(), category);
                    if (Flag) {
                        return Flag;
                    } else {
                        continue;
                    }
                }
            }
        }
        return false;
    }

    private Long getCategoryTreeId(List<CategoryTreeModel> categoryTreeModelList, Category category) {
        if (categoryTreeModelList != null) {
            for (Integer i = 0; i < categoryTreeModelList.size(); i++) {
                CategoryTreeModel categoryTreeModel = categoryTreeModelList.get(i);
                if (categoryTreeModel != null && categoryTreeModel.getId().equals(category.getId())) {
                    return categoryTreeModel.getId();
                } else {
                    Long id = getCategoryTreeId(categoryTreeModel.getChildren(), category);
                    if (id != null) {
                        return id;
                    } else {
                        continue;
                    }
                }
            }
        }
        return null;
    }

    private List<CategoryTreeModel> insertCategoryTreeById(List<CategoryTreeModel> categoryTreeModelList, Long id, CategoryTreeModel categoryTree) {
        for (CategoryTreeModel categoryTreeModel : categoryTreeModelList) {
            if (categoryTreeModel != null && categoryTreeModel.getId().equals(id)) {
                categoryTreeModel.addChildren(categoryTree);
                return categoryTreeModelList;
            } else {
                if (categoryTreeModel.getChildren() != null) {
                    List<CategoryTreeModel> categoryTreeModelList1 = insertCategoryTreeById(categoryTreeModel.getChildren(), id, categoryTree);
                    if (categoryTreeModelList1 == null) {
                        continue;
                    } else {
                        categoryTreeModel.setChildren(categoryTreeModelList1);
                        return categoryTreeModelList;
                    }
                }
            }
        }
        return null;
    }


    @Override
    public List<Category> getCategoryList(Category parent) {
        return categoryRepository.findByParentOrderByOrderWeightDesc(parent);
    }

}
