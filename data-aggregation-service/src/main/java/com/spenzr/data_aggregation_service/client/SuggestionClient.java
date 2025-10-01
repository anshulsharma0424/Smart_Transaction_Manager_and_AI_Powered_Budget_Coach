package com.spenzr.data_aggregation_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "suggestion-service")
public interface SuggestionClient {
    @PostMapping("/suggestions")
    void saveSuggestion(
            @RequestHeader("Authorization") String token,
            @RequestBody CreateSuggestionRequest request
    );
}