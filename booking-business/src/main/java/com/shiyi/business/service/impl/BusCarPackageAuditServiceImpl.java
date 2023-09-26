package com.shiyi.business.service.impl;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import com.shiyi.business.domain.BusBpmnInfo;
import com.shiyi.business.domain.BusServiceItem;
import com.shiyi.business.domain.vo.AuditVo;
import com.shiyi.business.domain.vo.HistoryCommentInfo;
import com.shiyi.business.domain.vo.StartAuditVo;
import com.shiyi.business.mapper.BusBpmnInfoMapper;
import com.shiyi.business.mapper.BusServiceItemMapper;
import com.shiyi.common.utils.DateUtils;
import com.shiyi.common.utils.PageUtils;
import com.shiyi.common.utils.SecurityUtils;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
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
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;

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

    /**
     * 我的代办
     * @return
     */
    @Override
    public List<BusCarPackageAudit> todoQuery() {
            // 1.查询bpmnInfo对象
            BusBpmnInfo bpmnInfo = bpmnInfoMapper.queryByType(0);
            // 2.查询流程定义对象
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().
                    processDefinitionKey(bpmnInfo.getProcessDefinitionKey()).
                    processDefinitionVersion(Integer.valueOf(String.valueOf(bpmnInfo.getVersion()))).singleResult();
            // 3.根据流程定义id，和当前登录用户id，查询任务集合
            List<Task> list = taskService.createTaskQuery().processDefinitionId(processDefinition.getId()).
                    taskAssignee(SecurityUtils.getUserId().toString()).list();
            if(list==null||list.size()==0){
                return Collections.emptyList();
            }
            // 4.定义List<Long>ids = new ArrayList() bus_car_package_audit 表id
            ArrayList<Long> ids = new ArrayList<>();
            // 5.遍历任务集合
            for (Task task : list) {
                // 5.1根据任务去获取流程实例id
                String processInstanceId = task.getProcessInstanceId();
                // 5.2根据流程实例id，查询流程实例对象
                ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
                // 5.3根据流程实例对象获取businessKey
                String businessKey = processInstance.getBusinessKey();
                // 5.4把businessKey放到集合中
                ids.add(Long.valueOf(businessKey));
            }
            PageUtils.startPage();// 开启分页
            // 6根据ids 集合查询bus_car_package_audit表数据
            return busCarPackageAuditMapper.selectBusCarPackageAuditByIds(ids);
    }

    /**
     * 审批操作
     * @param vo
     */
    @Override
    @Transactional
    public void audit(AuditVo vo) {
        // 1.vo非空判断
        if(vo==null){
            throw new RuntimeException("非法操作");
        }
        // 2.vo获取id，擦汗寻数据库中数据
        if(vo.getId()==null){
            throw new RuntimeException("非法操作");
        }
        BusCarPackageAudit busCarPackageAudit = busCarPackageAuditMapper.selectBusCarPackageAuditById(vo.getId());
        if(busCarPackageAudit==null){
            throw new RuntimeException("非法操作");
        }
        // 3.判断状态是否为审核中状态
        if(!BusCarPackageAudit.STATUS_IN_ROGRESS.equals(busCarPackageAudit.getStatus())){
            throw new RuntimeException("状态必须为审核中");
        }
        // 4.判断前台传递过来是审核通过还是审核拒绝，设置message
        String message = vo.getAuditStatus()?"审核通过："+SecurityUtils.getUsername()+"-批注："+vo.getInfo():
                "审核拒绝："+SecurityUtils.getUsername()+"-批注："+vo.getInfo();

        // 5.获取当前任务节点
        Task task = taskService.createTaskQuery().processInstanceId(busCarPackageAudit.getInstanceId()).singleResult();

        // 6.添加批注信息
        taskService.addComment(task.getId(),task.getProcessInstanceId(),message);
        // 7.设置流程变量Map<String,Object>
        Map<String,Object>map = new HashMap<>();
        map.put("shopOwner",vo.getAuditStatus());
        // 8.完成任务complete(tesk)
        taskService.complete(task.getId(),map);
        // 9.判断是审核通过还是审核拒绝
        if(vo.getAuditStatus()){
            // 9.1如果通过，判断当前任务节点是否为空，如果为空，修改单项和busCarPackageAudit为拒绝
            Task nextTask = taskService.createTaskQuery().processInstanceId(busCarPackageAudit.getInstanceId()).singleResult();
            if(nextTask==null){
                busServiceItemMapper.changeStatus(busCarPackageAudit.getServiceItemId(),BusServiceItem.AUDITSTATUS_APPROVED);
                busCarPackageAuditMapper.changeStatus(vo.getId(),BusCarPackageAudit.STATUS_PASS);
            }
        }else{
            // 9.2如果是审核拒绝，修改但是状态为重新调整，修改busCarPackageAudit为审核拒绝
            busServiceItemMapper.changeStatus(busCarPackageAudit.getServiceItemId(),BusServiceItem.AUDITSTATUS_REPLY);
            busCarPackageAuditMapper.changeStatus(vo.getId(),BusCarPackageAudit.STATUS_REJECT);
        }
    }

    /**
     * 查看审批历史
     * @param id
     * @return
     */
    @Override
    public List<HistoryCommentInfo> historyQuery(Long id) {
        // 1.判断id是否为空
        if(id==null){
            throw new RuntimeException("非法操作");
        }
        // 2.根据流程实例id，查询历史表中是否有数据
        List<HistoricActivityInstance> historyList = historyService.createHistoricActivityInstanceQuery().
                processInstanceId(id.toString()).
                activityType("userTask").finished().list();
        if(historyList==null||historyList.size()==0){
            throw new RuntimeException("非法操作");
        }
        // 3.创建一个HistoryComment list
        List<HistoryCommentInfo>list =new ArrayList<>();
        // 4.遍历历史表集合
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (HistoricActivityInstance historicActivityInstance : historyList) {
            // 4.1 把数据封装到HistoryCommentInfo对象中
            HistoryCommentInfo info = new HistoryCommentInfo();
            info.setStartTime(sdf.format(historicActivityInstance.getStartTime()));
            info.setEndTime(sdf.format(historicActivityInstance.getEndTime()));
            info.setDurationInMillis((historicActivityInstance.getDurationInMillis()/1000)+"s");
            info.setTaskName(historicActivityInstance.getActivityName());
            // 4.2 查看匹配信息
            List<Comment> taskComments = taskService.getTaskComments(historicActivityInstance.getTaskId());
            if(taskComments!=null&&taskComments.size()>0){
            // 4.3 把批注设置到HistoryComment中
                String message = taskComments.get(0).getFullMessage();
                info.setComment(message);
            }
            list.add(info);
        }

        //
        return list;
    }

    /**
     * 我的已办
     * @return
     */
    @Override
    public List<BusCarPackageAudit> doneQuery() {
        // 1.查询bpmnInfo对象
        BusBpmnInfo bpmnInfo = bpmnInfoMapper.queryByType(0);
        // 2.查询流程定义对象
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().
                processDefinitionKey(bpmnInfo.getProcessDefinitionKey()).
                processDefinitionVersion(Integer.valueOf(String.valueOf(bpmnInfo.getVersion()))).singleResult();
        // 3.根据流程定义id，和当前登录用户id，查询任务集合
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery().
                processDefinitionId(processDefinition.getId()).
                taskAssignee(SecurityUtils.getUserId().toString()).activityType("userTask").list();

        if(list==null||list.size()==0){
            return Collections.emptyList();
        }
        // 4.定义List<Long>ids = new ArrayList() bus_car_package_audit 表id
        ArrayList<Long> ids = new ArrayList<>();
        // 5.遍历任务集合
        for (HistoricActivityInstance task : list) {
            // 5.1根据任务去获取流程实例id
            String processInstanceId = task.getProcessInstanceId();
            // 5.2根据流程实例id，查询流程实例对象
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();

            // 5.3根据流程实例对象获取businessKey
            String businessKey = historicProcessInstance.getBusinessKey();
            // 5.4把businessKey放到集合中
            ids.add(Long.valueOf(businessKey));
        }
        PageUtils.startPage();// 开启分页
        // 6根据ids 集合查询bus_car_package_audit表数据
        return busCarPackageAuditMapper.selectDoneBusCarPackageAuditByIds(ids);
    }


}
