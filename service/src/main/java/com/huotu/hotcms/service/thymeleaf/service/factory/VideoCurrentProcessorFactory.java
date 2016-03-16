/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.service.factory;

import com.huotu.hotcms.service.entity.Video;
import com.huotu.hotcms.service.model.thymeleaf.current.VideoCurrentParam;
import com.huotu.hotcms.service.service.VideoService;
import com.huotu.hotcms.web.service.BaseProcessorService;
import com.huotu.hotcms.web.thymeleaf.expression.DialectAttributeFactory;
import com.huotu.hotcms.web.thymeleaf.expression.VariableExpression;
import com.huotu.hotcms.web.util.PatternMatchUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.model.IProcessableElementTag;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator xhl 2016/1/18.
 */
public class VideoCurrentProcessorFactory extends BaseProcessorService {
    private static final Log log = LogFactory.getLog(VideoCurrentProcessorFactory.class);

    private static VideoCurrentProcessorFactory instance;

    private VideoCurrentProcessorFactory() {
    }

    public static VideoCurrentProcessorFactory getInstance() {
        if(instance == null) {
            instance = new VideoCurrentProcessorFactory();
        }
        return instance;
    }

    public Object resolveDataByAttr(IProcessableElementTag tab,ITemplateContext context){
        try{
            Video video=(Video) VariableExpression.getVariable(context, "video");
            if(video!=null){
                return video;
            }else{
                WebApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();

                VideoService videoService = (VideoService)applicationContext.getBean("videoServiceImpl");
                VideoCurrentParam videoCurrentParam = DialectAttributeFactory.getInstance().getForeachParam(tab, VideoCurrentParam.class);
                HttpServletRequest request = ((IWebContext)context).getRequest();
                String selvertUrl=PatternMatchUtil.getServletUrl(request);
                if(videoCurrentParam!=null){//根据当前请求的Uri来获得指定的ID
                    if(videoCurrentParam.getId()==null){
                        videoCurrentParam.setId(PatternMatchUtil.getUrlIdByLongType(selvertUrl,PatternMatchUtil.urlParamRegexp));
                    }
                }
                return  videoService.getVideoByParam(videoCurrentParam);
            }
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        return null;
    }
}
