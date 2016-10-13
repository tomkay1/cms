package com.huotu.hotcms.service.model;

import com.huotu.hotcms.service.entity.MallProductCategory;
import com.huotu.huobanplus.common.entity.Goods;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by lhx on 2016/10/12.
 */
@Getter
@Setter
public class MallProductCategoryModel extends MallProductCategory {
    private Page<Goods> mallGoodsPage;
    private List<Goods> mallGoodsList;

}
