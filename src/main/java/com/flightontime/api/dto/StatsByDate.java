package com.flightontime.api.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatsByDate {
    private LocalDateTime date;
    private long totalFlights;
    private long delayedFlights;
    private double delayPercentage;
}
