package com.shiyi.business.service;

import java.util.List;
import com.shiyi.business.domain.BusAppointment;
import com.shiyi.business.domain.vo.BusAppointmentVo;

/**
 * 预约信息Service接口
 * 
 * @author shiyi
 * @date 2023-09-20
 */
public interface IBusAppointmentService 
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
    public int insertBusAppointment(BusAppointmentVo busAppointment);

    /**
     * 修改预约信息
     * 
     * @param busAppointment 预约信息
     * @return 结果
     */
    public int updateBusAppointment(BusAppointmentVo busAppointment);

    /**
     * 批量删除预约信息
     * 
     * @param ids 需要删除的预约信息主键集合
     * @return 结果
     */
    public int deleteBusAppointmentByIds(Long[] ids);

    /**
     * 删除预约信息信息
     * 
     * @param id 预约信息主键
     * @return 结果
     */
    public int deleteBusAppointmentById(Long id);

    /**
     * 用户到店
     * @param id
     * @return
     */
    int arralShop(Long id);

    /**
     * 预约取消
     * @param id
     * @return
     */
    int cancel(Long id);
}
