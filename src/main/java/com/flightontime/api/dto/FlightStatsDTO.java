package com.flightontime.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightStatsDTO {
    private OverallStats overallStats;
    private List<StatsByDate> statsByDate;
    private List<StatsByAirline> statsByAirline;
    private List<StatsByOrigin> statsByOrigin;
    private List<StatsByDestination> statsByDestination;
    private List<StatsByRoute> statsByRoute;
}
