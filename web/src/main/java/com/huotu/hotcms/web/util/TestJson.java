/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.web.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.huotu.hotcms.service.widget.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/21.
 */
public class TestJson {
    public static void main(String[] args) {
//        List<GoodsCategory> categories = new ArrayList<>();
//        GoodsCategory g1 = new GoodsCategory(1,"服装内衣",1,"http://www.cms.com/1.jpg");
//        GoodsCategory g11 = new GoodsCategory(2,"女装",2,"http://www.cms.com/2.jpg");
//        GoodsCategory g12 = new GoodsCategory(3,"男装",2,"http://www.cms.com/3.jpg");
//        g1.addChild(g11);
//        g1.addChild(g12);
//        GoodsCategory g2 = new GoodsCategory(4,"护肤彩妆",3,"http://www.cms.com/4.jpg");
//        categories.add(g1);
//        categories.add(g2);
//        JsonModel<List<GoodsCategory>> result = new JsonModel<>();
//        result.setElements(categories);
//        String jsonData = new Gson().toJson(categories);
//        System.out.println(jsonData);
//        Page<Goods> goodsPage = new Page<>(1,2,3);

        List<Goods> goodses = new ArrayList<>();
        Goods g1 = new Goods(1,"KROP女装抓毛长裙卫衣","2016-3-22 18:34:33",45,299,145,135,"http://www.cms.com/11.jpg");
        Link link1 = new Link();

        LinkHref self = new LinkHref();
        self.setHref("http://www.cms.com/11.jpg");
        link1.setSelf(self);
        Goods g2 = new Goods(2,"花花公子女士牛仔裤女秋经典简约修身潮流小脚女裤牛仔长裤子","2016-3-20 18:34:33",88,399,199,179,"http://www.cms.com/12.jpg");
        Goods g3 = new Goods(3,"春季新品牛仔裙女修身显瘦包臀开叉气质韩版中长连衣裙潮","2016-3-22 18:34:33",45,299,145,135,"http://www.cms.com/13.jpg");
        Goods g4 = new Goods(4,"BF风大码学生女宽松垮裤潮","2016-3-22 18:34:33",45,299,145,135,"http://www.cms.com/14.jpg");
        Goods g5 = new Goods(5,"代购 街头潮牌US风衣潮女原宿情侣宽松大码工装连帽外套","2016-3-22 18:34:33",45,299,145,135,"http://www.cms.com/15.jpg");
        Goods g6 = new Goods(6,"babygirl韩版新款街头时尚抽绳连帽纯色风衣休闲百搭外套学院风女","2016-3-22 18:34:33",45,299,145,135,"http://www.cms.com/16.jpg");
        Goods g7 = new Goods(7,"2016新款韩国街头夏季纯色t恤女韩版宽松大码简约百搭打底衫短袖","2016-3-22 18:34:33",45,299,145,135,"http://www.cms.com/17.jpg");
        Goods g8 = new Goods(8,"天天特价春秋季韩版百搭复古宽松古着街头男女夹克情侣装牛仔外套","2016-3-22 18:34:33",45,299,145,135,"http://www.cms.com/18.jpg");
        Goods g9 = new Goods(9,"uti尤缇2016新款春装欧美街头时尚拼接百搭长款风衣UC108125C125","2016-3-22 18:34:33",45,299,145,135,"http://www.cms.com/19.jpg");
        Goods g10 = new Goods(10,"达令女王2016春季新款百搭时尚韩范街头酷风BF皮衣棒球服外套女","2016-3-22 18:34:33",45,299,145,135,"http://www.cms.com/110.jpg");
        goodses.add(g3);
        goodses.add(g4);
        goodses.add(g5);
        goodses.add(g6);
        goodses.add(g7);
        goodses.add(g8);
        goodses.add(g9);
        goodses.add(g10);
        goodses.add(g1);
        goodses.add(g2);
        goodses.add(g3);
//        JsonModel<List<Goods>> result = new JsonModel<>();
//        Page page = new Page();
//        page.setSize(2);
//        page.setNumber(0);
//        page.setTotalElements(3);
//        page.setTotalPages(2);
//        result.setPage(page);
//        result.setElements(goodses);
        String jsonData = new Gson().toJson(goodses);
        System.out.println(jsonData);
    }
}
