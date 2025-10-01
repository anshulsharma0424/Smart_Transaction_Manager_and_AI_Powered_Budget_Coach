package com.spenzr.suggestion_service.repository;

import com.spenzr.suggestion_service.entity.Suggestion;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface SuggestionRepository extends MongoRepository<Suggestion, String> {
    List<Suggestion> findByUserIdOrderByCreatedAtDesc(String userId);
    void deleteAllByRelatedTransactionId(String transactionId);

}
