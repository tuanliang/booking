package com.shiyi.common.utils;

import com.github.pagehelper.PageHelper;
import com.shiyi.common.core.page.PageDomain;
import com.shiyi.common.core.page.TableSupport;
import com.shiyi.common.utils.sql.SqlUtil;

/**
 * 分页工具类
 * 
 * @author ruoyi
 */
public class PageUtils extends PageHelper
{
    /**
     * 设置请求分页数据
     */
    public static void startPage()
    {
        // 获取请求中的参数，分装到PageDomain中
        PageDomain pageDomain = TableSupport.buildPageRequest();
        // 获取当前页，当前数据
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
        // 合理化分页属性，如果当前页小于0，显示第一页，如果大于0总页数就是总页数
        Boolean reasonable = pageDomain.getReasonable();
        // 开启分页
        PageHelper.startPage(pageNum, pageSize, orderBy).setReasonable(reasonable);
    }

    /**
     * 清理分页的线程变量
     */
    public static void clearPage()
    {
        PageHelper.clearPage();
    }
}
