package com.shiyi.business.service.impl;

import java.math.BigDecimal;
import java.util.List;

import com.shiyi.business.domain.BusStatement;
import com.shiyi.business.domain.vo.BusStatementItemVo;
import com.shiyi.business.mapper.BusStatementMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.shiyi.business.mapper.BusStatementItemMapper;
import com.shiyi.business.domain.BusStatementItem;
import com.shiyi.business.service.IBusStatementItemService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 结算单明细Service业务层处理
 * 
 * @author shiyi
 * @date 2023-09-22
 */
@Service
public class BusStatementItemServiceImpl implements IBusStatementItemService 
{
    @Autowired
    private BusStatementItemMapper busStatementItemMapper;
    @Autowired
    private BusStatementMapper busStatementMapper;

    /**
     * 查询结算单明细
     * 
     * @param id 结算单明细主键
     * @return 结算单明细
     */
    @Override
    public BusStatementItem selectBusStatementItemById(Long id)
    {
        return busStatementItemMapper.selectBusStatementItemById(id);
    }

    /**
     * 查询结算单明细列表
     * 
     * @param busStatementItem 结算单明细
     * @return 结算单明细
     */
    @Override
    public List<BusStatementItem> selectBusStatementItemList(BusStatementItem busStatementItem)
    {
        return busStatementItemMapper.selectBusStatementItemList(busStatementItem);
    }

    /**
     * 新增结算单明细
     * 
     * @param busStatementItemVo 结算单明细
     * @return 结果
     */
    @Override
    @Transactional
    public int insertBusStatementItem(BusStatementItemVo busStatementItemVo)
    {
        // 1.vo非空校验
        if(busStatementItemVo==null){
            throw new RuntimeException("非法参数");
        }
        if(busStatementItemVo.getBusStatementItems()==null||busStatementItemVo.getBusStatementItems().size()==0){
            throw new RuntimeException("明细不能为空");
        }
        // 2.获取statementId
        BusStatementItem busStatementItem = busStatementItemVo.getBusStatementItems().get(0);
        Long statementId = busStatementItem.getStatementId();
        BusStatement busStatement = busStatementMapper.selectBusStatementById(statementId);
        if(busStatement==null){
            throw new RuntimeException("非法操作");
        }
        // 3.判断状态是否为消费中
        if(!BusStatement.STATUS_CONSUME.equals(busStatement.getStatus())){
            throw new RuntimeException("状态必须为消费中");
        }
        // 4.遍历集合
        BigDecimal totalAmount = new BigDecimal(0);
        BigDecimal totalQuantity = new BigDecimal(0);
        for (BusStatementItem statementItem : busStatementItemVo.getBusStatementItems()) {
            // 4.1计算总金额，计算总数
            totalAmount=totalAmount.add(statementItem.getItemPrice().multiply(statementItem.getItemQuantity()));
            totalQuantity = totalQuantity.add(statementItem.getItemQuantity());
            // 4.2把statementItem插入明细
            busStatementItemMapper.insertBusStatementItem(statementItem);
        }

        // 5.判断折扣金额不能大于总金额
        if(busStatementItemVo.getDiscountPrice().compareTo(totalAmount)>0){
            throw new RuntimeException("折扣金额不能大于总金额");
        }
        // 6.跟新结算单中总金额、总数量、折扣金额
        busStatement.setTotalAmount(totalAmount);
        busStatement.setTotalQuantity(totalQuantity);
        busStatement.setDiscountAmount(busStatement.getDiscountAmount());
        return busStatementMapper.updateBusStatement(busStatement);
    }

    /**
     * 修改结算单明细
     * 
     * @param busStatementItem 结算单明细
     * @return 结果
     */
    @Override
    public int updateBusStatementItem(BusStatementItem busStatementItem)
    {
        return busStatementItemMapper.updateBusStatementItem(busStatementItem);
    }

    /**
     * 批量删除结算单明细
     * 
     * @param ids 需要删除的结算单明细主键
     * @return 结果
     */
    @Override
    public int deleteBusStatementItemByIds(Long[] ids)
    {
        return busStatementItemMapper.deleteBusStatementItemByIds(ids);
    }

    /**
     * 删除结算单明细信息
     * 
     * @param id 结算单明细主键
     * @return 结果
     */
    @Override
    public int deleteBusStatementItemById(Long id)
    {
        return busStatementItemMapper.deleteBusStatementItemById(id);
    }
}
