package com.flightontime.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.flightontime.api.security.jwt.JwtAuthenticationFilter;

@SpringBootTest
class FlightOnTimeApplicationTests {

	@MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Test
	void contextLoads() {
	}

}
