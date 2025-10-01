package com.spenzr.suggestion_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateSuggestionRequest {
    private String userId;
    private String suggestionContent;
    private String relatedTransactionId;
    private String relatedTransactionDescription;
}
