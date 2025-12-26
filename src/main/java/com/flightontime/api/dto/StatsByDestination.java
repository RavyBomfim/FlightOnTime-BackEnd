package com.flightontime.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatsByDestination {
    private String destination;
    private long totalFlights;
    private long delayedFlights;
    private double delayPercentage;
}
