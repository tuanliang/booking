package com.shiyi.business.domain.vo;

import com.shiyi.common.annotation.Excel;
import com.shiyi.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 服务项对象 bus_service_item
 * 
 * @author shiyi
 * @date 2023-09-21
 */
@Getter
@Setter
@ToString
public class BusServiceItemVo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;

    /** 服务项原价 */
    private BigDecimal originalPrice;

    /** 服务项折扣价 */
    private BigDecimal discountPrice;

    /** 是否套餐【1是/0否】 */
    private Integer carPackage;

    /** 备注信息 */
    private String info;

    /** 服务分类【0维修/1保养/2其他】 */
    private Integer serviceCatalog;




}
