package com.shiyi.business.qo;

import com.shiyi.business.domain.BusServiceItem;
import com.shiyi.common.core.domain.entity.SysUser;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ServiceItemAuditInfo {
    private BusServiceItem serviceItem;
    private List<SysUser>shopOwners;
    private List<SysUser>finances;
    private BigDecimal discountPrice;
}
