package com.shiyi.business.mapper;

import java.util.List;
import com.shiyi.business.domain.BusAppointment;

/**
 * 预约信息Mapper接口
 * 
 * @author shiyi
 * @date 2023-09-20
 */
public interface BusAppointmentMapper 
{
    /**
     * 查询预约信息
     * 
     * @param id 预约信息主键
     * @return 预约信息
     */
    public BusAppointment selectBusAppointmentById(Long id);

    /**
     * 查询预约信息列表
     * 
     * @param busAppointment 预约信息
     * @return 预约信息集合
     */
    public List<BusAppointment> selectBusAppointmentList(BusAppointment busAppointment);

    /**
     * 新增预约信息
     * 
     * @param busAppointment 预约信息
     * @return 结果
     */
    public int insertBusAppointment(BusAppointment busAppointment);

    /**
     * 修改预约信息
     * 
     * @param busAppointment 预约信息
     * @return 结果
     */
    public int updateBusAppointment(BusAppointment busAppointment);

    /**
     * 删除预约信息
     * 
     * @param id 预约信息主键
     * @return 结果
     */
    public int deleteBusAppointmentById(Long id);

    /**
     * 批量删除预约信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBusAppointmentByIds(Long[] ids);
}
