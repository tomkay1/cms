package com.huotu.hotcms.service.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by chendeyu on 2016/5/6.
 */
@Entity
@Table(name = "cms_guest" ,uniqueConstraints = @UniqueConstraint(columnNames ={"username"}))
@Getter
@Setter
@Cacheable(value = false)
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 账号名称
     */
    @Column(length = 50)
    private String username;

    /**
     * 账号密码
     */
    private String password;

    /**
     * 用户昵称
     */
    @Column(name = "name")
    private String name;

    /**
     * ip地址
     */
    @Column(name = "ip")
    private String ip;


    /**
     * 手机号
     */
    @Column(name = "phoneNo")
    private String phoneNo;


    /**
     * 邮箱
     */
    @Column(name = "mail")
    private String mail;

    /**
     * 联系地址
     */
    @Column(name = "address")
    private String address;

    /**
     * qq号
     */
    @Column(name = "qq")
    private String qq;



    /**
     * 账号冻结情况
     */
    private boolean accountNonLocked = false;




    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private LocalDateTime createTime;




}
