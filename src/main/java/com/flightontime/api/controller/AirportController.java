package com.flightontime.api.controller;

import com.flightontime.api.entity.Airport;
import com.flightontime.api.service.AirportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/airports")
@RequiredArgsConstructor
@Tag(name = "Aeroportos", description = "Endpoints para gerenciar aeroportos")
public class AirportController {

    private final AirportService airportService;

    @GetMapping
    @Operation(summary = "Listar todos os aeroportos", description = "Retorna lista de todos os aeroportos cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso")
    })
    public ResponseEntity<List<Airport>> getAllAirports() {
        return ResponseEntity.ok(airportService.getAllAirports());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar aeroporto por ID", description = "Retorna um aeroporto específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aeroporto encontrado"),
            @ApiResponse(responseCode = "404", description = "Aeroporto não encontrado")
    })
    public ResponseEntity<Airport> getAirportById(@PathVariable Long id) {
        return ResponseEntity.ok(airportService.getAirportById(id));
    }
}
