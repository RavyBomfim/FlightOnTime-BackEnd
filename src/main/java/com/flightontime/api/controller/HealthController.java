package com.flightontime.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "API FlightOnTime está ON!"; // validacao de funcionamento da API
    }
    

}
