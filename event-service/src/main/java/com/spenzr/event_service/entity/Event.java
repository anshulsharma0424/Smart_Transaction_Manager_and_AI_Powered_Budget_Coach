package com.spenzr.event_service.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

@Document(collection = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    private String id;
    private String name;
    private String description;
    private LocalDate eventDate;
    private String createdBy; // Keycloak ID of the creator
    private Instant createdAt;
    private Set<Participant> participants;
}
