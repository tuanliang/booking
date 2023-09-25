package com.shiyi.business.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shiyi.business.domain.BusBpmnInfo;
import com.shiyi.business.domain.BusCarPackageAudit;
import com.shiyi.business.domain.vo.BusAppointmentVo;
import com.shiyi.business.domain.vo.BusServiceItemVo;
import com.shiyi.business.domain.vo.StartAuditVo;
import com.shiyi.business.mapper.BusBpmnInfoMapper;
import com.shiyi.business.mapper.BusCarPackageAuditMapper;
import com.shiyi.business.qo.ServiceItemAuditInfo;
import com.shiyi.common.core.domain.entity.SysUser;
import com.shiyi.common.utils.DateUtils;
import com.shiyi.common.utils.SecurityUtils;
import com.shiyi.system.mapper.SysConfigMapper;
import com.shiyi.system.mapper.SysUserMapper;
import com.shiyi.system.service.ISysConfigService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.shiyi.business.mapper.BusServiceItemMapper;
import com.shiyi.business.domain.BusServiceItem;
import com.shiyi.business.service.IBusServiceItemService;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private BusBpmnInfoMapper bpmnInfoMapper;
    @Autowired
    private BusCarPackageAuditMapper carPackageAuditMapper;
    @Autowired
    private RuntimeService runtimeService;

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

    /**
     * 发起审核
     * @param id
     * @return
     */
    @Override
    public ServiceItemAuditInfo auditPage(Long id) {
        // 1.判断id不为空，根据id查询服务单项
        if(id==null){
            throw new RuntimeException("非法操作");
        }
        BusServiceItem busServiceItem = busServiceItemMapper.selectBusServiceItemById(id);
        if(busServiceItem==null){
            throw new RuntimeException("非法操作");
        }
        // 2.判断单项是否为套餐，状态是否为初始化和重新调整
        if(!BusServiceItem.CARPACKAGE_YES.equals(busServiceItem.getCarPackage())){
            throw new RuntimeException("必须为套餐才可以发起审核");
        }
        if(!(BusServiceItem.AUDITSTATUS_INIT.equals(busServiceItem.getAuditStatus())||BusServiceItem.AUDITSTATUS_REPLY.equals(busServiceItem.getAuditStatus()))){
            throw new RuntimeException("状态必须为初始化后重新调整");
        }
        // 3.创建ServiceItemAuditInf对象，把服务项放到info中
        ServiceItemAuditInfo serviceItemAuditInfo = new ServiceItemAuditInfo();
        serviceItemAuditInfo.setServiceItem(busServiceItem);
        // 4.根据店长的key，获取店长集合，放到info中
        List<SysUser>shopOwners = userMapper.selectByRoleKey("shopOwner");
        serviceItemAuditInfo.setShopOwners(shopOwners);
        // 5.上参数管理获取discountPriceLimit
        String discountPriceLimit = sysConfigService.selectConfigByKey("discountPriceLimit");
        // 6.判断折扣金额如果大于等于discountPriceLimit
        if(busServiceItem.getDiscountPrice().compareTo(new BigDecimal(discountPriceLimit))>=0){
            // 7.根据财务的key，获取财务集合，设置到info对象中
            List<SysUser>finance = userMapper.selectByRoleKey("finance");
            serviceItemAuditInfo.setFinances(finance);
        }

        // 8.把discountPriceLimit放到info中
        serviceItemAuditInfo.setDiscountPrice(new BigDecimal(discountPriceLimit));
        return serviceItemAuditInfo;
    }

    /**
     * 发起审核
     * @param vo
     */
    @Override
    @Transactional
    public void audit(StartAuditVo vo) {
        // 1.vo的非空校验，vo获取id
        if(vo==null){
            throw new RuntimeException("非法操作");
        }
        Long id = vo.getId();
        if(id==null){
            throw new RuntimeException("非法操作");
        }
        // 2.根据id上数据库中查询数据
        BusServiceItem busServiceItem = busServiceItemMapper.selectBusServiceItemById(id);
        // 3.判断数据库中的状态是否为空，判断数据是否为套餐，判断审核状态是否为初始化和重新调整
        if(busServiceItem==null){
            throw new RuntimeException("非法操作");
        }
        if(!BusServiceItem.CARPACKAGE_YES.equals(busServiceItem.getCarPackage())){
            throw new RuntimeException("单项必须是套餐");
        }
        if(!(BusServiceItem.AUDITSTATUS_INIT.equals(busServiceItem.getAuditStatus())||
                BusServiceItem.AUDITSTATUS_REPLY.equals(busServiceItem.getAuditStatus()))){
            throw new RuntimeException("状态必须为初始化或者重新调整");
        }
        // 4.根据type=0 查询bpmnInfo对象,根据版本号降序排序，选择最新版本
        BusBpmnInfo bpmnInfo = bpmnInfoMapper.queryByType(0);
        if(bpmnInfo==null){
            throw new RuntimeException("先部署然后再发起流程");
        }
        // 5.创建carPackAudit对象，把数据封装进去，把对象保存到数据库中
        BusCarPackageAudit busCarPackageAudit = new BusCarPackageAudit();
        busCarPackageAudit.setServiceItemId(busServiceItem.getId());
        busCarPackageAudit.setCreatorId(SecurityUtils.getUserId().toString());
        busCarPackageAudit.setServiceItemName(busServiceItem.getName());
        busCarPackageAudit.setServiceItemInfo(busServiceItem.getInfo());
        busCarPackageAudit.setStatus(BusCarPackageAudit.STATUS_IN_ROGRESS);
        busCarPackageAudit.setInfo(vo.getInfo());
        busCarPackageAudit.setCreateTime(new Date());
        busCarPackageAudit.setServiceItemPrice(busServiceItem.getDiscountPrice());
        carPackageAuditMapper.insertBusCarPackageAudit(busCarPackageAudit);
        // 6.设置流程变量Map<String,Object>
        Map<String,Object>map =  new HashMap<>();
        // 6.1设置店长
        map.put("shopOwnerId",vo.getShopOwnerId());
        // 6.2判断财务id是否为空，如果不为空把财务也放进去
        if(vo.getFinanceId()!=null){
            map.put("financeId",vo.getFinanceId());
        }
        // 6.3把折扣金额设置流程变量中，细节：流程变量不支持BigDecmail类型，需要把他转换成Long类型
        map.put("discountPrice",busServiceItem.getDiscountPrice().longValue());
        // 7.调用runTimeService.startProcessInstanceBykey(key,businessKey,map)启动
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(bpmnInfo.getProcessDefinitionKey(), busCarPackageAudit.getId().toString(), map);

        // 8.跟新流程实例，
        busCarPackageAudit.setInstanceId(processInstance.getId());
        carPackageAuditMapper.updateBusCarPackageAudit(busCarPackageAudit);
        // 9.跟新单项状态为审核中状态
        busServiceItemMapper.changeStatus(vo.getId(),BusServiceItem.AUDITSTATUS_AUDITING);
    }
}
