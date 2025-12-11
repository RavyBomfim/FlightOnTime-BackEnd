package com.flightontime.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class RestClientConfig {

    @Value("${python.api.url:http://localhost:8000}")
    private String pythonApiUrl;

    @Value("${python.api.timeout:30}")
    private int timeoutSeconds;

    @Bean
    public RestClient pythonApiClient() {
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory();
        requestFactory.setReadTimeout(Duration.ofSeconds(timeoutSeconds));

        return RestClient.builder()
                .baseUrl(pythonApiUrl)
                .requestFactory(requestFactory)
                .build();
    }
}
