package com.flightontime.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatsByAirline {
    private String airline;
    private long totalFlights;
    private long delayedFlights;
    private double delayPercentage;
}
