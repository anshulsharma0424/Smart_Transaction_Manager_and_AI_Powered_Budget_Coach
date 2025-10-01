package com.spenzr.data_aggregation_service.client;

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
