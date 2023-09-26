package com.shiyi.business.domain.vo;

import lombok.Data;

@Data
public class AuditVo {
    private Long id;
    private String info;
    private Boolean auditStatus;
}
