package com.shiyi.business.domain.vo;

import lombok.Data;

@Data
public class StartAuditVo {
    private Long financeId;
    private Long id;
    private String info;
    private Long shopOwnerId;
}
