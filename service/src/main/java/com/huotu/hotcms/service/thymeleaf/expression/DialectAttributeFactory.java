/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.expression;

import com.huotu.hotcms.service.common.EnumUtils;
import com.huotu.hotcms.service.common.RouteType;
import com.huotu.hotcms.service.common.SysConstant;
import com.huotu.hotcms.service.entity.BaseEntity;
import com.huotu.hotcms.service.entity.Route;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.Video;
import com.huotu.hotcms.service.model.thymeleaf.foreach.BaseForeachParam;
import com.huotu.hotcms.service.model.thymeleaf.foreach.Rename;
import com.huotu.hotcms.service.thymeleaf.model.PageModel;
import com.huotu.hotcms.service.thymeleaf.model.RequestModel;
import com.huotu.hotcms.service.util.HttpUtils;
import com.huotu.hotcms.service.util.PageUtils;
import com.huotu.hotcms.service.util.PatternMatchUtil;
import com.huotu.hotcms.service.util.StringUtil;
import com.huotu.hotcms.service.widget.model.BasePage;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IElementAttributes;
import org.thymeleaf.model.IProcessableElementTag;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwb on 2016/1/5.
 */
@Component
public class DialectAttributeFactory {
    private final String DEFAULT_PAGE_NO = "1";
    private final String DEFAULT_PAGE_SIZE = "12";
    private final String DEFAULT_PAGE_NUMBER = "3";

    private Field getField(String rename,Object object){
        Field[] fields=object.getClass().getFields();
        for(Field fields1:fields){
            Rename rename1=fields1.getAnnotation(Rename.class);
            if(rename1.value().equals(rename)){
                return  fields1;
            }
        }
        return null;
    }

    /**
     * 根据html 元素获得需要交给thymeleaf解析引擎的准备参数model
     * 每一个方言对应一个参数实体类
     *
     * @param elementTag html 中的dom元素 IProcessableElementTag
     * @param t
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> T getForeachParam(IProcessableElementTag elementTag, Class<T> t) throws Exception {
        T obj = t.newInstance();
        IElementAttributes elementAttributes = elementTag.getAttributes();
        List<AttributeName> attributeNames = elementAttributes.getAllAttributeNames();
        for (AttributeName attr : attributeNames) {
            String paramValue = elementAttributes.getValue(attr);
            Field field =getField(attr.getAttributeName(),obj);
            if(field!=null) {
                field.setAccessible(true);
                Class<?> classType = field.getType();
                if (classType == Integer.class) {
                    field.set(obj, Integer.parseInt(paramValue));
                } else if (classType == Long.class) {
                    field.set(obj, Long.parseLong(paramValue));
                } else if (classType == Double.class) {
                    field.set(obj, Double.parseDouble(paramValue));
                } else if (classType == String.class) {
                    field.set(obj, paramValue);
                } else if (classType == String[].class) {
                    field.set(obj, paramValue.split(","));
                } else if (classType == RouteType.class) {
                    field.set(obj, EnumUtils.valueOf(RouteType.class, Integer.parseInt(paramValue)));
                } else {
                    field.set(obj, paramValue);
                }
            }
        }
        return obj;
    }

    /**
     * 从界面有两种方式可以拿到数据，一种通过一个请求，一种通过自定义标签；
     * 此方法用于对比两种方法拿到的数据的优先级，即如果两种方法都拿到同一个属性的值，则优先选择请求中的;如果各不相同，则融合在一个对象中，并返回。
     *
     * @param elementTag         元素标签
     * @param httpServletRequest http请求
     * @param clz                对应的class类型
     * @param <T>                泛型，需要返回的对应的类型
     * @return 返回相应的类
     * @throws Exception 发生各种异常
     */
    public <T> T megerObject(IProcessableElementTag elementTag, HttpServletRequest httpServletRequest, Class<T> clz)
            throws Exception {

        T obj1 =this.getForeachParam(elementTag, clz);//自定义字段中读取到的
        T obj2 = HttpUtils.getRequestParam(httpServletRequest, clz); //从一起请求中读取到

        Field[] obj1Fields = obj1.getClass().getDeclaredFields();
        Field[] obj2Fields = obj2.getClass().getDeclaredFields();
        for (Field obj1Field : obj1Fields) {
            obj1Field.setAccessible(true);
            Object object1Value=obj1Field.get(obj1);
            for (Field obj2Field : obj2Fields) {
                obj2Field.setAccessible(true);
                if(obj1Field.getName().equals(obj2Field.getName())){
                    Object object2Value=obj2Field.get(obj2);
                    //对于同一个字段，如果从自定义字段中读取到的不为空，从请求中拿到的为空,则把前者的值赋给后者，并返回后者
                    if(object1Value!=null && object2Value==null){
                        obj2Field.set(obj2,object1Value);
                    }
                    break;
                }
            }
        }
        return obj2;
    }

    private <T> T getObjectByFiled(Object object,Field field,String paramValue) throws IllegalAccessException {
        T obj=(T)object;
        field.setAccessible(true);
        Class<?> classType = field.getType();
        if (classType == Integer.class) {
            field.set(obj, Integer.parseInt(paramValue));
        } else if (classType == Long.class) {
            field.set(obj, Long.parseLong(paramValue));
        } else if (classType == Double.class) {
            field.set(obj, Double.parseDouble(paramValue));
        } else if (classType == String.class) {
            field.set(obj, paramValue);
        } else if (classType == String[].class) {
            field.set(obj, paramValue.split(","));
        } else if (classType == RouteType.class) {
            field.set(obj, EnumUtils.valueOf(RouteType.class, Integer.parseInt(paramValue)));
        } else {
            field.set(obj, paramValue);
        }
        return obj;
    }

    /**
     * 根据请求环境获得解析需要的Model  xhl 1.2
     * @param request http 请求的request 上下文
     * @param object  目标对象
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> T getForeachParamByRequest(HttpServletRequest request,Object object) throws Exception {
        T resultObj=(T)object;
        Field[] fields=object.getClass().getFields();
        for(Field field:fields){
            String name=field.getName();
            String attrName = StringUtil.toUpperCase(name);
            Object obj=object.getClass().getMethod("get" + attrName).invoke(object);
            if(obj==null){
                if(name=="pageNo"){
                    if(StringUtils.isEmpty(request.getParameter("pageNo"))){
                        resultObj=getObjectByFiled(resultObj,field,DEFAULT_PAGE_NO);
                    }else{
                        int pageNo = Integer.parseInt(request.getParameter("pageNo"));
                        if(pageNo < 1) {
                            throw new Exception("页码小于1");
                        }else {
                            resultObj = getObjectByFiled(resultObj, field, request.getParameter("pageNo"));
                        }
                    }
                }
                if(name=="pageSize"){
                    if(StringUtils.isEmpty(request.getParameter("pageSize"))){
                        resultObj=getObjectByFiled(resultObj,field,DEFAULT_PAGE_SIZE);
                    }else{
                        resultObj=getObjectByFiled(resultObj,field,request.getParameter("pageSize"));
                    }
                }
                if(name=="pageNumber"){
                    resultObj=getObjectByFiled(resultObj,field,DEFAULT_PAGE_NUMBER);
                }
            }
        }
        return resultObj;
    }

    /**
     * 计算页码,分页扩展方言使用  xhl 1.2 代码重构
     *
     * @param currentPage
     * @param pageNumber
     * @param totalPages
     * @return
     */
    public int calculateStartPageNo(int currentPage, int pageNumber, int totalPages) {
        if(pageNumber == totalPages) {
            return 1;
        }
        return currentPage - pageNumber + 1 < 1 ? 1 : currentPage - pageNumber + 1;
    }

    /**
     * 设置分页相关信息对象
     * 设置后,页面上可以通过cms 扩展的内置对象request来获得相关分页信息,比如${request.hasPrevPage} xhl 1.2 代码重构
     *
     * @param baseForeachParam 分页循环基本实体类
     * @param Page 页面数据
     * @param context
     */
    public <T> void setPageList(BaseForeachParam baseForeachParam,Page<T> Page,ITemplateContext context) {
        if(baseForeachParam.getPageNumber()==null){
            baseForeachParam.setPageNumber(Integer.valueOf(DEFAULT_PAGE_NUMBER));
        }
        int currentPage = baseForeachParam.getPageNo();
        int totalPages = Page.getTotalPages();
        int pageNumber = baseForeachParam.getPageNumber() < totalPages ? baseForeachParam.getPageNumber() : totalPages;
        int startPage =calculateStartPageNo(currentPage,pageNumber,totalPages);
        List<PageModel> pages = new ArrayList<>();
        for(int i=1;i<=pageNumber;i++) {
            PageModel pageModel = new PageModel();
            pageModel.setPageNo(startPage);
            pageModel.setPageHref("?pageNo=" + startPage);
            pages.add(pageModel);
            startPage++;
        }
        RequestModel requestModel = (RequestModel)VariableExpression.getVariable(context,"request");
        requestModel.setPages(pages);
        requestModel.setHasNextPage(Page.hasNext());
        if(Page.hasNext()) {
            requestModel.setNextPageHref("?pageNo=" + (currentPage + 1));
        }
        if(Page.hasPrevious()) {
            requestModel.setPrevPageHref("?pageNo=" + (currentPage - 1));
        }
        requestModel.setHasPrevPage(Page.hasPrevious());
        requestModel.setCurrentPage(currentPage);
    }

    /**
     * 解析上下文(ITemplateContext) xhl 1.2 代码重构
     * @param context ITemplateContext 上下文
     * @param basePage 分页基类
     * @param pageBtnCount 分页显示页码数量
     */
    public void setPageList(ITemplateContext context, BasePage basePage,Integer pageBtnCount) {
        //分页标签处理
        RequestModel requestModel = (RequestModel) VariableExpression.getVariable(context, "request");
        int pageNo = basePage.getPageNo() + 1;
        int totalPages = basePage.getTotalPages();
        int pageBtnNum =pageBtnCount < totalPages ? pageBtnCount: totalPages;
        int startPageNo = PageUtils.calculateStartPageNo(pageNo, pageBtnNum, totalPages);
        List<Integer> pageNos = new ArrayList<>();
        for (int i = 1; i <= pageBtnNum; i++) {
            pageNos.add(startPageNo);
            startPageNo++;
        }
        requestModel.setCurrentPage(pageNo);
        requestModel.setTotalPages(totalPages);
        //没有数据时前台页面显示 第1页/共1页
        requestModel.setTotalRecords(basePage.getTotalRecords() == 0 ? 1 : basePage.getTotalRecords());
        requestModel.setHasPrevPage(pageNo > 1);
        requestModel.setHasNextPage(pageNo < totalPages);
        requestModel.setPageNos(pageNos);
    }

    /**
     * 根据http request 和路由规则来获得指定ID，一般该路由规则为通配规则
     * @param request
     * @param route 通配路由规则
     * @return
     */
    public Long getUrlId(HttpServletRequest request,Route route){
        String requestUrl = PatternMatchUtil.getUrl(request);
        Long id=PatternMatchUtil.getUrlIdByLongType(requestUrl, route.getRule());
        return id;
    }
}
