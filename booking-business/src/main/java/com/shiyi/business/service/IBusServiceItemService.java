package com.shiyi.business.service;

import java.util.List;
import com.shiyi.business.domain.BusServiceItem;
import com.shiyi.business.domain.vo.BusAppointmentVo;
import com.shiyi.business.domain.vo.BusServiceItemVo;

/**
 * 服务项Service接口
 * 
 * @author shiyi
 * @date 2023-09-21
 */
public interface IBusServiceItemService 
{
    /**
     * 查询服务项
     * 
     * @param id 服务项主键
     * @return 服务项
     */
    public BusServiceItem selectBusServiceItemById(Long id);

    /**
     * 查询服务项列表
     * 
     * @param busServiceItem 服务项
     * @return 服务项集合
     */
    public List<BusServiceItem> selectBusServiceItemList(BusServiceItem busServiceItem);

    /**
     * 新增服务项
     * 
     * @param busServiceItem 服务项
     * @return 结果
     */
    public int insertBusServiceItem(BusServiceItemVo busServiceItem);

    /**
     * 修改服务项
     * 
     * @param busServiceItem 服务项
     * @return 结果
     */
    public int updateBusServiceItem(BusServiceItemVo busServiceItem);

    /**
     * 批量删除服务项
     * 
     * @param ids 需要删除的服务项主键集合
     * @return 结果
     */
    public int deleteBusServiceItemByIds(Long[] ids);

    /**
     * 删除服务项信息
     * 
     * @param id 服务项主键
     * @return 结果
     */
    public int deleteBusServiceItemById(Long id);

    /**
     * 上架
     * @param id
     * @return
     */
    int saleOn(Long id);

    /**
     * 下架
     * @param id
     * @return
     */
    int saleOff(Long id);
}
