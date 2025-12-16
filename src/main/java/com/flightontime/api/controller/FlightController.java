package com.flightontime.api.controller;

import com.flightontime.api.dto.FlightRequestDTO;
import com.flightontime.api.dto.FlightResponseDTO;
import com.flightontime.api.service.PredictionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor

@Tag(name = "Previsão de Voos", description = "Endpoints para previsão de atrasos de voos")
public class FlightController {

    private final PredictionService service;

    @PostMapping("/predict")

    @Operation(
        summary = "Prever atraso de voo",
        description = "Recebe dados de um voo e retorna a previsão de atraso com probabilidade"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Previsão realizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição"),
        @ApiResponse(responseCode = "503", description = "Serviço indisponível")
    })

    public ResponseEntity<FlightResponseDTO> predict(@RequestBody @Valid FlightRequestDTO request) {
        var response = service.predict(request);
        return ResponseEntity.ok(response);
    }

}
