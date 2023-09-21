package com.shiyi.business.service.impl;

import java.util.Date;
import java.util.List;

import com.shiyi.business.domain.vo.BusStatementVo;
import com.shiyi.business.util.RegexUtils;
import com.shiyi.business.util.VehiclePlateNoUtil;
import com.shiyi.common.utils.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.shiyi.business.mapper.BusStatementMapper;
import com.shiyi.business.domain.BusStatement;
import com.shiyi.business.service.IBusStatementService;

/**
 * 结算单Service业务层处理
 * 
 * @author shiyi
 * @date 2023-09-21
 */
@Service
public class BusStatementServiceImpl implements IBusStatementService 
{
    @Autowired
    private BusStatementMapper busStatementMapper;

    /**
     * 查询结算单
     * 
     * @param id 结算单主键
     * @return 结算单
     */
    @Override
    public BusStatement selectBusStatementById(Long id)
    {
        return busStatementMapper.selectBusStatementById(id);
    }

    /**
     * 查询结算单列表
     * 
     * @param busStatement 结算单
     * @return 结算单
     */
    @Override
    public List<BusStatement> selectBusStatementList(BusStatement busStatement)
    {
        return busStatementMapper.selectBusStatementList(busStatement);
    }

    /**
     * 新增结算单
     * 
     * @param busStatementVo 结算单
     * @return 结果
     */
    @Override
    public int insertBusStatement(BusStatementVo busStatementVo)
    {
        // 1.非空校验，手机号码、车牌号码校验
        if(busStatementVo==null){
            throw new RuntimeException("非法参数");
        }
        if(!RegexUtils.isPhoneLegal(busStatementVo.getCustomerPhone())){
            throw new RuntimeException("手机号码格式不正确");
        }
        VehiclePlateNoUtil.VehiclePlateNoEnum vehiclePlateNo = VehiclePlateNoUtil.getVehiclePlateNo(busStatementVo.getLicensePlate());
        if(vehiclePlateNo==null){
            throw new RuntimeException("车牌号码格式不正确");
        }
        // 2.把Vo数据封装到对象中
        BusStatement busStatement = new BusStatement();
        BeanUtils.copyProperties(busStatementVo,busStatement);
        // 3.设置字段时间createTime
        busStatement.setCreateTime(new Date());
        busStatement.setActualArrivalTime(new Date());
        busStatement.setStatus(BusStatement.STATUS_CONSUME);
        // 4.保存数据
        return busStatementMapper.insertBusStatement(busStatement);
    }

    /**
     * 修改结算单
     * 
     * @param busStatement 结算单
     * @return 结果
     */
    @Override
    public int updateBusStatement(BusStatement busStatement)
    {
        return busStatementMapper.updateBusStatement(busStatement);
    }

    /**
     * 批量删除结算单
     * 
     * @param ids 需要删除的结算单主键
     * @return 结果
     */
    @Override
    public int deleteBusStatementByIds(Long[] ids)
    {
        return busStatementMapper.deleteBusStatementByIds(ids);
    }

    /**
     * 删除结算单信息
     * 
     * @param id 结算单主键
     * @return 结果
     */
    @Override
    public int deleteBusStatementById(Long id)
    {
        return busStatementMapper.deleteBusStatementById(id);
    }
}
