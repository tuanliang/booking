package com.shiyi.business.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shiyi.common.annotation.Excel;
import com.shiyi.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 结算单对象 bus_statement
 * 
 * @author shiyi
 * @date 2023-09-21
 */
@Getter
@Setter
@ToString
public class BusStatementVo extends BaseEntity
{

    private Long id;
    private String customerName;
    private String customerPhone;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")//timezone指定时区
    private Date actualArrivalTime;
    private String licensePlate;

    private String carSeries;
    private Integer serviceType;

    private String info;


}
