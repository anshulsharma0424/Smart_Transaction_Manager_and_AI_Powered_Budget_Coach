package com.spenzr.group_expense_service.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Split {
    private String userId;
    private String userName;
    private BigDecimal shareAmount;
}
