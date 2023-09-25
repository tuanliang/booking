package com.shiyi.business.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.shiyi.common.annotation.Excel;
import com.shiyi.common.core.domain.BaseEntity;

/**
 * 套餐审核对象 bus_car_package_audit
 * 
 * @author shiyi
 * @date 2023-09-24
 */
public class BusCarPackageAudit extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    public static final Integer STATUS_IN_ROGRESS = 0;//审核中
    public static final Integer STATUS_REJECT = 1;//审核拒绝
    public static final Integer STATUS_PASS = 2;//审核通过
    public static final Integer STATUS_CANCEL = 3;//审核撤销

    /** 主键 */
    private Long id;

    /** 服务单项id */
    @Excel(name = "服务单项id")
    private Long serviceItemId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String serviceItemName;

    /** 服务单项备注 */
    @Excel(name = "服务单项备注")
    private String serviceItemInfo;

    /** 服务单项审核价格 */
    @Excel(name = "服务单项审核价格")
    private BigDecimal serviceItemPrice;

    /** 流程实例id */
    @Excel(name = "流程实例id")
    private String instanceId;

    /** 创建者 */
    @Excel(name = "创建者")
    private String creatorId;

    /** 备注 */
    @Excel(name = "备注")
    private String info;

    /** 状态【进行中0/审核拒绝1/审核通过2/审核撤销3】 */
    @Excel(name = "状态【进行中0/审核拒绝1/审核通过2/审核撤销3】")
    private Integer status;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setServiceItemId(Long serviceItemId) 
    {
        this.serviceItemId = serviceItemId;
    }

    public Long getServiceItemId() 
    {
        return serviceItemId;
    }
    public void setServiceItemName(String serviceItemName) 
    {
        this.serviceItemName = serviceItemName;
    }

    public String getServiceItemName() 
    {
        return serviceItemName;
    }
    public void setServiceItemInfo(String serviceItemInfo) 
    {
        this.serviceItemInfo = serviceItemInfo;
    }

    public String getServiceItemInfo() 
    {
        return serviceItemInfo;
    }
    public void setServiceItemPrice(BigDecimal serviceItemPrice) 
    {
        this.serviceItemPrice = serviceItemPrice;
    }

    public BigDecimal getServiceItemPrice() 
    {
        return serviceItemPrice;
    }
    public void setInstanceId(String instanceId) 
    {
        this.instanceId = instanceId;
    }

    public String getInstanceId() 
    {
        return instanceId;
    }
    public void setCreatorId(String creatorId) 
    {
        this.creatorId = creatorId;
    }

    public String getCreatorId() 
    {
        return creatorId;
    }
    public void setInfo(String info) 
    {
        this.info = info;
    }

    public String getInfo() 
    {
        return info;
    }
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("serviceItemId", getServiceItemId())
            .append("serviceItemName", getServiceItemName())
            .append("serviceItemInfo", getServiceItemInfo())
            .append("serviceItemPrice", getServiceItemPrice())
            .append("instanceId", getInstanceId())
            .append("creatorId", getCreatorId())
            .append("info", getInfo())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .toString();
    }
}
