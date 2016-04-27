package com.huotu.hotcms.service.model.thymeleaf.foreach;

import lombok.Data;

/**
 * <p>
 *     foreach过程参数模型基类,为了兼容1.0版本，目前以下命名保持1.0版本的命名
 * </p>
 *
 * @since 1.2
 *
 * @author xhl
 */
@Data
public class BaseForeachParam {
    /**
     * 所属栏目id
     */
    @Rename("categoryid")
    public Long categoryId;

    /**
     * 页码
     */
    @Rename("pageno")
    public Integer pageNo;
    /**
     * 分页大小
     */
    @Rename("pagesize")
    public Integer pageSize;
    /**
     * 指定需要展示的页数
     */
    @Rename("pagenumber")
    public Integer pageNumber;

    /**
     * 获取列表时排除的主键Id(可排除多个，逗号分隔)
     */
    @Rename("excludeids")
    public String[] excludeIds;

    /**
     * 获取指定Id的列表(可指定多个，逗号分隔)
     */
    @Rename("specifyids")
    public String[] specifyIds;
}
