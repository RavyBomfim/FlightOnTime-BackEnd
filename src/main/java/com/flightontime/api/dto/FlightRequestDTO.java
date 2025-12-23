package com.flightontime.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record FlightRequestDTO(
        @NotBlank(message = "A companhia aérea é obrigatória")
        @Size(min = 2, max = 100, message = "O nome da companhia aérea deve pelo menos 2 caracteres")
        String airline,

        @NotBlank(message = "O aeroporto de origem é obrigatório")
        @Size(min = 4, max = 4, message = "O código do aeroporto de origem deve ter 4 caracteres")
        String origin,

        @NotBlank(message = "O aeroporto de destino é obrigatório")
        @Size(min = 4, max = 4, message = "O código do aeroporto de destino deve ter 4 caracteres")
        String destination,

        @NotNull(message = "A data de partida é obrigatória")
        LocalDate scheduledDepartureDate,

        @NotNull(message = "A hora de partida é obrigatória")
        LocalTime scheduledDepartureTime
) {
}
