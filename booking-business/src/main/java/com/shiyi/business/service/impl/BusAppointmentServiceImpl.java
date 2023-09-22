package com.shiyi.business.service.impl;

import java.util.Date;
import java.util.List;

import com.shiyi.business.domain.BusStatement;
import com.shiyi.business.domain.vo.BusAppointmentVo;
import com.shiyi.business.mapper.BusStatementMapper;
import com.shiyi.business.util.RegexUtils;
import com.shiyi.business.util.VehiclePlateNoUtil;
import com.shiyi.common.utils.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
    @Autowired
    private BusStatementMapper busStatementMapper;

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
        // 1.非空校验
        if(id==null){
            throw new RuntimeException("非法参数");
        }
        // 2.根据id查询对象
        BusAppointment busAppointment = busAppointmentMapper.selectBusAppointmentById(id);
        if(busAppointment==null){
            throw new RuntimeException("非法参数");
        }
        // 3.判断状态是否为预约中或者取消
        if(!(BusAppointment.STATUS_APPOINTMENT.equals(busAppointment.getStatus())||
                BusAppointment.STATUS_CANCEL.equals(busAppointment.getStatus()))){
            throw new RuntimeException("状态必须为预约中或取消");
        }
        return busAppointmentMapper.deleteBusAppointmentById(id);
    }

    /**
     * 用户到店
     * @param id
     * @return
     */
    @Override
    public int arralShop(Long id) {
        // 1.非空校验
        if(id==null){
            throw new RuntimeException("非法参数");
        }
        // 2.根据id查询对象
        BusAppointment busAppointment = busAppointmentMapper.selectBusAppointmentById(id);
        if(busAppointment==null){
            throw new RuntimeException("非法参数");
        }
        // 3.判断状态是否为预约中
        if(!BusAppointment.STATUS_APPOINTMENT.equals(busAppointment.getStatus())){
            throw new RuntimeException("状态必须为预约中");
        }
        // 4.修改状态为到店状态
        return busAppointmentMapper.changeArralShopStatus(id,BusAppointment.STATUS_ARRIVAL );

    }

    /**
     * 预约取消
     * @param id
     * @return
     */
    @Override
    public int cancel(Long id) {
        // 1.判断id不为空
        if(id==null){
            throw new RuntimeException("非法参数");
        }
        // 2.根据id查询对象
        BusAppointment busAppointment = busAppointmentMapper.selectBusAppointmentById(id);
        if(busAppointment==null){
            throw new RuntimeException("非法参数");
        }
        // 3.判断状态是否为预约中
        if(!BusAppointment.STATUS_APPOINTMENT.equals(busAppointment.getStatus())){
            throw new RuntimeException("状态必须为预约中");
        }
        // 4.修改状态为取消状态
        return busAppointmentMapper.changeStatus(id,BusAppointment.STATUS_CANCEL) ;
    }

    /**
     * 预约结算单生成
     * @param id
     * @return
     */
    @Override
    public Long generate(Long id) {
        // 1.判断id不为空
        if(id==null){
            throw new RuntimeException("非法操作");
        }
        // 2.根据id查询数据
        BusAppointment busAppointment = busAppointmentMapper.selectBusAppointmentById(id);
        if(busAppointment==null){
            throw new RuntimeException("非法操作");
        }
        // 3.判断状态是否为到店、结算单已生成、已支付
        if(!(BusAppointment.STATUS_ARRIVAL.equals(busAppointment.getStatus())||
                BusAppointment.STATUS_SETTLE.equals(busAppointment.getStatus())||
                BusAppointment.STATUS_PAYED.equals(busAppointment.getStatus()))){
            throw new RuntimeException("状态不合法");
        }
        // 4.根据预约单id上结算单表中查询数据
        BusStatement busStatement= busStatementMapper.selectByAppointmentId(busAppointment.getId());
        // 5.判断结算单是否为空
        if(busStatement==null){
            // 5.1创建BusStatement对象,BeanUtils.copy
            busStatement= new BusStatement();
            BeanUtils.copyProperties(busAppointment,busStatement);
            // 5.2设置status，appointment_id,createTime
            busStatement.setStatus(BusStatement.STATUS_CONSUME);
            busStatement.setAppointmentId(busAppointment.getId());
            busStatement.setCreateTime(new Date());
            // 5.3BusStatement保存到数据库
            busStatementMapper.insertBusStatement(busStatement);
            // 6.修改预约单结算状态为已生成
            busAppointmentMapper.changeStatus(id,BusAppointment.STATUS_SETTLE);
        }


        return busStatement.getId();
    }


}
