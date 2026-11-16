package com.instashop.auth_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Public User Profile")
public class UserResponse {

    private UUID id;
    private String email;
    private String name;
}
