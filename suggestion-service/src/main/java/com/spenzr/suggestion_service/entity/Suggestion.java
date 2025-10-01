package com.spenzr.suggestion_service.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document(collection = "suggestions")
@Data
@Builder
public class Suggestion {
    @Id
    private String id;
    private String userId;
    private String suggestionContent;
    private String relatedTransactionId;
    private String relatedTransactionDescription;
    private boolean isRead;
    @CreatedDate
    private Instant createdAt;
}

