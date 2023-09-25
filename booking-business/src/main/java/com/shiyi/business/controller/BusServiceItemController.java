package com.shiyi.business.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.shiyi.business.domain.vo.BusAppointmentVo;
import com.shiyi.business.domain.vo.BusServiceItemVo;
import com.shiyi.business.domain.vo.StartAuditVo;
import com.shiyi.business.qo.ServiceItemAuditInfo;
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
import com.shiyi.business.domain.BusServiceItem;
import com.shiyi.business.service.IBusServiceItemService;
import com.shiyi.common.utils.poi.ExcelUtil;
import com.shiyi.common.core.page.TableDataInfo;

/**
 * 服务项Controller
 * 
 * @author shiyi
 * @date 2023-09-21
 */
@RestController
@RequestMapping("/appointment/serviceItem")
public class BusServiceItemController extends BaseController
{
    @Autowired
    private IBusServiceItemService busServiceItemService;

    /**
     * 查询服务项列表
     */
    @PreAuthorize("@ss.hasPermi('appointment:serviceItem:list')")
    @GetMapping("/list")
    public TableDataInfo list(BusServiceItem busServiceItem)
    {
        startPage();
        List<BusServiceItem> list = busServiceItemService.selectBusServiceItemList(busServiceItem);
        return getDataTable(list);
    }

    /**
     * 导出服务项列表
     */
    @PreAuthorize("@ss.hasPermi('appointment:serviceItem:export')")
    @Log(title = "服务项", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BusServiceItem busServiceItem)
    {
        List<BusServiceItem> list = busServiceItemService.selectBusServiceItemList(busServiceItem);
        ExcelUtil<BusServiceItem> util = new ExcelUtil<BusServiceItem>(BusServiceItem.class);
        util.exportExcel(response, list, "服务项数据");
    }

    /**
     * 获取服务项详细信息
     */
    @PreAuthorize("@ss.hasPermi('appointment:serviceItem:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(busServiceItemService.selectBusServiceItemById(id));
    }

    /**
     * 新增服务项
     */
    @PreAuthorize("@ss.hasPermi('appointment:serviceItem:add')")
    @Log(title = "服务项", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BusServiceItemVo busServiceItem)
    {
        return toAjax(busServiceItemService.insertBusServiceItem(busServiceItem));
    }

    /**
     * 修改服务项
     */
    @PreAuthorize("@ss.hasPermi('appointment:serviceItem:edit')")
    @Log(title = "服务项", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BusServiceItemVo busServiceItem)
    {
        return toAjax(busServiceItemService.updateBusServiceItem(busServiceItem));
    }

    /**
     * 删除服务项
     */
    @PreAuthorize("@ss.hasPermi('appointment:serviceItem:remove')")
    @Log(title = "服务项", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(busServiceItemService.deleteBusServiceItemByIds(ids));
    }

    /***
     * 上架
     * @param id
     * @return
     */
    @Log(title = "服务项上架", businessType = BusinessType.UPDATE)
    @PutMapping("/saleOn/{id}")
    public AjaxResult saleOn(@PathVariable Long id){
        return toAjax(busServiceItemService.saleOn(id));
    }

    /***
     * 下架
     * @param id
     * @return
     */
    @Log(title = "服务项下架", businessType = BusinessType.UPDATE)
    @PutMapping("/saleOff/{id}")
    public AjaxResult saleOff(@PathVariable Long id){
        return toAjax(busServiceItemService.saleOff(id));
    }

    /**
     * 发起审核
     * @param id
     * @return
     */
    @GetMapping("/audit/{id}")
    public AjaxResult auditPage(@PathVariable Long id){
        ServiceItemAuditInfo info = busServiceItemService.auditPage(id);
        return AjaxResult.success(info);
    }

    /**
     * 发起审核确定
     * @param vo
     * @return
     */
    @Log(title = "套餐审核", businessType = BusinessType.UPDATE)
    @PutMapping("/audit")
    public AjaxResult audit(@RequestBody StartAuditVo vo){
        busServiceItemService.audit(vo);
        return AjaxResult.success();
    }
}
