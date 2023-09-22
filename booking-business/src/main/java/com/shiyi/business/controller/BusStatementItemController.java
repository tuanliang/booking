package com.shiyi.business.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.shiyi.business.domain.vo.BusStatementItemVo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.shiyi.common.annotation.Log;
import com.shiyi.common.core.controller.BaseController;
import com.shiyi.common.core.domain.AjaxResult;
import com.shiyi.common.enums.BusinessType;
import com.shiyi.business.domain.BusStatementItem;
import com.shiyi.business.service.IBusStatementItemService;
import com.shiyi.common.utils.poi.ExcelUtil;
import com.shiyi.common.core.page.TableDataInfo;

/**
 * 结算单明细Controller
 * 
 * @author shiyi
 * @date 2023-09-22
 */
@RestController
@RequestMapping("/appointment/statementItem")
public class BusStatementItemController extends BaseController
{
    @Autowired
    private IBusStatementItemService busStatementItemService;

    /**
     * 查询结算单明细列表
     */
    @PreAuthorize("@ss.hasPermi('appointment:statementItem:list')")
    @GetMapping("/list")
    public TableDataInfo list(BusStatementItem busStatementItem)
    {
        startPage();
        List<BusStatementItem> list = busStatementItemService.selectBusStatementItemList(busStatementItem);
        return getDataTable(list);
    }



    /**
     * 新增结算单明细
     */
    @PreAuthorize("@ss.hasPermi('appointment:statementItem:add')")
    @Log(title = "结算单明细", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BusStatementItemVo busStatementItem)
    {
        return toAjax(busStatementItemService.insertBusStatementItem(busStatementItem));
    }

}
