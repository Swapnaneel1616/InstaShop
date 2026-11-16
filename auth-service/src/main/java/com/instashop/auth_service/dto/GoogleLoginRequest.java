package com.instashop.auth_service.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Login with Google ID Token")
public class GoogleLoginRequest {

    @NotBlank
    @Schema(description = "ID Token form Google Sign-in")
    private String idToken;
}
