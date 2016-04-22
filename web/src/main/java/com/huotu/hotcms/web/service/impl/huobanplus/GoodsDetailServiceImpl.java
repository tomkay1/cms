package com.huotu.hotcms.web.service.impl.huobanplus;

import com.alibaba.fastjson.JSON;
import com.huotu.hotcms.service.common.ConfigInfo;
import com.huotu.hotcms.service.service.HttpService;
import com.huotu.hotcms.service.widget.model.GoodsDetail;
import com.huotu.hotcms.web.service.ConfigService;
import com.huotu.hotcms.web.service.GoodsDetailService;
import com.huotu.huobanplus.common.entity.Product;
import com.huotu.huobanplus.sdk.common.repository.GoodsRestRepository;
import com.huotu.huobanplus.sdk.common.repository.UserRestRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 商品详情
 * Created by chendeyu on 2016/4/8.
 */
@Profile("container")
@Service
public class GoodsDetailServiceImpl implements GoodsDetailService {

    @Autowired
    private HttpService httpService;

    private static final Log log = LogFactory.getLog(GoodsDetailServiceImpl.class);

    @Autowired
    private GoodsRestRepository goodsRestRepository;

    @Autowired
    private UserRestRepository userRestRepository;
    @Autowired
    private ConfigInfo configInfo;

    @Autowired
    private ConfigService configService;

    @Override
    public GoodsDetail getGoodsDetail(int goodsId, int userId) throws Exception {
        com.huotu.huobanplus.common.entity.Goods huobanGoods = null;
        try{
            huobanGoods = goodsRestRepository.getOneByPK(goodsId);
        }
        catch (IOException e) {
            System.out.println("接口服务不可用");
            log.error("接口服务不可用");
        }
        GoodsDetail mallGoods = new GoodsDetail();
//        if (userId!=0) {
//            try{
//                Double[] userPrice = userRestRepository.goodPrice(userId, goodsId);
//                mallGoods.setUserPrice(userPrice);
//            }
//            catch (IOException e) {
//                System.out.println("接口服务不可用");
//                log.error("接口服务不可用");
//            }
//        }
        huobanGoods.setSpec(JSON.parse(huobanGoods.getSpec()).toString());
        Set<Product> products = huobanGoods.getProducts();
        List<Double> userPrice = new ArrayList<>();
        if (products.size()!=0){//获取产品价格区间
            List<Double> priceList = new ArrayList<>();
            for (Product product : products ){
                priceList.add(product.getPrice());
            }
            userPrice.add(Collections.max(priceList));
            userPrice.add(Collections.min(priceList));
        }
        mallGoods.setUserPrice(userPrice);
        mallGoods.setId(Long.valueOf(goodsId));
        if(mallGoods.getBrandName()!=null){
            mallGoods.setBrandName(huobanGoods.getBrand().getBrandName());
        }
        mallGoods.setSpecDescriptions(huobanGoods.getSpecDescriptions());
        mallGoods.setCode(huobanGoods.getCode());
        mallGoods.setTitle(huobanGoods.getTitle());
        mallGoods.setBrief(huobanGoods.getBrief());
        mallGoods.setCost(huobanGoods.getCost());
        mallGoods.setDisabled(huobanGoods.isDisabled());
        mallGoods.setGoodsType(huobanGoods.getGoodsType());
        mallGoods.setIntro(huobanGoods.getIntro().replace("img src=\"/", "img src=\""+configService.getImgUri("")+"/"));
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

    @Override
    public String getGoodsWxUrl(HttpServletRequest request, Long goodsId) {
        String remotePort = "";
        if(request.getLocalPort()!=80){
            remotePort = "%3A"+request.getLocalPort() ;//获取端口号
        }

        String appid = configInfo.getAppid();
        String url = "https://open.weixin.qq.com/connect/qrconnect?appid="+appid+"&redirect_uri=http%3A%2F%2F"+request.getServerName()+remotePort+"%2Ffront%2Fbind%2Fcallback%2F&response_type=code&scope=snsapi_login&state=" + goodsId;
        return url;
    }


}
