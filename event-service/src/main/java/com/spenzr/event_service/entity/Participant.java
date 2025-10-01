package com.spenzr.event_service.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Participant {
    private String userId; // Keycloak ID
    private String name; // User's full name for display
}
