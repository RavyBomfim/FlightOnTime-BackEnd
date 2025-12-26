package com.flightontime.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OverallStats {
    private long totalFlights;
    private long delayedFlights;
    private long ontimeFlights;
    private double delayPercentage;
}
