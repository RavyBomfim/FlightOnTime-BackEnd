package com.flightontime.api.exception.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "Detalhes da resposta de erro")
public class ErrorResponse {

   @Schema(description = "Data e hora do erro", example = "2026-01-08T15:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "Status HTTP", example = "400")
    private int status;

    @Schema(description = "Mensagem resumida do erro", example = "Erro de validação")
    private String error;

    @Schema(description = "Detalhes adicionais do erro")
    private Object details;
}

