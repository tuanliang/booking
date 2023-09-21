package com.shiyi.business.service.impl;

import java.util.Date;
import java.util.List;

import com.shiyi.business.domain.vo.BusAppointmentVo;
import com.shiyi.business.domain.vo.BusServiceItemVo;
import com.shiyi.common.utils.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.shiyi.business.mapper.BusServiceItemMapper;
import com.shiyi.business.domain.BusServiceItem;
import com.shiyi.business.service.IBusServiceItemService;

/**
 * 服务项Service业务层处理
 * 
 * @author shiyi
 * @date 2023-09-21
 */
@Service
public class BusServiceItemServiceImpl implements IBusServiceItemService 
{
    @Autowired
    private BusServiceItemMapper busServiceItemMapper;

    /**
     * 查询服务项
     * 
     * @param id 服务项主键
     * @return 服务项
     */
    @Override
    public BusServiceItem selectBusServiceItemById(Long id)
    {
        return busServiceItemMapper.selectBusServiceItemById(id);
    }

    /**
     * 查询服务项列表
     * 
     * @param busServiceItem 服务项
     * @return 服务项
     */
    @Override
    public List<BusServiceItem> selectBusServiceItemList(BusServiceItem busServiceItem)
    {
        return busServiceItemMapper.selectBusServiceItemList(busServiceItem);
    }

    /**
     * 新增服务项
     * 
     * @param busServiceItemVo 服务项
     * @return 结果
     */
    @Override
    public int insertBusServiceItem(BusServiceItemVo busServiceItemVo)
    {
        // 1.参数校验，价格校验
        if(busServiceItemVo==null){
            throw new RuntimeException("非法操作");
        }
        if(busServiceItemVo.getDiscountPrice().compareTo(busServiceItemVo.getOriginalPrice())>0){
            throw new RuntimeException("折扣金额不能大于原价");
        }
        // 2.把vo数据放到对象中
        BusServiceItem busServiceItem = new BusServiceItem();
        BeanUtils.copyProperties(busServiceItemVo,busServiceItem);
        // 3.判断状态是否为套餐设置审核状态
        if(BusServiceItem.CARPACKAGE_YES.equals(busServiceItem.getCarPackage())){
            busServiceItem.setAuditStatus(BusServiceItem.AUDITSTATUS_INIT);
        }else if(BusServiceItem.CARPACKAGE_NO.equals(busServiceItem.getCarPackage())){
            busServiceItem.setAuditStatus(BusServiceItem.AUDITSTATUS_NO_REQUIRED);
        }else{
            throw new RuntimeException("非法操作");
        }
        // 4.设置隐藏字段属性
        busServiceItem.setCreateTime(new Date());
        // 5.把数据保存到数据库中
        return busServiceItemMapper.insertBusServiceItem(busServiceItem);
    }

    /**
     * 修改服务项
     *
     * @param busServiceItemVo 服务项
     * @return 结果
     */
    @Override
    public int updateBusServiceItem(BusServiceItemVo busServiceItemVo)
    {
        // 1.参数校验，价格校验
        if(busServiceItemVo==null){
            throw new RuntimeException("非法操作");
        }
        if(busServiceItemVo.getDiscountPrice().compareTo(busServiceItemVo.getOriginalPrice())>0){
            throw new RuntimeException("折扣金额不能大于原价");
        }
        // 2.根据id查询数据
        BusServiceItem busServiceItem = busServiceItemMapper.selectBusServiceItemById(busServiceItemVo.getId());
        if(busServiceItem==null){
            throw new RuntimeException("非法操作");
        }
        // 3.如果审核状态是审核中或审核通过或上架状态
        if((BusServiceItem.AUDITSTATUS_AUDITING.equals(busServiceItem.getAuditStatus())||
                BusServiceItem.AUDITSTATUS_APPROVED.equals(busServiceItem.getAuditStatus())||
                BusServiceItem.SALESTATUS_ON.equals(busServiceItem.getSaleStatus()))){
            throw new RuntimeException("状态不合法");
        }
        // 4.把vo数据封装到对象中
        BeanUtils.copyProperties(busServiceItemVo,busServiceItem);
        // 5.如果是套餐，则状态改为初始化，如果不是套餐，状态改为无需审核
        if(BusServiceItem.CARPACKAGE_YES.equals(busServiceItem.getCarPackage())){
            busServiceItem.setAuditStatus(BusServiceItem.AUDITSTATUS_INIT);
        }else{
            busServiceItem.setAuditStatus(BusServiceItem.AUDITSTATUS_NO_REQUIRED);
        }

        // 6.修改数据库数据
        return busServiceItemMapper.updateBusServiceItem(busServiceItem);
    }

    /**
     * 批量删除服务项
     * 
     * @param ids 需要删除的服务项主键
     * @return 结果
     */
    @Override
    public int deleteBusServiceItemByIds(Long[] ids)
    {
        return busServiceItemMapper.deleteBusServiceItemByIds(ids);
    }

    /**
     * 删除服务项信息
     * 
     * @param id 服务项主键
     * @return 结果
     */
    @Override
    public int deleteBusServiceItemById(Long id)
    {
        return busServiceItemMapper.deleteBusServiceItemById(id);
    }

    /**
     * 上架
     * @param id
     * @return
     */
    @Override
    public int saleOn(Long id) {
        // 1.判空
        if(id==null){
            throw new RuntimeException("非法操作");
        }
        // 2.根据id查询数据
        BusServiceItem busServiceItem = busServiceItemMapper.selectBusServiceItemById(id);
        if(busServiceItem==null){
            throw new RuntimeException("非法操作");
        }
        // 3.判断如果是套餐审核状态是非审核通过
        if(BusServiceItem.CARPACKAGE_YES.equals(busServiceItem.getCarPackage())&&
            !BusServiceItem.AUDITSTATUS_APPROVED.equals(busServiceItem.getAuditStatus())){
            throw new RuntimeException("套餐必须审核通过才能上架");
        }
        if(!BusServiceItem.SALESTATUS_OFF.equals(busServiceItem.getSaleStatus())){
            throw new RuntimeException("必须为下架状态才能上架");
        }
        // 4.修改上架的状态
        return busServiceItemMapper.changeSaleStatus(id,BusServiceItem.SALESTATUS_ON);
    }

    /**
     * 下架
     * @param id
     * @return
     */
    @Override
    public int saleOff(Long id) {
        // 1.判空
        if(id==null){
            throw new RuntimeException("非法操作");
        }
        // 2.根据id查询数据
        BusServiceItem busServiceItem = busServiceItemMapper.selectBusServiceItemById(id);
        if(busServiceItem==null){
            throw new RuntimeException("非法操作");
        }
        // 3.判断必须是上架状态才可以下架
        if(BusServiceItem.SALESTATUS_OFF.equals(busServiceItem.getSaleStatus())){
            throw new RuntimeException("必须为上架状态才能下架");
        }
        // 4.修改上架的状态
        return busServiceItemMapper.changeSaleStatus(id,BusServiceItem.SALESTATUS_OFF);
    }
}
