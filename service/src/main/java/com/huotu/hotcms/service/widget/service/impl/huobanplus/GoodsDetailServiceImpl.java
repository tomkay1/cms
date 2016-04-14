package com.huotu.hotcms.service.widget.service.impl.huobanplus;

import com.alibaba.fastjson.JSON;
import com.huotu.hotcms.service.service.HttpService;
import com.huotu.hotcms.service.util.ApiResult;
import com.huotu.hotcms.service.widget.model.GoodsDetail;
import com.huotu.hotcms.service.widget.service.GoodsDetailService;
import com.huotu.huobanplus.sdk.common.repository.GoodsRestRepository;
import com.huotu.huobanplus.sdk.common.repository.UserRestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;

/**
 * 商品详情
 * Created by chendeyu on 2016/4/8.
 */
@Profile("container")
@Service
public class GoodsDetailServiceImpl implements GoodsDetailService {

    @Autowired
    private HttpService httpService;

    @Autowired
    private GoodsRestRepository goodsRestRepository;

    @Autowired
    private UserRestRepository userRestRepository;
//
//    @Override
//    public Goods getGoodsDetail(int goodsId) throws Exception {
//
//        ApiResult<String> apiResult = invokeGoodsDetailProce(goodsId);
//        if(apiResult.getCode()!=200) {
//            throw new Exception(apiResult.getMsg());
//        }
//        return JSON.parseObject(apiResult.getData(),Goods.class);
//    }



    @Override
    public GoodsDetail getGoodsDetail(int goodsId,int userId) throws Exception {

        com.huotu.huobanplus.common.entity.Goods huobanGoods = goodsRestRepository.getOneByPK(goodsId);
        GoodsDetail mallGoods = new GoodsDetail();
        if (userId!=0) {
            Double[] userPrice = userRestRepository.goodPrice(userId, goodsId);
            mallGoods.setUserPrice(userPrice);
        }
        huobanGoods.setSpec(JSON.parse(huobanGoods.getSpec()).toString());
        mallGoods.setId(Long.valueOf(goodsId));
        mallGoods.setSpecDescriptions(huobanGoods.getSpecDescriptions());
        mallGoods.setCode(huobanGoods.getCode());
        mallGoods.setTitle(huobanGoods.getTitle());
        mallGoods.setBrief(huobanGoods.getBrief());
        mallGoods.setCost(huobanGoods.getCost());
        mallGoods.setDisabled(huobanGoods.isDisabled());
        mallGoods.setGoodsType(huobanGoods.getGoodsType());
        mallGoods.setIntro(huobanGoods.getIntro());
        mallGoods.setMarketable(huobanGoods.isMarketable());
        mallGoods.setMarketPrice(huobanGoods.getMarketPrice());
        mallGoods.setTypeId(huobanGoods.getTypeId());
        if (huobanGoods.getSmallPic() != null) {
            mallGoods.setSmallPic(huobanGoods.getSmallPic().getValue());
        }
        if (huobanGoods.getThumbnailPic() != null) {
            mallGoods.setThumbnailPic(huobanGoods.getThumbnailPic().getValue());
        }
        if (huobanGoods.getBigPic() != null) {
            mallGoods.setBigPic(huobanGoods.getBigPic().getValue());
        }
        mallGoods.setThumbnailPic(huobanGoods.getThumbnailPic().getValue());
        mallGoods.setSpec(huobanGoods.getSpec());
        mallGoods.setScenes(huobanGoods.getScenes());
        mallGoods.setCost(huobanGoods.getCost());
        mallGoods.setSalesCount(huobanGoods.getSalesCount());
        mallGoods.setPrice(huobanGoods.getPrice());
        mallGoods.setStock(huobanGoods.getStock());
        return mallGoods;
    }



    private ApiResult<String> invokeGoodsDetailProce(int goodsId) throws Exception{
        Map<String,Object> params = new TreeMap<>();
        params.put("goodsId",goodsId);
        return httpService.httpGet_prod("http", "api.open.huobanplus.com", null, "/goodses/"+goodsId, params);
    }

    private ApiResult<String> invokeGoodsPriceProce(int goodsId,String unionId) throws Exception{
        Map<String,Object> params = new TreeMap<>();
        params.put("goodsId",goodsId);
        return httpService.httpGet_prod("http", "api.open.huobanplus.com", null, "/users/"+unionId+"/goodsPrices", params);
    }

}
