package com.instashop.auth_service.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Login with email and password")
public class LoginRequest {

    @NotBlank
    @Email
    @Schema(example = "user@example.com")
    private String email;


    @NotBlank
    @Schema(example = "Secret123!")
    private String password;
}
