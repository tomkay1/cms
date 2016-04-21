package com.huotu.hotcms.service.util;

/**
 * 常用分页算法
 * Created by cwb on 2016/3/17.
 */
public class PageUtils {
    public static Integer pageSize = 20;

    /**
     * 计算分页button的起始显示页码
     * @param currentPage 当前页
     * @param pageBtnNum 页码button数量 奇数
     * @param totalPages 总页数
     * @return
     */
    public static int calculateStartPageNo(int currentPage, int pageBtnNum, int totalPages) {
        int middleBtn = pageBtnNum/2 + 1;
        if(currentPage<=middleBtn || pageBtnNum == totalPages) {
            return 1;
        }
        if((totalPages-currentPage)<middleBtn){
            return totalPages - pageBtnNum + 1;
        }
        return   currentPage + middleBtn - pageBtnNum;
    }
}
