package com.spenzr.event_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDto {
    private String id;

    @NotBlank(message = "Event name is required")
    private String name;

    private String description;

    @NotNull(message = "Event date is required")
    private LocalDate eventDate;

    private String createdBy; // <-- ADD THIS LINE

    private Set<ParticipantDto> participants;
}
