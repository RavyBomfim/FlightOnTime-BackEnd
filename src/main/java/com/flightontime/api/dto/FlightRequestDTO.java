package com.flightontime.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record FlightRequestDTO(
        @NotBlank(message = "A companhia aérea é obrigatória")
        String companhia,

        @NotBlank(message = "O aeroporto de origem é obrigatório")
        String origem,

        @NotBlank(message = "O aeroporto de destino é obrigatório")
        String destino,

        @NotNull(message = "A data de partida é obrigatória")
        LocalDateTime data_partida,

        @NotNull(message = "A distância é obrigatória")
        @Min(value = 1, message = "A distância deve ser maior que 0")
        Double distancia_km
) {
}
