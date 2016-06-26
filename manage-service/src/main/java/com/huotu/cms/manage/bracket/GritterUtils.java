/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.bracket;

import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Bracket系列UI的辅助工具
 *
 * @author CJ
 */
public class GritterUtils {

    private static final String PrimaryKey = "_primary";
    private static final String InfoKey = "_info";
    private static final String DangerKey = "_danger";
    private static final String WarningKey = "_warning";
    private static final String SuccessKey = "_success";


    private static void AddNormalMessage(Model model, String key, String message) {
        model.addAttribute(key, message);
    }

    private static void AddFlashMessage(RedirectAttributes model, String key, String message) {
        model.addFlashAttribute(key, message);
    }

    /**
     * 以闪存的方式,可以用于页面跳转之后的信息展示
     *
     * @param message 消息
     * @param model   MVC Model
     * @see #AddPrimary(String, Model)
     */
    public static void AddFlashPrimary(String message, RedirectAttributes model) {
        AddFlashMessage(model, PrimaryKey, message);
    }

    /**
     * 以闪存的方式,可以用于页面跳转之后的信息展示
     *
     * @param message 消息
     * @param model   MVC Model
     * @see #AddInfo(String, Model)
     */
    public static void AddFlashInfo(String message, RedirectAttributes model) {
        AddFlashMessage(model, InfoKey, message);
    }

    /**
     * 以闪存的方式,可以用于页面跳转之后的信息展示
     *
     * @param message 消息
     * @param model   MVC Model
     * @see #AddDanger(String, Model)
     */
    public static void AddFlashDanger(String message, RedirectAttributes model) {
        AddFlashMessage(model, DangerKey, message);
    }

    /**
     * 以闪存的方式,可以用于页面跳转之后的信息展示
     *
     * @param message 消息
     * @param model   MVC Model
     * @see #AddSuccess(String, Model)
     */
    public static void AddFlashSuccess(String message, RedirectAttributes model) {
        AddFlashMessage(model, SuccessKey, message);
    }

    /**
     * 以闪存的方式,可以用于页面跳转之后的信息展示
     *
     * @param message 消息
     * @param model   MVC Model
     * @see #AddWarning(String, Model)
     */
    public static void AddFlashWarning(String message, RedirectAttributes model) {
        AddFlashMessage(model, WarningKey, message);
    }

    /**
     * 弹出gritter的提示框,以growl-primary风格展示
     *
     * @param message 消息
     * @param model   MVC Model
     */
    public static void AddPrimary(String message, Model model) {
        AddNormalMessage(model, PrimaryKey, message);
    }

    /**
     * 弹出gritter的提示框,以growl-info风格展示
     *
     * @param message 消息
     * @param model   MVC Model
     */
    public static void AddInfo(String message, Model model) {
        AddNormalMessage(model, InfoKey, message);
    }

    /**
     * 弹出gritter的提示框,以growl-danger风格展示
     *
     * @param message 消息
     * @param model   MVC Model
     */
    public static void AddDanger(String message, Model model) {
        AddNormalMessage(model, DangerKey, message);
    }

    /**
     * 弹出gritter的提示框,以growl-success风格展示
     *
     * @param message 消息
     * @param model   MVC Model
     */
    public static void AddSuccess(String message, Model model) {
        AddNormalMessage(model, SuccessKey, message);
    }

    /**
     * 弹出gritter的提示框,以growl-warning风格展示
     *
     * @param message 消息
     * @param model   MVC Model
     */
    public static void AddWarning(String message, Model model) {
        AddNormalMessage(model, WarningKey, message);
    }

}
