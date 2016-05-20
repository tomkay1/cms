package com.huotu.hotcms.service.entity;

import com.huotu.hotcms.service.common.AuditStatus;
import com.huotu.hotcms.service.common.MessageType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by chendeyu on 2016/5/6.
 */
@Entity
@Table(name = "cms_message")
@Getter
@Setter
@Cacheable(value = false)
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
