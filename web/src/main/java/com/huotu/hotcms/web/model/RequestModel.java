package com.huotu.hotcms.web.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.beans.BeanMap;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * Created by Administrator xhl 2016/1/7.
 */
@Getter
@Setter
public class RequestModel{
    private String hosts;

    private String url;

    private HttpServletRequest request;

    public String get(String param){
        if(request!=null){
            return request.getParameter(param.toString());
        }
        return null;
    }

////    Set<String> query;
//    public String getParam(String name){
////        this.beanMap
//    }
//
//    private BeanMap beanMap;
//
//    @Override
//    public BeanMap newInstance(Object o) {
//        return beanMap;
////        Object o=new Object(){ };
////       return super(o);
////        return o;
////        return null;
//    }
//
//    @Override
//    public Class getPropertyType(String s) {
//        return null;
//    }
//
//    @Override
//    public Object get(Object o, Object o1) {
//        return null;
//    }
//
//    @Override
//    public Object put(Object o, Object o1, Object o2) {
//        return null;
//    }
//
//    @Override
//    public Set keySet() {
//        return null;
//    }
}
