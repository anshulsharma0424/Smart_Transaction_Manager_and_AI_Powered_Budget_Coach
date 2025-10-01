package com.spenzr.settlement_service.client;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SplitDto {
    private String userId;
    private String userName;
    private BigDecimal shareAmount;
}
