package com.shiyi.business.domain;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.shiyi.common.annotation.Excel;
import com.shiyi.common.core.domain.BaseEntity;

/**
 * 结算单明细对象 bus_statement_item
 * 
 * @author shiyi
 * @date 2023-09-22
 */
@Getter
@Setter
public class BusStatementItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 结算单ID */
    @Excel(name = "结算单ID")
    private Long statementId;

    /** 服务项明细ID */
    @Excel(name = "服务项明细ID")
    private Long itemId;

    /** 服务项明细名称 */
    @Excel(name = "服务项明细名称")
    private String itemName;

    /** 服务项价格 */
    @Excel(name = "服务项价格")
    private BigDecimal itemPrice;

    /** 购买数量 */
    @Excel(name = "购买数量")
    private BigDecimal itemQuantity;


}
