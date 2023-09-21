package com.shiyi.business.service.impl;

import java.util.Date;
import java.util.List;

import com.shiyi.business.domain.vo.BusAppointmentVo;
import com.shiyi.business.util.RegexUtils;
import com.shiyi.business.util.VehiclePlateNoUtil;
import com.shiyi.common.utils.DateUtils;
import org.springframework.beans.BeanUtils;
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
     * @param busAppointmentVo 预约信息
     * @return 结果
     */
    @Override
    public int insertBusAppointment(BusAppointmentVo busAppointmentVo)
    {
        // 1.判断vo不为空，判断手机号，车牌号，预约时间校验
        if(busAppointmentVo==null) {
            throw new RuntimeException("非法参数");
        }
        if(!RegexUtils.isPhoneLegal(busAppointmentVo.getCustomerPhone().toString())){
            throw new RuntimeException("手机格式不正确");
        }
        if(new Date().before(busAppointmentVo.getAppointmentTime())){
            throw new RuntimeException("预约时间不合理");
        }
        VehiclePlateNoUtil.VehiclePlateNoEnum vehiclePlateNo = VehiclePlateNoUtil.getVehiclePlateNo(busAppointmentVo.getLicensePlate());
        if(vehiclePlateNo==null){
            throw new RuntimeException("车牌号码不合法");
        }
        // 2.把vo数据封装到BusAppointment中
        BusAppointment busAppointment = new BusAppointment();
        BeanUtils.copyProperties(busAppointmentVo,busAppointment);
        // 3.设置时间和状态
        busAppointment.setCreateTime(new Date());
        busAppointment.setStatus(BusAppointment.STATUS_APPOINTMENT);
        return busAppointmentMapper.insertBusAppointment(busAppointment);
    }

    /**
     * 修改预约信息
     * 
     * @param busAppointmentVo 预约信息
     * @return 结果
     */
    @Override
    public int updateBusAppointment(BusAppointmentVo busAppointmentVo)
    {
        // 1.判断vo不为空，判断手机号，车牌号，预约时间校验
        if(busAppointmentVo==null) {
            throw new RuntimeException("非法参数");
        }
        if(!RegexUtils.isPhoneLegal(busAppointmentVo.getCustomerPhone().toString())){
            throw new RuntimeException("手机格式不正确");
        }
        if(new Date().before(busAppointmentVo.getAppointmentTime())){
            throw new RuntimeException("预约时间不合理");
        }
        VehiclePlateNoUtil.VehiclePlateNoEnum vehiclePlateNo = VehiclePlateNoUtil.getVehiclePlateNo(busAppointmentVo.getLicensePlate());
        if(vehiclePlateNo==null){
            throw new RuntimeException("车牌号码不合法");
        }
        // 2.根据id查询对象
        BusAppointment busAppointment = busAppointmentMapper.selectBusAppointmentById(busAppointmentVo.getId());
        if(busAppointment==null){
            throw new RuntimeException("非法操作");
        }
        // 3.判断状态是否为预约中
        if(!BusAppointment.STATUS_APPOINTMENT.equals(busAppointment.getStatus())){
            throw new RuntimeException("状态必须为预约中");
        }
        // 4.把vo数据封装到BusAppointment中
        BeanUtils.copyProperties(busAppointmentVo,busAppointment);
        // 3.设置时间和状态
        busAppointment.setCreateTime(new Date());
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
