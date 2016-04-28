package com.huotu.hotcms.service.thymeleaf.service;

import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.thymeleaf.expression.DialectAttributeFactory;
import com.huotu.hotcms.service.thymeleaf.expression.VariableExpression;
import com.huotu.hotcms.service.thymeleaf.service.factory.GoodsPageableTagProcessor;
import com.huotu.hotcms.service.widget.model.GoodsPage;
import com.huotu.hotcms.service.widget.model.GoodsSearcher;
import com.huotu.hotcms.service.widget.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IProcessableElementTag;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2016/4/21.
 */
@Component
public class GoodsPageableTagProcessorService {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsPageableTagProcessor goodsPageableTagProcessor;
    @Autowired
    private DialectAttributeFactory dialectAttributeFactory;

    public Object invokeGoodsPageableService(IProcessableElementTag tag, ITemplateContext context) {
        int customerId = ((Site) VariableExpression.getVariable(context, "site")).getCustomerId();
        GoodsPage goodsPage = null;
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            GoodsSearcher goodsSearcher= (GoodsSearcher) goodsPageableTagProcessor.process(tag, request);
            goodsSearcher.init(goodsSearcher);
            goodsPage = goodsService.searchGoods(request, customerId, goodsSearcher);
            dialectAttributeFactory.setPageList(context,goodsPage);
//            putPageAttrsIntoModel(context, goodsPage);
        } catch (Exception e) {
           return null;
        }
        return goodsPage == null ? null : goodsPage.getGoodses();
    }

    //TODO 1.2 版本稳定后,把该代码移除
//    private void putPageAttrsIntoModel(ITemplateContext context, GoodsPage goodsPage) {
//        //分页标签处理
//        RequestModel requestModel = (RequestModel) VariableExpression.getVariable(context, "request");
//        int pageNo = goodsPage.getPageNo() + 1;
//        int totalPages = goodsPage.getTotalPages();
//        int pageBtnNum = totalPages > SysConstant.DEFAULT_PAGE_BUTTON_NUM ? SysConstant.DEFAULT_PAGE_BUTTON_NUM : totalPages;
//        int startPageNo = PageUtils.calculateStartPageNo(pageNo, pageBtnNum, totalPages);
//        List<Integer> pageNos = new ArrayList<>();
//        for (int i = 1; i <= pageBtnNum; i++) {
//            pageNos.add(startPageNo);
//            startPageNo++;
//        }
//        requestModel.setCurrentPage(pageNo);
//        requestModel.setTotalPages(totalPages);
//        //没有数据时前台页面显示 第1页/共1页
//        requestModel.setTotalRecords(goodsPage.getTotalRecords() == 0 ? 1 : goodsPage.getTotalRecords());
//        requestModel.setHasPrevPage(pageNo > 1);
//        requestModel.setHasNextPage(pageNo < totalPages);
//        requestModel.setPageNos(pageNos);
//    }


}
