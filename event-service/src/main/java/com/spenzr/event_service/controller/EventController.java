package com.spenzr.event_service.controller;

import com.spenzr.event_service.dto.EventDto;
import com.spenzr.event_service.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventDto> createEvent(@Valid @RequestBody EventDto eventDto, @AuthenticationPrincipal Jwt jwt) {
        EventDto createdEvent = eventService.createEvent(eventDto, jwt);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EventDto>> getMyEvents(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return ResponseEntity.ok(eventService.getEventsForUser(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable("id") String eventId, @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        eventService.deleteEvent(eventId, userId);
        return ResponseEntity.noContent().build();
    }

    // ADD THESE NEW ENDPOINTS
    @PostMapping("/{id}/participants")
    public ResponseEntity<EventDto> addParticipant(
            @PathVariable("id") String eventId,
            @RequestParam String email,
            @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        EventDto updatedEvent = eventService.addParticipant(eventId, email, userId);
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/{id}/participants/{participantId}")
    public ResponseEntity<Void> removeParticipant(
            @PathVariable("id") String eventId,
            @PathVariable("participantId") String participantId,
            @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        eventService.removeParticipant(eventId, participantId, userId);
        return ResponseEntity.noContent().build();
    }

    // ADD THIS NEW ENDPOINT
    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getEventById(@PathVariable("id") String eventId, @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return ResponseEntity.ok(eventService.getEventById(eventId, userId));
    }
}
