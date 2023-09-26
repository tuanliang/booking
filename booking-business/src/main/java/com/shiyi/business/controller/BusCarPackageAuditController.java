package com.shiyi.business.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.shiyi.business.domain.vo.AuditVo;
import com.shiyi.business.domain.vo.HistoryCommentInfo;
import com.shiyi.business.domain.vo.StartAuditVo;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.apache.commons.io.IOUtils;
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
import com.shiyi.business.domain.BusCarPackageAudit;
import com.shiyi.business.service.IBusCarPackageAuditService;
import com.shiyi.common.utils.poi.ExcelUtil;
import com.shiyi.common.core.page.TableDataInfo;

/**
 * 套餐审核Controller
 * 
 * @author shiyi
 * @date 2023-09-24
 */
@RestController
@RequestMapping("/business/audit")
public class BusCarPackageAuditController extends BaseController
{
    @Autowired
    private IBusCarPackageAuditService busCarPackageAuditService;

    /**
     * 查询套餐审核列表
     */
    @PreAuthorize("@ss.hasPermi('business:audit:list')")
    @GetMapping("/list")
    public TableDataInfo list(BusCarPackageAudit busCarPackageAudit)
    {
        startPage();
        List<BusCarPackageAudit> list = busCarPackageAuditService.selectBusCarPackageAuditList(busCarPackageAudit);
        return getDataTable(list);
    }

    /**
     * 导出套餐审核列表
     */
    @PreAuthorize("@ss.hasPermi('business:audit:export')")
    @Log(title = "套餐审核", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BusCarPackageAudit busCarPackageAudit)
    {
        List<BusCarPackageAudit> list = busCarPackageAuditService.selectBusCarPackageAuditList(busCarPackageAudit);
        ExcelUtil<BusCarPackageAudit> util = new ExcelUtil<BusCarPackageAudit>(BusCarPackageAudit.class);
        util.exportExcel(response, list, "套餐审核数据");
    }

    /**
     * 获取套餐审核详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:audit:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(busCarPackageAuditService.selectBusCarPackageAuditById(id));
    }

    /**
     * 新增套餐审核
     */
    @PreAuthorize("@ss.hasPermi('business:audit:add')")
    @Log(title = "套餐审核", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BusCarPackageAudit busCarPackageAudit)
    {
        return toAjax(busCarPackageAuditService.insertBusCarPackageAudit(busCarPackageAudit));
    }

    /**
     * 修改套餐审核
     */
    @PreAuthorize("@ss.hasPermi('business:audit:edit')")
    @Log(title = "套餐审核", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BusCarPackageAudit busCarPackageAudit)
    {
        return toAjax(busCarPackageAuditService.updateBusCarPackageAudit(busCarPackageAudit));
    }

    /**
     * 删除套餐审核
     */
    @PreAuthorize("@ss.hasPermi('business:audit:remove')")
    @Log(title = "套餐审核", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(busCarPackageAuditService.deleteBusCarPackageAuditByIds(ids));
    }

    /**
     * 高亮显示
     * @param id
     * @param response
     */
    @GetMapping("/process/{id}")
    public void process(@PathVariable Long id,HttpServletResponse response) throws IOException {
        InputStream inputStream = busCarPackageAuditService.process(id);
        IOUtils.copy(inputStream,response.getOutputStream());
    }

    /**
     * 审核撤销
     * @param id
     * @return
     */
    @PutMapping("/cancel/{id}")
    public AjaxResult cancel(@PathVariable Long id){
        busCarPackageAuditService.cancel(id);
        return AjaxResult.success();
    }

    @GetMapping("/todo")
    public TableDataInfo todo(){
        List<BusCarPackageAudit>list = busCarPackageAuditService.todoQuery();

        return getDataTable(list);
    }

    @PostMapping("/audit")
    public AjaxResult audit (@RequestBody AuditVo vo){
        busCarPackageAuditService.audit(vo);
        return AjaxResult.success();
    }

    @GetMapping("/history/{id}")
    public TableDataInfo history(@PathVariable Long id){
        List<HistoryCommentInfo> list = busCarPackageAuditService.historyQuery(id);
        return getDataTable(list);
    }

    @GetMapping("/done")
    public TableDataInfo doneQuery(){
        List<BusCarPackageAudit>list = busCarPackageAuditService.doneQuery();
        return getDataTable(list);
    }
}
