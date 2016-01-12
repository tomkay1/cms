package com.huotu.hotcms.service.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by chendeyu on 2016/1/9.
 */
@Getter
@Setter
public class Contents {
    /**
     * 内容id
     */
    private Long id;
    /**
     * 内容标题
     */
    private String title;
    /**
     * 内容描述
     */
    private String description;
    /**
     * 栏目名称
     */
    private String name;
    /**
     * 内容模型类型名称
     */
    private String modelname;
    /**
     * 内容模型类型ID
     */
    private Integer modelId;
    /**
     * 内容模型（用来跳转正确的controller）
     */
    private String model;
    /**
     * 创建时间
     */
    private String createTime;

}
