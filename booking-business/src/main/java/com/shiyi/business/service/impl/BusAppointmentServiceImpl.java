package com.shiyi.business.service.impl;

import java.util.List;
import com.shiyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.shiyi.business.mapper.BusAppointmentMapper;
import com.shiyi.business.domain.BusAppointment;
import com.shiyi.business.service.IBusAppointmentService;

/**
 * 预约信息Service业务层处理
 * 
 * @author shiyi
 * @date 2023-09-20
 */
@Service
public class BusAppointmentServiceImpl implements IBusAppointmentService 
{
    @Autowired
    private BusAppointmentMapper busAppointmentMapper;

    /**
     * 查询预约信息
     * 
     * @param id 预约信息主键
     * @return 预约信息
     */
    @Override
    public BusAppointment selectBusAppointmentById(Long id)
    {
        return busAppointmentMapper.selectBusAppointmentById(id);
    }

    /**
     * 查询预约信息列表
     * 
     * @param busAppointment 预约信息
     * @return 预约信息
     */
    @Override
    public List<BusAppointment> selectBusAppointmentList(BusAppointment busAppointment)
    {
        return busAppointmentMapper.selectBusAppointmentList(busAppointment);
    }

    /**
     * 新增预约信息
     * 
     * @param busAppointment 预约信息
     * @return 结果
     */
    @Override
    public int insertBusAppointment(BusAppointment busAppointment)
    {
        busAppointment.setCreateTime(DateUtils.getNowDate());
        return busAppointmentMapper.insertBusAppointment(busAppointment);
    }

    /**
     * 修改预约信息
     * 
     * @param busAppointment 预约信息
     * @return 结果
     */
    @Override
    public int updateBusAppointment(BusAppointment busAppointment)
    {
        return busAppointmentMapper.updateBusAppointment(busAppointment);
    }

    /**
     * 批量删除预约信息
     * 
     * @param ids 需要删除的预约信息主键
     * @return 结果
     */
    @Override
    public int deleteBusAppointmentByIds(Long[] ids)
    {
        return busAppointmentMapper.deleteBusAppointmentByIds(ids);
    }

    /**
     * 删除预约信息信息
     * 
     * @param id 预约信息主键
     * @return 结果
     */
    @Override
    public int deleteBusAppointmentById(Long id)
    {
        return busAppointmentMapper.deleteBusAppointmentById(id);
    }
}
