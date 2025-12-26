package com.flightontime.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatsByDate {
    private LocalDate date;
    private long totalFlights;
    private long delayedFlights;
    private double delayPercentage;
}
