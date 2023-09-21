package com.shiyi.business.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shiyi.common.annotation.Excel;
import com.shiyi.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 预约信息对象 bus_appointment
 * 
 * @author shiyi
 * @date 2023-09-20
 */
@Getter
@Setter
public class BusAppointmentVo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long id;
    private String customerName;
    private Long customerPhone;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date appointmentTime;
    private String licensePlate;
    private String carSeries;
    private Integer serviceType;
    private String info;
}
