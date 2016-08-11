/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.widget.model;
import com.huotu.hotcms.service.model.thymeleaf.foreach.Rename;
import lombok.Getter;
import lombok.Setter;

/**
 * 商品检索条件
 * Created by cwb on 2016/3/17.
 */
@Getter
@Setter
public class GoodsSearcher {

    /**
     * 商品分类主键id
     */
    @Rename("goodscatid")
    public Long goodsCatId;
    /**
     * 商品类型主键id
     */
    @Rename("goodstypeid")
    public Integer goodsTypeId;
    /**
     * 品牌主键id
     */
    @Rename("brandid")
    public Long brandId;
    /**
     * 起始销售价格(闭合，如果会员是登录状态则查询的应该是会员价)
     */
    @Rename("minprice")
    public Double minPrice;
    /**
     * 结束销售价格(闭合，如果会员是登录状态则查询的应该是会员价)
     */
    @Rename("maxprice")
    public Double maxPrice;
    /**
     * 会员主键id（用于查询会员价）
     */
    @Rename("userid")
    public Long userId;
    /**
     * 商品关键字（可模糊匹配）
     */
    @Rename("keyword")
    public String keyword;
    /**
     * 页码
     */
    @Rename("page")
    public Integer page;

    @Rename("pagenumber")
    public Integer pageNumber;

    /**
     * 每页显示的数量
     * */
    @Rename("pagesize")
    public Integer pagesize;
    /**
     * 排序(propertyName[,desc|asc]，排序方向默认asc，这个参数支持多个以达成多条件排序)
     * 举例：http://(……)&amp;sort=price,desc&amp;sort=salesCount,asc
     */
    @Rename("sort")
    public String[] sort;

    public GoodsSearcher init(GoodsSearcher goodsSearcher){
        if(goodsSearcher.getSort() == null){
            goodsSearcher.setSort(new String[]{"salesCount,desc"});
        }
        if(goodsSearcher.getPage() == null || goodsSearcher.getPage() <= 1){
            goodsSearcher.setPage(1);
        }
        return goodsSearcher;
    }

}
