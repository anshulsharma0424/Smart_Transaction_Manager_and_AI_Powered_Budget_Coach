package com.spenzr.suggestion_service.dto;

import lombok.Builder;
import lombok.Data;
import java.time.Instant;

@Data
@Builder
public class SuggestionDto {
    private String id;
    private String userId;
    private String suggestionContent;
    private String relatedTransactionId;
    private String relatedTransactionDescription;
    private boolean isRead;
    private Instant createdAt;
}

