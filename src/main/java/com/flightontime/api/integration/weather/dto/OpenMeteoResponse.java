package com.flightontime.api.integration.weather.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenMeteoResponse {
    
    private double latitude;
    private double longitude;
    
    @JsonProperty("current")
    private CurrentWeather current;
    
    @JsonProperty("hourly")
    private HourlyWeather hourly;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CurrentWeather {
        
        @JsonProperty("time")
        private String time;
        
        @JsonProperty("temperature_2m")
        private double temperature2m;
        
        @JsonProperty("precipitation")
        private double precipitation;
        
        @JsonProperty("wind_speed_10m")
        private double windSpeed10m;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HourlyWeather {
        
        @JsonProperty("time")
        private List<String> time;
        
        @JsonProperty("temperature_2m")
        private List<Double> temperature2m;
        
        @JsonProperty("precipitation")
        private List<Double> precipitation;
        
        @JsonProperty("wind_speed_10m")
        private List<Double> windSpeed10m;
    }
}
