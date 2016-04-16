
package com.huotu.hotcms.service.widget.service.impl.mock;

import com.alibaba.fastjson.JSON;
import com.huotu.hotcms.service.common.ConfigInfo;
import com.huotu.hotcms.service.widget.model.GoodsDetail;
import com.huotu.hotcms.service.widget.service.GoodsDetailService;
import com.huotu.huobanplus.sdk.common.repository.GoodsRestRepository;
import com.huotu.huobanplus.sdk.common.repository.UserRestRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 商品详情
 * Created by chendeyu on 2016/4/8.
 */
@Profile("!container")
@Service
public class GoodsDetailServiceImpl implements GoodsDetailService {


    private static final Log log = LogFactory.getLog(GoodsDetailServiceImpl.class);


    @Autowired
    private GoodsRestRepository goodsRestRepository;

    @Autowired
    private ConfigInfo configInfo;

    @Autowired
    private UserRestRepository userRestRepository;

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
        if (userId!=0) {
            try{
                Double[] userPrice = userRestRepository.goodPrice(userId, goodsId);
                mallGoods.setUserPrice(userPrice);
            }
            catch (IOException e) {
                System.out.println("接口服务不可用");
                log.error("接口服务不可用");
            }
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

    @Override
    public String getGoodsWxUrl(HttpServletRequest request,Long goodsId) {
        String remotePort = "";
        if(request.getLocalPort()!=80){
            remotePort = "%3A"+request.getLocalPort() ;//获取端口号
        }

        String appid = configInfo.getAppid();
        String url = "https://open.weixin.qq.com/connect/qrconnect?appid="+appid+"&redirect_uri=http%3A%2F%2F"+request.getServerName()+remotePort+"%2Ffront%2Fbind%2Fcallback%2F&response_type=code&scope=snsapi_login&state=" + goodsId;
        return url;
    }


}
