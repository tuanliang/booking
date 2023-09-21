package com.shiyi.business.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.shiyi.business.domain.vo.BusAppointmentVo;
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
import com.shiyi.business.domain.BusAppointment;
import com.shiyi.business.service.IBusAppointmentService;
import com.shiyi.common.utils.poi.ExcelUtil;
import com.shiyi.common.core.page.TableDataInfo;

/**
 * 预约信息Controller
 * 
 * @author shiyi
 * @date 2023-09-20
 */
@RestController
@RequestMapping("/appointment/appointment")
public class BusAppointmentController extends BaseController
{
    @Autowired
    private IBusAppointmentService busAppointmentService;

    /**
     * 查询预约信息列表
     */
    @PreAuthorize("@ss.hasPermi('appointment:appointment:list')")
    @GetMapping("/list")
    public TableDataInfo list(BusAppointment busAppointment)
    {
        startPage();
        List<BusAppointment> list = busAppointmentService.selectBusAppointmentList(busAppointment);
        return getDataTable(list);
    }

    /**
     * 导出预约信息列表
     */
    @PreAuthorize("@ss.hasPermi('appointment:appointment:export')")
    @Log(title = "预约信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BusAppointment busAppointment)
    {
        List<BusAppointment> list = busAppointmentService.selectBusAppointmentList(busAppointment);
        ExcelUtil<BusAppointment> util = new ExcelUtil<BusAppointment>(BusAppointment.class);
        util.exportExcel(response, list, "预约信息数据");
    }

    /**
     * 获取预约信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('appointment:appointment:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(busAppointmentService.selectBusAppointmentById(id));
    }

    /**
     * 新增预约信息
     */
    @PreAuthorize("@ss.hasPermi('appointment:appointment:add')")
    @Log(title = "预约信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BusAppointmentVo busAppointmentVo)
    {
        return toAjax(busAppointmentService.insertBusAppointment(busAppointmentVo));
    }

    /**
     * 修改预约信息
     */
    @PreAuthorize("@ss.hasPermi('appointment:appointment:edit')")
    @Log(title = "预约信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BusAppointmentVo busAppointment)
    {
        return toAjax(busAppointmentService.updateBusAppointment(busAppointment));
    }

    /**
     * 删除预约信息
     */
    @PreAuthorize("@ss.hasPermi('appointment:appointment:remove')")
    @Log(title = "预约信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id)
    {
        return toAjax(busAppointmentService.deleteBusAppointmentById(id));
    }

    /**
     * 用户到店
     */
    @Log(title = "预约信息", businessType = BusinessType.UPDATE)
    @PutMapping("/arral/{id}")
    public AjaxResult arral(@PathVariable Long id)
    {
        return toAjax(busAppointmentService.arralShop(id));
    }

    /**
     * 预约取消
     */
    @Log(title = "预约取消", businessType = BusinessType.UPDATE)
    @PutMapping("/cancel/{id}")
    public AjaxResult cancel(@PathVariable Long id)
    {
        return toAjax(busAppointmentService.cancel(id));
    }
}
