package com.flightontime.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.flightontime.api.dto.FlightStatsDTO;
import com.flightontime.api.dto.OverallStats;
import com.flightontime.api.entity.Flight;
import com.flightontime.api.repository.FlightRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightService flightService;

    @Test
    void shouldGetAllFlightsOrderedByCreatedAt() {
        // Arrange
        Flight flight1 = createFlight(1L, "GOL", "SBGR", "SBBR", "Pontual");
        Flight flight2 = createFlight(2L, "AZU", "SBSP", "SBGR", "Atrasado");
        List<Flight> expectedFlights = Arrays.asList(flight2, flight1);

        when(flightRepository.findAllByOrderByCreatedAtDesc()).thenReturn(expectedFlights);

        // Act
        List<Flight> result = flightService.getAllFlights();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(2L, result.get(0).getId());
        assertEquals(1L, result.get(1).getId());
        
        verify(flightRepository, times(1)).findAllByOrderByCreatedAtDesc();
    }

    @Test
    void shouldGetFlightByIdSuccessfully() {
        // Arrange
        Long flightId = 1L;
        Flight expectedFlight = createFlight(flightId, "TAM", "SBGR", "SBBR", "Pontual");

        when(flightRepository.findById(flightId)).thenReturn(Optional.of(expectedFlight));

        // Act
        Flight result = flightService.getFlightById(flightId);

        // Assert
        assertNotNull(result);
        assertEquals(flightId, result.getId());
        assertEquals("TAM", result.getAirline());
        
        verify(flightRepository, times(1)).findById(flightId);
    }

    @Test
    void shouldThrowExceptionWhenFlightNotFound() {
        // Arrange
        Long flightId = 999L;
        when(flightRepository.findById(flightId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            flightService.getFlightById(flightId);
        });

        assertTrue(exception.getMessage().contains("Voo não encontrado com ID: 999"));
        verify(flightRepository, times(1)).findById(flightId);
    }

    @Test
    void shouldGetFlightsByOrigin() {
        // Arrange
        String origin = "SBGR";
        List<Flight> expectedFlights = Arrays.asList(
                createFlight(1L, "GOL", origin, "SBBR", "Pontual"),
                createFlight(2L, "AZU", origin, "SBSP", "Atrasado")
        );

        when(flightRepository.findByOrigin(origin)).thenReturn(expectedFlights);

        // Act
        List<Flight> result = flightService.getFlightsByOrigin(origin);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(f -> origin.equals(f.getOrigin())));
        
        verify(flightRepository, times(1)).findByOrigin(origin);
    }

    @Test
    void shouldGetFlightsByDestination() {
        // Arrange
        String destination = "SBBR";
        List<Flight> expectedFlights = Arrays.asList(
                createFlight(1L, "GOL", "SBGR", destination, "Pontual")
        );

        when(flightRepository.findByDestination(destination)).thenReturn(expectedFlights);

        // Act
        List<Flight> result = flightService.getFlightsByDestination(destination);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(destination, result.get(0).getDestination());
        
        verify(flightRepository, times(1)).findByDestination(destination);
    }

    @Test
    void shouldGetFlightsByRoute() {
        // Arrange
        String origin = "SBGR";
        String destination = "SBBR";
        List<Flight> expectedFlights = Arrays.asList(
                createFlight(1L, "GOL", origin, destination, "Pontual")
        );

        when(flightRepository.findByOriginAndDestination(origin, destination))
                .thenReturn(expectedFlights);

        // Act
        List<Flight> result = flightService.getFlightsByRoute(origin, destination);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(origin, result.get(0).getOrigin());
        assertEquals(destination, result.get(0).getDestination());
        
        verify(flightRepository, times(1)).findByOriginAndDestination(origin, destination);
    }

    @Test
    void shouldGetOntimeFlights() {
        // Arrange
        List<Flight> expectedFlights = Arrays.asList(
                createFlight(1L, "GOL", "SBGR", "SBBR", "Pontual"),
                createFlight(2L, "TAM", "SBSP", "SBGR", "Pontual")
        );

        when(flightRepository.findOntimeFlights()).thenReturn(expectedFlights);

        // Act
        List<Flight> result = flightService.getOntimeFlights();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(f -> "Pontual".equals(f.getPredictionResult())));
        
        verify(flightRepository, times(1)).findOntimeFlights();
    }

    @Test
    void shouldGetDelayedFlights() {
        // Arrange
        List<Flight> expectedFlights = Arrays.asList(
                createFlight(1L, "AZU", "SBGR", "SBSP", "Atrasado"),
                createFlight(2L, "GOL", "SBBR", "SBGR", "Atrasado")
        );

        when(flightRepository.findDelayedFlights()).thenReturn(expectedFlights);

        // Act
        List<Flight> result = flightService.getDelayedFlights();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(f -> "Atrasado".equals(f.getPredictionResult())));
        
        verify(flightRepository, times(1)).findDelayedFlights();
    }

    @Test
    void shouldDeleteFlightAndInvalidateCache() {
        // Arrange
        Long flightId = 1L;
        doNothing().when(flightRepository).deleteById(flightId);

        // Act
        flightService.deleteFlight(flightId);

        // Assert
        verify(flightRepository, times(1)).deleteById(flightId);
    }

    @Test
    void shouldGetFlightStatsSuccessfully() {
        // Arrange
        when(flightRepository.countAllFlights()).thenReturn(100L);
        when(flightRepository.countByStatus("Atrasado")).thenReturn(30L);
        when(flightRepository.countByStatus("Pontual")).thenReturn(70L);
        when(flightRepository.findStatsGroupedByDate(anyString())).thenReturn(Arrays.asList());
        when(flightRepository.findStatsGroupedByAirline(anyString())).thenReturn(Arrays.asList());
        when(flightRepository.findStatsGroupedByOrigin(anyString())).thenReturn(Arrays.asList());
        when(flightRepository.findStatsGroupedByDestination(anyString())).thenReturn(Arrays.asList());
        when(flightRepository.findStatsGroupedByRoute(anyString())).thenReturn(Arrays.asList());

        // Act
        FlightStatsDTO result = flightService.getFlightStats();

        // Assert
        assertNotNull(result);
        assertNotNull(result.getOverallStats());
        assertEquals(100L, result.getOverallStats().getTotalFlights());
        assertEquals(30L, result.getOverallStats().getDelayedFlights());
        assertEquals(70L, result.getOverallStats().getOntimeFlights());
        assertEquals(30.0, result.getOverallStats().getDelayPercentage(), 0.01);

        verify(flightRepository, times(1)).countAllFlights();
        verify(flightRepository, times(1)).countByStatus("Atrasado");
        verify(flightRepository, times(1)).countByStatus("Pontual");
    }

    @Test
    void shouldHandleEmptyFlightList() {
        // Arrange
        when(flightRepository.findAllByOrderByCreatedAtDesc()).thenReturn(Arrays.asList());

        // Act
        List<Flight> result = flightService.getAllFlights();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldCalculateCorrectDelayPercentageWhenNoFlights() {
        // Arrange
        when(flightRepository.countAllFlights()).thenReturn(0L);
        when(flightRepository.countByStatus("Atrasado")).thenReturn(0L);
        when(flightRepository.countByStatus("Pontual")).thenReturn(0L);
        when(flightRepository.findStatsGroupedByDate(anyString())).thenReturn(Arrays.asList());
        when(flightRepository.findStatsGroupedByAirline(anyString())).thenReturn(Arrays.asList());
        when(flightRepository.findStatsGroupedByOrigin(anyString())).thenReturn(Arrays.asList());
        when(flightRepository.findStatsGroupedByDestination(anyString())).thenReturn(Arrays.asList());
        when(flightRepository.findStatsGroupedByRoute(anyString())).thenReturn(Arrays.asList());

        // Act
        FlightStatsDTO result = flightService.getFlightStats();

        // Assert
        assertNotNull(result);
        assertEquals(0L, result.getOverallStats().getTotalFlights());
        assertEquals(0.0, result.getOverallStats().getDelayPercentage());
    }

    // Helper method to create Flight objects
    private Flight createFlight(Long id, String airline, String origin, String destination, String status) {
        Flight flight = new Flight();
        flight.setId(id);
        flight.setAirline(airline);
        flight.setOrigin(origin);
        flight.setDestination(destination);
        flight.setPredictionResult(status);
        flight.setScheduledDepartureDate(LocalDateTime.now());
        flight.setDistanceMeters(1000000);
        flight.setPredictionProbability(0.85);
        flight.setCreatedAt(LocalDateTime.now());
        flight.setUpdatedAt(LocalDateTime.now());
        return flight;
    }
}
