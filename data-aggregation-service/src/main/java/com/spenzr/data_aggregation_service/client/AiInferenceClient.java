package com.spenzr.data_aggregation_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ai-inference-service")
public interface AiInferenceClient {

    @PostMapping("/inference/generate-suggestion")
    SuggestionResponse generateSuggestion(
            @RequestHeader("Authorization") String token,
            @RequestBody SuggestionRequest request
    );
}