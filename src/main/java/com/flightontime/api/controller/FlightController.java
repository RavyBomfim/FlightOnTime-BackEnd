package com.flightontime.api.controller;

import com.flightontime.api.dto.FlightRequestDTO;
import com.flightontime.api.dto.FlightResponseDTO;
import com.flightontime.api.entity.Flight;
import com.flightontime.api.service.FlightService;
import com.flightontime.api.service.PredictionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
@Tag(name = "Previsão de Voos", description = "Endpoints para previsão de atrasos de voos")
public class FlightController {

    private final PredictionService predictionService;
    private final FlightService flightService;

    @PostMapping("/predict")
    @Operation(summary = "Prever atraso de voo", description = "Recebe dados de um voo e retorna a previsão de atraso com probabilidade")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Previsão realizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição"),
            @ApiResponse(responseCode = "503", description = "Serviço indisponível")
    })
    public ResponseEntity<FlightResponseDTO> predict(@RequestBody @Valid FlightRequestDTO request) {
        var response = predictionService.predict(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Listar todos os voos", description = "Retorna lista de todos os voos cadastrados")
    public ResponseEntity<List<Flight>> getAllFlights() {
        return ResponseEntity.ok(flightService.getAllFlights());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar voo por ID", description = "Retorna um voo específico pelo ID")
    public ResponseEntity<Flight> getFlightById(@PathVariable Long id) {
        return ResponseEntity.ok(flightService.getFlightById(id));
    }

    @GetMapping("/search/origin")
    @Operation(summary = "Buscar voos por origem", description = "Retorna voos que partem de uma origem específica. Exemplo: GRU, CGH")
    public ResponseEntity<List<Flight>> getFlightsByOrigin(@RequestParam String origin) {
        return ResponseEntity.ok(flightService.getFlightsByOrigin(origin));
    }

    @GetMapping("/search/destination")
    @Operation(summary = "Buscar voos por destino", description = "Retorna voos que vão para um destino específico. Exemplo: GRU, CGH")
    public ResponseEntity<List<Flight>> getFlightsByDestination(@RequestParam String destination) {
        return ResponseEntity.ok(flightService.getFlightsByDestination(destination));
    }

    @GetMapping("/search/prediction")
    @Operation(summary = "Buscar voos por resultado de predição", description = "Retorna voos com resultado específico. Valores: 'Atraso' ou 'Pontual'")
    public ResponseEntity<List<Flight>> getFlightsByPredictionResult(@RequestParam String result) {
        return ResponseEntity.ok(flightService.getFlightsByPredictionResult(result));
    }

    @GetMapping("/search/route")
    @Operation(summary = "Buscar voos por rota", description = "Retorna voos de uma origem para um destino. Exemplo: origin=GRU&destination=CGH")
    public ResponseEntity<List<Flight>> getFlightsByRoute(
            @RequestParam String origin,
            @RequestParam String destination) {
        return ResponseEntity.ok(flightService.getFlightsByRoute(origin, destination));
    }

    @GetMapping("/search/delayed")
    @Operation(summary = "Buscar voos atrasados", description = "Retorna todos os voos com predição 'Atraso'")
    public ResponseEntity<List<Flight>> getDelayedFlights() {
        return ResponseEntity.ok(flightService.getDelayedFlights());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar voo", description = "Remove um voo do banco de dados")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);
        return ResponseEntity.noContent().build();
    }
}
