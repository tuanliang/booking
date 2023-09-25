package com.shiyi.business.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.shiyi.business.domain.BusBpmnInfo;
import com.shiyi.business.domain.BusServiceItem;
import com.shiyi.business.domain.vo.StartAuditVo;
import com.shiyi.business.mapper.BusBpmnInfoMapper;
import com.shiyi.business.mapper.BusServiceItemMapper;
import com.shiyi.common.utils.DateUtils;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.shiyi.business.mapper.BusCarPackageAuditMapper;
import com.shiyi.business.domain.BusCarPackageAudit;
import com.shiyi.business.service.IBusCarPackageAuditService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 套餐审核Service业务层处理
 * 
 * @author shiyi
 * @date 2023-09-24
 */
@Service
public class BusCarPackageAuditServiceImpl implements IBusCarPackageAuditService 
{
    @Autowired
    private BusCarPackageAuditMapper busCarPackageAuditMapper;
    @Autowired
    private BusBpmnInfoMapper bpmnInfoMapper;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private BusServiceItemMapper busServiceItemMapper;

    /**
     * 查询套餐审核
     * 
     * @param id 套餐审核主键
     * @return 套餐审核
     */
    @Override
    public BusCarPackageAudit selectBusCarPackageAuditById(Long id)
    {
        return busCarPackageAuditMapper.selectBusCarPackageAuditById(id);
    }

    /**
     * 查询套餐审核列表
     * 
     * @param busCarPackageAudit 套餐审核
     * @return 套餐审核
     */
    @Override
    public List<BusCarPackageAudit> selectBusCarPackageAuditList(BusCarPackageAudit busCarPackageAudit)
    {
        return busCarPackageAuditMapper.selectBusCarPackageAuditList(busCarPackageAudit);
    }

    /**
     * 新增套餐审核
     * 
     * @param busCarPackageAudit 套餐审核
     * @return 结果
     */
    @Override
    public int insertBusCarPackageAudit(BusCarPackageAudit busCarPackageAudit)
    {
        busCarPackageAudit.setCreateTime(DateUtils.getNowDate());
        return busCarPackageAuditMapper.insertBusCarPackageAudit(busCarPackageAudit);
    }

    /**
     * 修改套餐审核
     * 
     * @param busCarPackageAudit 套餐审核
     * @return 结果
     */
    @Override
    public int updateBusCarPackageAudit(BusCarPackageAudit busCarPackageAudit)
    {
        return busCarPackageAuditMapper.updateBusCarPackageAudit(busCarPackageAudit);
    }

    /**
     * 批量删除套餐审核
     * 
     * @param ids 需要删除的套餐审核主键
     * @return 结果
     */
    @Override
    public int deleteBusCarPackageAuditByIds(Long[] ids)
    {
        return busCarPackageAuditMapper.deleteBusCarPackageAuditByIds(ids);
    }

    /**
     * 删除套餐审核信息
     * 
     * @param id 套餐审核主键
     * @return 结果
     */
    @Override
    public int deleteBusCarPackageAuditById(Long id)
    {
        return busCarPackageAuditMapper.deleteBusCarPackageAuditById(id);
    }

    /**
     * 进度查看，高亮显示
     * @param id
     * @return
     */
    @Override
    public InputStream process(Long id) {
        // 1.判断id不为空，根据id查询套餐审核信息
        if(id==null){
            throw new RuntimeException("非法参数");
        }
        BusCarPackageAudit busCarPackageAudit = busCarPackageAuditMapper.selectBusCarPackageAuditById(id);
        if(busCarPackageAudit==null){
            throw new RuntimeException("非法参数");
        }
        // 2.查询bpmnInfo对象，根据类型进行查询
        BusBpmnInfo bpmnInfo = bpmnInfoMapper.queryByType(0);
        // 3.查询流程定义对象
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(bpmnInfo.getProcessDefinitionKey()).
                processDefinitionVersion(Integer.valueOf(String.valueOf(bpmnInfo.getVersion()))).singleResult();
        // 4.获取bpmnModel
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
        // 5.判断状态是否为审核中状态
        List<String> activeActivityIds = new ArrayList<>();
        if(BusCarPackageAudit.STATUS_IN_ROGRESS.equals(busCarPackageAudit.getStatus())){
            // 5.1.如果是审核中状态，获取到节点集合
            activeActivityIds = runtimeService.getActiveActivityIds(busCarPackageAudit.getInstanceId());

        }else{
            // 5.2.如果是非审核中状态，Collections.emptyList()
            activeActivityIds= Collections.emptyList();
        }
        // 6.工具类方法
        DefaultProcessDiagramGenerator generator = new DefaultProcessDiagramGenerator();
        return  generator.generateDiagram(bpmnModel,activeActivityIds,Collections.emptyList(),"宋体","宋体","宋体");

    }

    /**
     * 审核撤销
     * @param id
     */
    @Override
    @Transactional
    public void cancel(Long id) {
        // 1.id非空判断，根据id查询套餐审核信息
        if(id==null){
            throw new RuntimeException("非法参数");
        }
        BusCarPackageAudit busCarPackageAudit = busCarPackageAuditMapper.selectBusCarPackageAuditById(id);
        // 2.判断数据库是否为空，判断状态是否为审核中状态，
        if(busCarPackageAudit==null){
            throw new RuntimeException("非法参数");
        }
        if(!BusCarPackageAudit.STATUS_IN_ROGRESS.equals(busCarPackageAudit.getStatus())){
            throw new RuntimeException("状态必须要为审核中");
        }
        // 3.修改单项状态为初始化状态，修改套餐信息为审核拒绝状态
        busServiceItemMapper.changeStatus(busCarPackageAudit.getServiceItemId(),BusServiceItem.AUDITSTATUS_INIT);
        busCarPackageAuditMapper.changeStatus(id,BusCarPackageAudit.STATUS_CANCEL);
        // 4.根据流程实例id 删除流程实例信息
        runtimeService.deleteProcessInstance(busCarPackageAudit.getInstanceId(),"删除了");
    }


}
