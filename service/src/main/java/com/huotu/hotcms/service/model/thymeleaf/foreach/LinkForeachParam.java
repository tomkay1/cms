package com.huotu.hotcms.service.model.thymeleaf.foreach;


/**
 * Created by cwb on 2016/1/7.
 */
public class LinkForeachParam {


    /**
     * 所属栏目id
     */
    private Long categoryid;

    /**
     * 获取列表时排除的主键Id(可排除多个，逗号分隔)
     */
    private String[] excludeid;//TODO 上线前重命名为excludeids

    /**
     * 获取指定Id的列表(可指定多个，逗号分隔)
     */
    private String[] specifyids;

    /**
     * 取得列表大小
     */
    private Integer size;

    public Long getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(Long categoryid) {
        this.categoryid = categoryid;
    }

    public String[] getExcludeid() {
        return excludeid;
    }

    public void setExcludeid(String[] excludeid) {
        this.excludeid = excludeid;
    }

    public String[] getSpecifyids() {
        return specifyids;
    }

    public void setSpecifyids(String[] specifyids) {
        this.specifyids = specifyids;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
