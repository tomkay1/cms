/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller;

import com.huotu.cms.manage.controller.support.CRUDController;
import com.huotu.cms.manage.exception.RedirectException;
import com.huotu.hotcms.service.entity.login.Login;
import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.service.repository.OwnerRepository;
import com.huotu.hotcms.service.service.LoginService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 这里是超管管理商户的控制器
 * <ul>
 * <li>以某个商户身份进入管理</li>
 * <li>展示所有商户</li>
 * <li>添加一个与商城关联的商户 或者无关联的商户</li>
 * <li>暂停某一个商户,同时关闭相关站点</li>
 * <li>恢复某一个商户,同时恢复相关站点</li>
 * </ul>
 *
 * @author CJ
 */
@Controller
@RequestMapping("/manage/supper/owner")
public class OwnerController extends CRUDController<Owner, Long, Void, Void> {

    private static final Log log = LogFactory.getLog(OwnerController.class);

    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "/{id}/customerId", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void changeCustomerId(@PathVariable("id") long id, @RequestBody int customerId) {
        Owner owner = ownerRepository.getOne(id);
        owner.setCustomerId(customerId);
        log.info("Owner " + owner + " Toggle to " + owner.getCustomerId());
    }

    @Override
    protected String indexViewName() {
        return "/view/supper/owner.html";
    }

    @Override
    protected void prepareUpdate(Login login, Owner entity, Owner data, Void extra, RedirectAttributes attributes, HttpServletRequest request)
            throws RedirectException {

    }

    @Override
    protected String openViewName() {
        return null;
    }

    @Override
    protected Owner preparePersist(HttpServletRequest request, Login login, Owner data, Void extra
            , RedirectAttributes attributes) throws RedirectException {
        if (!StringUtils.isEmpty(data.getLoginName()) && data.getLoginName().equalsIgnoreCase("root"))
            throw new IllegalArgumentException("用户名不可用。");
        if (StringUtils.isEmpty(data.getLoginName()) && data.getCustomerId() == null)
            throw new IllegalArgumentException("用户名或者商户号必须选择一个");
        if (!StringUtils.isEmpty(data.getLoginName()) && StringUtils.isEmpty(data.getPassword()))
            throw new IllegalArgumentException("必须输入密码");
        data.setEnabled(true);
        if (!StringUtils.isEmpty(data.getPassword()))
            loginService.changePassword(data, data.getPassword());

        return data;
    }

}
