package com.shiyi.business.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.shiyi.business.domain.BusBpmnInfo;
import com.shiyi.business.service.IBusBpmnInfoService;
import com.shiyi.common.utils.poi.ExcelUtil;
import com.shiyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 流程定义明细Controller
 * 
 * @author shiyi
 * @date 2023-09-22
 */
@RestController
@RequestMapping("/business/bpmnInfo")
public class BusBpmnInfoController extends BaseController
{
    @Autowired
    private IBusBpmnInfoService busBpmnInfoService;

    /**
     * 查询流程定义明细列表
     */
    @PreAuthorize("@ss.hasPermi('business:bpmnInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(BusBpmnInfo busBpmnInfo)
    {
        startPage();
        List<BusBpmnInfo> list = busBpmnInfoService.selectBusBpmnInfoList(busBpmnInfo);
        return getDataTable(list);
    }

    @PostMapping("/flow")
    public AjaxResult flow(Integer bpmnType, String info, String bpmnLabel, MultipartFile file) throws IOException {
        busBpmnInfoService.flow(bpmnType,bpmnLabel,file,info);
        return  AjaxResult.success();
    }


}
