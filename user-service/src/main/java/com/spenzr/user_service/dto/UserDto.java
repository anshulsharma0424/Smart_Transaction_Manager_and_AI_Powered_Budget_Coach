package com.spenzr.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String keycloakId;
    private String email;
    private String firstName;
    private String lastName;
}

/*
Explanation: This is a plain Java object used to transfer data between the service and the client,
             preventing exposure of the JPA entity.
 */