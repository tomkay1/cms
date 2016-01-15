/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.model.thymeleaf;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by cwb on 2016/1/15.
 */
@Getter
@Setter
public class VideoForeachParam {
    /**
     * 所属栏目Id
     */
    private Long categoryid;

    /**
     * 父栏目id(与所属栏目id存在性互斥)
     */
    private Long parentcid;

    /**
     * 获取列表时排除的主键Id(可排除多个，逗号分隔)
     */
    private String[] excludeid;//TODO 上线前重命名为excludeids

    /**
     * 获取指定Id的列表(可指定多个，逗号分隔)
     */
    private String[] specifyids;

    /**
     * 页码
     */
    private Integer pageno;

    /**
     * 列表大小
     */
    private Integer pagesize;

    /**
     * 指定需要展示的页数
     */
    private Integer pagenumber;
}
