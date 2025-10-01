package com.spenzr.settlement_service.client;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class GroupExpenseDto {
    private String paidByUserId;
    private BigDecimal amount;
    private List<SplitDto> splits;
}
