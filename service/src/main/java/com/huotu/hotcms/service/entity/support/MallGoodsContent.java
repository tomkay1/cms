package com.huotu.hotcms.service.entity.support;

import com.huotu.hotcms.service.entity.AbstractContent;
import com.huotu.huobanplus.common.entity.Goods;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by lhx on 2016/10/13.
 */
@Getter
@Setter
public class MallGoodsContent extends AbstractContent {

    /**
     * 商城商品
     */
    private Goods mallGoods;

    @Override
    public AbstractContent copy() {
        return null;
    }
}
