package com.flightontime.api.service;

import com.flightontime.api.dto.FlightRequestDTO;
import com.flightontime.api.dto.FlightResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class PredictionService {

    public FlightResponseDTO predict(FlightRequestDTO flightRequestDTO) {
        // TODO: Chamada para python
        var dados = flightRequestDTO;

        // MOCK para a Dema
        boolean vaiAtrasar = dados.distanciaKm() > 1000;

        return new FlightResponseDTO(
                vaiAtrasar ? "Atrasado" : "Pontual",
                vaiAtrasar ? 0.85 : 0.15,
                vaiAtrasar ? "Alto risco de atraso devido à distância." : "Voo com boas chances de pontualidade"
        );
    }
}
