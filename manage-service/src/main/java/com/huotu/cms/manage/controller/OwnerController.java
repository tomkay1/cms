/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller;

import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.service.repository.OwnerRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

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
public class OwnerController {

    private static final Log log = LogFactory.getLog(OwnerController.class);

    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/{id}/enable", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void toggleEnable(@PathVariable("id") long id) {
        Owner owner = ownerRepository.getOne(id);
        owner.setEnabled(!owner.isEnabled());
        log.info("Owner " + owner + " Toggle to " + owner.isEnabled());
    }

    @RequestMapping(value = "/{id}/customerId", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void changeCustomerId(@PathVariable("id") long id, @RequestBody int customerId) {
        Owner owner = ownerRepository.getOne(id);
        owner.setCustomerId(customerId);
        log.info("Owner " + owner + " Toggle to " + owner.getCustomerId());
    }

    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public String add(Owner data) {
        if (StringUtils.isEmpty(data.getLoginName()) && data.getCustomerId() == null)
            throw new IllegalArgumentException("用户名或者商户号必须选择一个");
        if (!StringUtils.isEmpty(data.getLoginName()) && StringUtils.isEmpty(data.getPassword()))
            throw new IllegalArgumentException("必须输入密码");
        data.setEnabled(true);
        if (!StringUtils.isEmpty(data.getLoginName()))
            data.setPassword(passwordEncoder.encode(data.getPassword()));
        ownerRepository.save(data);

        return "redirect:/manage/supper/owner";
    }

    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String index(Model model) {
        model.addAttribute("list", ownerRepository.findAll());
//        if (searchText != null) {
//            String toSearch = "%" + searchText + "%";
//
//            model.addAttribute("list", ownerRepository.findAll((root, query, cb) -> {
//                return cb.or(cb.equal(root.get("customerId"), toSearch), cb.like(root.get("loginName"), toSearch));
//            }, pageable));
//        } else
//            model.addAttribute("list", ownerRepository.findAll(pageable));

        return "/view/supper/owner.html";
    }

}
