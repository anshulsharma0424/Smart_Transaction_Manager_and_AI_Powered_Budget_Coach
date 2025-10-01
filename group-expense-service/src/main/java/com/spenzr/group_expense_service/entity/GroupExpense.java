package com.spenzr.group_expense_service.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Document(collection = "group_expenses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupExpense {
    @Id
    private String id;
    private String eventId;
    private String description;
    private BigDecimal amount;
    private LocalDate expenseDate;
    private String paidByUserId;
    private String paidByUserName;
    private List<Split> splits;
    private Instant createdAt;
}