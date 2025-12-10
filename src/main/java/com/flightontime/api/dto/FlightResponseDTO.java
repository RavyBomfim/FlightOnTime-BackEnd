package com.flightontime.api.dto;

public record FlightResponseDTO(
        String status,
        Double probabilidade,
        String mensagem
) {}