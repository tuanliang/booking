package com.shiyi.business.domain.vo;


import com.shiyi.business.domain.BusStatementItem;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class BusStatementItemVo {
    private List<BusStatementItem>busStatementItems;
    private BigDecimal discountPrice;
}
