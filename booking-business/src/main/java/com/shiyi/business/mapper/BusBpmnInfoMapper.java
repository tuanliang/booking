package com.shiyi.business.mapper;

import java.util.List;
import com.shiyi.business.domain.BusBpmnInfo;

/**
 * 流程定义明细Mapper接口
 * 
 * @author shiyi
 * @date 2023-09-22
 */
public interface BusBpmnInfoMapper 
{
    /**
     * 查询流程定义明细
     * 
     * @param id 流程定义明细主键
     * @return 流程定义明细
     */
    public BusBpmnInfo selectBusBpmnInfoById(Long id);

    /**
     * 查询流程定义明细列表
     * 
     * @param busBpmnInfo 流程定义明细
     * @return 流程定义明细集合
     */
    public List<BusBpmnInfo> selectBusBpmnInfoList(BusBpmnInfo busBpmnInfo);

    /**
     * 新增流程定义明细
     * 
     * @param busBpmnInfo 流程定义明细
     * @return 结果
     */
    public int insertBusBpmnInfo(BusBpmnInfo busBpmnInfo);

    /**
     * 修改流程定义明细
     * 
     * @param busBpmnInfo 流程定义明细
     * @return 结果
     */
    public int updateBusBpmnInfo(BusBpmnInfo busBpmnInfo);

    /**
     * 删除流程定义明细
     * 
     * @param id 流程定义明细主键
     * @return 结果
     */
    public int deleteBusBpmnInfoById(Long id);

    /**
     * 批量删除流程定义明细
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBusBpmnInfoByIds(Long[] ids);

    /**
     * 根据类型查询数据（为套餐审核的）
     * @param i
     * @return
     */
    BusBpmnInfo queryByType(int type);
}
