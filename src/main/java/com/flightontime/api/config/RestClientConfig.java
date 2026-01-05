package com.flightontime.api.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

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

    @Bean
    public RestTemplate restTemplate() {
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory();
        requestFactory.setReadTimeout(Duration.ofSeconds(30));
        
        return new RestTemplate(requestFactory);
    }
}
