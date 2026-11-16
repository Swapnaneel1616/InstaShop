package com.instashop.auth_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Register with email and password")
public class RegisterRequest {

    @NotBlank
    @Email
    @Schema(example = "user@example.com")
    private String email;

    @NotBlank
    @Size(min = 8 ,max = 100)
    @Schema(example = "Secret123!")
    private String password;

    @Size(max = 200)
    @Schema(example = "Neel")
    private String name;
}
