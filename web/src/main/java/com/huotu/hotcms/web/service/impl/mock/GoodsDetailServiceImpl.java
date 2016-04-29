
package com.huotu.hotcms.web.service.impl.mock;

import com.alibaba.fastjson.JSON;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.thymeleaf.service.SiteResolveService;
import com.huotu.hotcms.service.widget.model.GoodsDetail;
import com.huotu.hotcms.web.service.ConfigService;
import com.huotu.hotcms.web.service.GoodsDetailService;
import com.huotu.huobanplus.common.entity.Merchant;
import com.huotu.huobanplus.common.entity.Product;
import com.huotu.huobanplus.common.entity.support.SpecDescription;
import com.huotu.huobanplus.sdk.common.repository.GoodsRestRepository;
import com.huotu.huobanplus.sdk.common.repository.MerchantRestRepository;
import com.huotu.huobanplus.sdk.common.repository.ProductRestRepository;
import com.huotu.huobanplus.sdk.mall.service.MallInfoService;
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
import java.util.Map;

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
    private ProductRestRepository productRestRepository;

    @Autowired
    private SiteResolveService siteResolveService;

    @Autowired
    private MerchantRestRepository merchantRestRepository;

    @Autowired
    private MallInfoService mallInfoService;

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
        List<Product> huobanProductList = productRestRepository.findByGoods(huobanGoods);//获取goods里的product
        List<com.huotu.hotcms.service.model.Bind.Product> productList = new ArrayList();
        List<Double> priceList = new ArrayList();
        for(Product huobanProduct : huobanProductList){
            com.huotu.hotcms.service.model.Bind.Product product = new com.huotu.hotcms.service.model.Bind.Product();
            product.setId(huobanProduct.getId());
            product.setSpec(huobanProduct.getSpec());
            product.setCode(huobanProduct.getCode());
            product.setCostPrice(huobanProduct.getCostPrice());
            product.setMarketPrice(huobanProduct.getMarketPrice());
            product.setName(huobanProduct.getName());
            product.setStock(huobanProduct.getStock());
            product.setPrice(huobanProduct.getPrice());
            priceList.add(huobanProduct.getPrice());
            productList.add(product);
        }
        mallGoods.setProducts(productList);
        mallGoods.setId(Long.valueOf(goodsId));
        if(mallGoods.getBrandName()!=null){
            mallGoods.setBrandName(huobanGoods.getBrand().getBrandName());
        }
        mallGoods.setSpec(JSON.parseObject(huobanGoods.getSpec(), Map.class));
        mallGoods.setSpecDescriptions(huobanGoods.getSpecDescriptions());//将规格对应的图片路径做处理
        for(List<SpecDescription> specDescriptionList : mallGoods.getSpecDescriptions().values() ){
            for(SpecDescription specDescription : specDescriptionList){
                if(specDescription.getGoodsImageIds().length!=0){
                String[] arry = new String[1];
                arry[0] = configService.getImgUri("") + specDescription.getGoodsImageIds()[0];
                specDescription.setGoodsImageIds(arry);
            }
            }
        }
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
        mallGoods.setScenes(huobanGoods.getScenes());
        mallGoods.setCost(huobanGoods.getCost());
        mallGoods.setSalesCount(huobanGoods.getSalesCount());
        mallGoods.setPrice(huobanGoods.getPrice());
        mallGoods.setMaxPrice(Collections.max(priceList));//设置产品最大价格
        mallGoods.setMinPrice(Collections.min(priceList));//设置产品最小价格
        mallGoods.setStock(huobanGoods.getStock());
        return mallGoods;
    }

    @Override
    public String getGoodsWxUrl(HttpServletRequest request,Long goodsId) {
//        String remotePort = "";
//        if(request.getLocalPort()!=80){
//            remotePort = "%3A"+request.getLocalPort() ;//获取端口号
//        }
//
//        String appid = configInfo.getAppid();
//        String url = "https://open.weixin.qq.com/connect/qrconnect?appid="+appid+"&redirect_uri=http%3A%2F%2F"+request.getServerName()+remotePort+"%2Fbind%2Fcallback%2F&response_type=code&scope=snsapi_login&state=" + goodsId;
//        return url;

        return null;
    }

    @Override
    public String getPersonDetailUrl(HttpServletRequest request){
        Site site = null;
        String domain = "";
        try {
            site = siteResolveService.getCurrentSite(request);
            Merchant merchant = merchantRestRepository.getOneByPK(site.getCustomerId());
            domain = merchant.getSubDomain();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = configService.getCustomerUri(domain)+"/UserCenter/Index.aspx?customerid="+site.getCustomerId();
        return  url;
    }

    @Override
    public String getSubscribeUrl(HttpServletRequest request) {
        Site site = null;
        try {
            site = siteResolveService.getCurrentSite(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = null;
        try {
            url = mallInfoService.subscribeUrl(site.getCustomerId());
        } catch (IOException e) {
            System.out.println("接口服务不可用");
            log.error("接口服务不可用");
        }
        return  url;
    }


}
