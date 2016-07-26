package com.huotu.hotcms.widget.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * 查询数据源接口
 * Created by lhx on 2016/7/26.
 */
@Controller("/_web")
public class CMSDataSourceController {

    /**
     * 根据parent 的contentType 来决定查询的数据类型{Link@CMSDataSourceService}
     * @param parentId 数据源id
     * @return json 返回当前parentId 的所有子级元素
     *          例如{code=200,message="Success",data=[...]},{code=403,message="fail",data=[]}
     */
    @RequestMapping(value = "/CMSDataSource/{parentId}")
    @ResponseBody
    public void findByAllChildren(Long parentId, Model model, HttpServletResponse response){

    }
}
