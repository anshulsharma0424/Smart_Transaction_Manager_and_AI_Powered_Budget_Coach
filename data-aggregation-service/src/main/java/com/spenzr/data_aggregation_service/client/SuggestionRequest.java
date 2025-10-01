package com.spenzr.data_aggregation_service.client;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class SuggestionRequest {
    private String description;
    private BigDecimal amount;
    private String categoryId;
}
