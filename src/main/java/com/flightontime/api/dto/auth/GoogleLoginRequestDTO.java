package com.flightontime.api.dto.auth;
import jakarta.validation.constraints.NotBlank;

public record GoogleLoginRequestDTO(
        @NotBlank(message = "Token do Google é obrigatório")
        String token
) {}
 //front manda o id do token