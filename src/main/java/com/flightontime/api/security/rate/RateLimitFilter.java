package com.flightontime.api.security.rate;


import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j //logger
public class RateLimitFilter extends OncePerRequestFilter {

    private final Bucket bucket = Bucket.builder()
        .addLimit(Bandwidth.simple(2, Duration.ofMinutes(1)))
        .build();

        private final Map<String, Bucket> cache = new ConcurrentHashMap<>();
        //Mapa para armazenar buckets por IP (futuro aprimoramento)
        @Override
        protected void doFilterInternal(HttpServletRequest request, 
                                        HttpServletResponse response, 
                                        FilterChain filterChain)
            throws ServletException, IOException {

        // Ignora rate limiting para H2 Console e endpoints de autenticação
        String path = request.getRequestURI();
        if (path.startsWith("/h2-console") || path.startsWith("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Pega o IP 
        String clientIp = getClientIP(request);
        
        // Pega o bucket desse IP ou cria um novo se não existir
        Bucket bucket = cache.computeIfAbsent(clientIp, this::createNewBucket);

        // Tenta consumir 1 token
        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            log.warn("Bloqueado: IP {} excedeu o limite.", clientIp);
            response.setStatus(429); // Too Many Requests
            response.getWriter().write("Muitas requisicoes. Tente novamente mais tarde.");
        }
    }

    // 10 requisições por minuto 
    private Bucket createNewBucket(String ip) {
        Bandwidth limit = Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(1)));
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    // Método auxiliar para pegar o IP real (importante se usar Docker/Nginx depois)
    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}


