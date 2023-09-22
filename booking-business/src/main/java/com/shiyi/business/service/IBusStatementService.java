package com.shiyi.business.service;

import java.util.List;
import com.shiyi.business.domain.BusStatement;
import com.shiyi.business.domain.vo.BusStatementVo;

/**
 * 结算单Service接口
 * 
 * @author shiyi
 * @date 2023-09-21
 */
public interface IBusStatementService 
{
    /**
     * 查询结算单
     * 
     * @param id 结算单主键
     * @return 结算单
     */
    public BusStatement selectBusStatementById(Long id);

    /**
     * 查询结算单列表
     * 
     * @param busStatement 结算单
     * @return 结算单集合
     */
    public List<BusStatement> selectBusStatementList(BusStatement busStatement);

    /**
     * 新增结算单
     * 
     * @param busStatement 结算单
     * @return 结果
     */
    public int insertBusStatement(BusStatementVo busStatement);

    /**
     * 修改结算单
     * 
     * @param busStatement 结算单
     * @return 结果
     */
    public int updateBusStatement(BusStatementVo busStatement);

    /**
     * 批量删除结算单
     * 
     * @param ids 需要删除的结算单主键集合
     * @return 结果
     */
    public int deleteBusStatementByIds(Long[] ids);

    /**
     * 删除结算单信息
     * 
     * @param id 结算单主键
     * @return 结果
     */
    public int deleteBusStatementById(Long id);
}
