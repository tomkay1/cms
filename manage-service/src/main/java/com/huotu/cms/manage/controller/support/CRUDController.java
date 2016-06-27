/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller.support;

import com.huotu.cms.manage.bracket.GritterUtils;
import com.huotu.hotcms.service.entity.login.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.Serializable;

/**
 * @param <T>  资源类型
 * @param <ID> 资源主键类型
 * @param <PD> 持久时共同请求的额外数据
 * @param <MD> 修改时共同请求的额外数据
 * @author CJ
 */
public abstract class CRUDController<T, ID extends Serializable, PD, MD> {

    @Autowired
    private JpaRepository<T, ID> jpaRepository;

    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public String add(@AuthenticationPrincipal Login login, T data, PD extra, RedirectAttributes attributes) {
        try {
            data = preparePersist(login, data, extra, attributes);

            jpaRepository.save(data);

            GritterUtils.AddFlashSuccess("成功添加", attributes);
        } catch (Exception ex) {
            // TODO 有些异常我们应该另外处理
            GritterUtils.AddFlashDanger(ex.getMessage(), attributes);
        }
        return redirectIndexViewName();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void doDelete(@AuthenticationPrincipal Login login, @PathVariable("id") ID id) {
        prepareRemove(login, id);
        jpaRepository.delete(id);
    }

    // 用这种方式,必然是需要302回主界面
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
    @Transactional
    public String delete(@AuthenticationPrincipal Login login, @PathVariable("id") ID id) {
        doDelete(login, id);
        return redirectIndexViewName();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String open(@AuthenticationPrincipal Login login, @PathVariable("id") ID id, Model model) {
        T data = jpaRepository.getOne(id);
        model.addAttribute("object", data);
        prepareOpen(login, data, model);
        return openViewName();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    @Transactional
    public String save(@AuthenticationPrincipal Login login, @PathVariable("id") ID id, T data, MD extra, RedirectAttributes attributes) {
        T entity = jpaRepository.getOne(id);
        try {
            prepareSave(login, entity, data, extra, attributes);
            jpaRepository.save(entity);
            GritterUtils.AddFlashSuccess("保存成功", attributes);
        } catch (Exception ex) {
            // TODO 有些异常我们应该另外处理
            GritterUtils.AddFlashDanger(ex.getMessage(), attributes);
        }
        return redirectIndexViewName();
    }

    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String index(Model model) {
        model.addAttribute("list", jpaRepository.findAll());
//        if (searchText != null) {
//            String toSearch = "%" + searchText + "%";
//
//            model.addAttribute("list", ownerRepository.findAll((root, query, cb) -> {
//                return cb.or(cb.equal(root.get("customerId"), toSearch), cb.like(root.get("loginName"), toSearch));
//            }, pageable));
//        } else
//            model.addAttribute("list", ownerRepository.findAll(pageable));

        return indexViewName();
    }

    /**
     * @return index的视图名称
     */
    protected abstract String indexViewName();

    /**
     * 在新增持久一个资源之前
     *
     * @param login      当前操作者的身份
     * @param data       来自用户的数据
     * @param extra      额外数据
     * @param attributes 空间
     * @return 提交到持久层的数据
     */
    protected abstract T preparePersist(Login login, T data, PD extra, RedirectAttributes attributes);

    /**
     * @return 重定向到索引界面的视图名称
     */
    @SuppressWarnings("WeakerAccess")
    protected String redirectIndexViewName() {
        return "redirect:" + AnnotationUtils.findAnnotation(getClass(), RequestMapping.class).value()[0];
    }

    /**
     * 在删除某一个资源之前
     *
     * @param login 当前操作者的身份
     * @param id    主键
     */
    @SuppressWarnings("WeakerAccess")
    protected void prepareRemove(Login login, ID id) {

    }

    /**
     * 准备展示一个资源
     *
     * @param login 当前操作者的身份
     * @param data  资源
     * @param model 模型
     */
    @SuppressWarnings("WeakerAccess")
    protected void prepareOpen(Login login, T data, Model model) {

    }

    /**
     * 保存之前
     *
     * @param login      当前操作者的身份
     * @param entity     数据
     * @param data       用户请求的数据
     * @param extra      额外数据
     * @param attributes 空间
     */
    protected abstract void prepareSave(Login login, T entity, T data, MD extra, RedirectAttributes attributes);

    /**
     * @return 打开一个资源的视图名称
     */
    protected abstract String openViewName();
}
