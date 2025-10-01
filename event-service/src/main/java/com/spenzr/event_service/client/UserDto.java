package com.spenzr.event_service.client;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String keycloakId;
    private String email;
    private String firstName;
    private String lastName;
}
