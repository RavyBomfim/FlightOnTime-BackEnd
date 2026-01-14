package com.flightontime.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record FlightRequestDTO(
                @NotBlank(message = "A companhia aérea é obrigatória") 
                @Size(min = 2, max = 3, message = "O código da companhia aérea deve ter 2-3 caracteres") 
                @Pattern(regexp = "^[A-Z0-9]{2,3}$", message = "Código da companhia aérea deve conter apenas letras maiúsculas e números")
                @JsonProperty("companhia") 
                String airline,

                @NotBlank(message = "O aeroporto de origem é obrigatório") 
                @Size(min = 4, max = 4, message = "O código do aeroporto de origem deve ter 4 caracteres") 
                @Pattern(regexp = "^[A-Z]{4}$", message = "Código do aeroporto de origem deve conter apenas letras maiúsculas")
                @JsonProperty("origem") 
                String origin,

                @NotBlank(message = "O aeroporto de destino é obrigatório") 
                @Size(min = 4, max = 4, message = "O código do aeroporto de destino deve ter 4 caracteres") 
                @Pattern(regexp = "^[A-Z]{4}$", message = "Código do aeroporto de destino deve conter apenas letras maiúsculas")
                @JsonProperty("destino") 
                String destination,

                @NotNull(message = "A data de partida é obrigatória") 
                @JsonProperty("data_partida") 
                LocalDateTime scheduledDeparture) {
}
