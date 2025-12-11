package com.flightontime.api.service;

import com.flightontime.api.dto.FlightRequestDTO;
import com.flightontime.api.dto.FlightResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PredictionService {

    private final RestClient pythonApiClient;

    public FlightResponseDTO predict(FlightRequestDTO flightRequestDTO) {
        Map<String, Object> payload = Map.of(
                "companhia", flightRequestDTO.companhia(),
                "origem", flightRequestDTO.origem(),
                "destino", flightRequestDTO.destino(),
                "data_partida", flightRequestDTO.data_partida().toString(),
                "distancia_km", flightRequestDTO.distancia_km());

        try {
            var response = pythonApiClient.post()
                    .uri("/predict")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(payload)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                            (request, httpResponse) -> {
                                log.error("Erro ao chamar API Python. Status: {}", httpResponse.getStatusCode());
                                throw new RuntimeException(
                                        "Erro ao processar predição: " + httpResponse.getStatusCode());
                            })
                    .body(FlightResponseDTO.class);

            return response;
        } catch (Exception e) {
            throw new RuntimeException("Serviço de predição indisponível. Tente novamente mais tarde.", e);
        }
    }
}
