package com.spenzr.ai_inference_service.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SuggestionRequest {
    private String description;
    private BigDecimal amount;
    private String categoryId;
}
