package com.shiyi.business.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import com.shiyi.common.utils.StringUtils;
import com.shiyi.common.utils.file.FileUploadUtils;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.shiyi.business.mapper.BusBpmnInfoMapper;
import com.shiyi.business.domain.BusBpmnInfo;
import com.shiyi.business.service.IBusBpmnInfoService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 流程定义明细Service业务层处理
 * 
 * @author shiyi
 * @date 2023-09-22
 */
@Service
public class BusBpmnInfoServiceImpl implements IBusBpmnInfoService 
{
    @Autowired
    private BusBpmnInfoMapper busBpmnInfoMapper;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;

    /**
     * 查询流程定义明细
     * 
     * @param id 流程定义明细主键
     * @return 流程定义明细
     */
    @Override
    public BusBpmnInfo selectBusBpmnInfoById(Long id)
    {
        return busBpmnInfoMapper.selectBusBpmnInfoById(id);
    }

    /**
     * 查询流程定义明细列表
     * 
     * @param busBpmnInfo 流程定义明细
     * @return 流程定义明细
     */
    @Override
    public List<BusBpmnInfo> selectBusBpmnInfoList(BusBpmnInfo busBpmnInfo)
    {
        return busBpmnInfoMapper.selectBusBpmnInfoList(busBpmnInfo);
    }

    /**
     * 新增流程定义明细
     * 
     * @param busBpmnInfo 流程定义明细
     * @return 结果
     */
    @Override
    public int insertBusBpmnInfo(BusBpmnInfo busBpmnInfo)
    {
        return busBpmnInfoMapper.insertBusBpmnInfo(busBpmnInfo);
    }

    /**
     * 修改流程定义明细
     * 
     * @param busBpmnInfo 流程定义明细
     * @return 结果
     */
    @Override
    public int updateBusBpmnInfo(BusBpmnInfo busBpmnInfo)
    {
        return busBpmnInfoMapper.updateBusBpmnInfo(busBpmnInfo);
    }

    /**
     * 批量删除流程定义明细
     * 
     * @param ids 需要删除的流程定义明细主键
     * @return 结果
     */
    @Override
    public int deleteBusBpmnInfoByIds(Long[] ids)
    {
        return busBpmnInfoMapper.deleteBusBpmnInfoByIds(ids);
    }

    /**
     * 删除流程定义明细信息
     * 
     * @param id 流程定义明细主键
     * @return 结果
     */
    @Override
    public int deleteBusBpmnInfoById(Long id)
    {
        return busBpmnInfoMapper.deleteBusBpmnInfoById(id);
    }

    /**
     * 流程定义的部署
     * @param bpmnType
     * @param bpmnLabel
     * @param file
     * @param info
     */
    @Override
    @Transactional
    public void flow(Integer bpmnType, String bpmnLabel, MultipartFile file, String info) throws IOException {
        // 1.判断文件不为空，文件类型必须为bpmn
        if(file==null||file.getSize()==0){
            throw new RuntimeException("文件不能为空");
        }
        String extension = FileUploadUtils.getExtension(file);
        if(!"bpmn".equals(extension)){
            throw new RuntimeException("文件类型必须为bpmn");
        }
        if(StringUtils.isEmpty(bpmnLabel)||StringUtils.isEmpty(info)||bpmnType==null){
            throw new RuntimeException("非法操作");
        }
        // 2.把bpmn部署到activiti
        Deployment deploy = repositoryService.createDeployment().
                addInputStream(bpmnLabel, file.getInputStream()).deploy();
        // 3.根据deploymentId查询流程定义对象
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();
        // 4.创建BpmnInfo，封装到BpmnInfo
        BusBpmnInfo busBpmnInfo = new BusBpmnInfo();
        busBpmnInfo.setInfo(info);
        busBpmnInfo.setBpmnLabel(bpmnLabel);
        busBpmnInfo.setBpmnType(bpmnType);
        busBpmnInfo.setDeployTime(deploy.getDeploymentTime());
        busBpmnInfo.setVersion(Long.valueOf(processDefinition.getVersion()));
        busBpmnInfo.setProcessDefinitionKey(processDefinition.getKey());
        busBpmnInfoMapper.insertBusBpmnInfo(busBpmnInfo);
    }

    /**
     * 流程文件的查看
     * @param type
     * @param id
     * @return
     */
    @Override
    public InputStream queryProcess(String type, Long id) {
        // 1.判断id，type不为空
        if(id==null||StringUtils.isEmpty(type)){
            throw new RuntimeException("非法操作");
        }
        // 2.根据id查询bpmnInfo对象
        BusBpmnInfo busBpmnInfo = busBpmnInfoMapper.selectBusBpmnInfoById(id);
        if(busBpmnInfo==null){
            throw new RuntimeException("非法操作");
        }
        // 3.查询流程定义对象
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().
                processDefinitionKey(busBpmnInfo.getProcessDefinitionKey()).
                processDefinitionVersion(Integer.parseInt(String.valueOf(busBpmnInfo.getVersion()))).singleResult();


        // 4.判断type是xml还是png
        InputStream inputStream = null;
        if("xml".equals(type)){
            inputStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), processDefinition.getResourceName());

        }else if("png".equals(type)){
            DefaultProcessDiagramGenerator generator = new DefaultProcessDiagramGenerator();
            // 获取bpmnModel
            BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
            inputStream = generator.generateDiagram(bpmnModel, Collections.emptyList(),Collections.emptyList(),"宋体","宋体","宋体");
        }else{
            throw new RuntimeException("非法操作");
        }
        return inputStream;
    }

    /**
     * 流程定义的撤销
     * @param id
     */
    @Override
    @Transactional
    public void revoke(Long id) {
        // 1.判断id不为空，根据id查询数据
        if(id==null){
            throw new RuntimeException("非法操作");
        }
        BusBpmnInfo busBpmnInfo = busBpmnInfoMapper.selectBusBpmnInfoById(id);
        if(busBpmnInfo==null){
            throw new RuntimeException("非法操作");
        }
        // 2.删除bpmnInfo对象
        busBpmnInfoMapper.deleteBusBpmnInfoById(id);
        /**
         * TODO
         * 1.根据流程定义查询，流程定义的流程实例
         * 2.遍历流程实例集合
         * 3.根据流程实例获取businessKey（业务表id）
         * 4.根据businessKey把状态修改为初始化状态
         */

        // 3.删除流程定义的信息，包括流程定义下的流程实例
        /**
         * 如何获取deploymentId，如果能拿到流程定义对象，可以拿到deploymentId
         */
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().
                processDefinitionKey(busBpmnInfo.getProcessDefinitionKey()).
                processDefinitionVersion(Integer.parseInt(String.valueOf(busBpmnInfo.getVersion()))).singleResult();
        repositoryService.deleteDeployment(processDefinition.getDeploymentId(),true);
    }
}
