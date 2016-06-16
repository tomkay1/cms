/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.entity;

import com.huotu.hotcms.service.common.AuditStatus;
import com.huotu.hotcms.service.common.MessageType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Created by chendeyu on 2016/5/6.
 */
@Entity
@Table(name = "cms_message")
@Getter
@Setter
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 商户ID
     */
    @Column(name = "customerId")
    private Integer customerId;

    /**
     * 留言用户
     */
    @ManyToOne
    @JoinColumn(name = "guestId")
    private Guest guestId;


    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private LocalDateTime createTime;

    /**
     * 标题
     */
    @Column(name = "title",length = 50)
    private String title;


    /**
     * 留言内容（富文本）
     */
    @Lob
    @Column(name = "content")
    private String content;

    /**
     * 管理员回复内容
     */
    @Column(name = "replyMessage")
    private String replyMessage;


    /**
     * 审核状态
     */
    @Column(name = "auditStatus")
    private AuditStatus auditStatus;


    /**
     * 评论所属模块
     */
    @Column(name = "messageType")
    private MessageType messageType;





}
