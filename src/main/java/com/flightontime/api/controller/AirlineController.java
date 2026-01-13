package com.flightontime.api.controller;

import com.flightontime.api.entity.Airline;
import com.flightontime.api.service.AirlineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/airlines")
@RequiredArgsConstructor
@Tag(name = "Companhias Aéreas", description = "Endpoints para gerenciar companhias aéreas")
public class AirlineController {

    private final AirlineService airlineService;

    @GetMapping
    @Operation(summary = "Listar todas as companhias aéreas", description = "Retorna lista de todas as companhias aéreas cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso")
    })
    public ResponseEntity<List<Airline>> getAllAirlines() {
        return ResponseEntity.ok(airlineService.getAllAirlines());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar companhia aérea por ID", description = "Retorna uma companhia aérea específica pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Companhia aérea encontrada"),
            @ApiResponse(responseCode = "404", description = "Companhia aérea não encontrada")
    })
    public ResponseEntity<Airline> getAirlineById(@PathVariable Long id) {
        return ResponseEntity.ok(airlineService.getAirlineById(id));
    }
}
