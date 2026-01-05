# Arquitetura do Projeto - FlightOnTime BackEnd

## 📋 Visão Geral

O FlightOnTime BackEnd é uma API REST desenvolvida em Java/Spring Boot que fornece predições de atrasos de voos utilizando Machine Learning. O projeto segue uma arquitetura em camadas com separação clara de responsabilidades.

---

## 🏗️ Arquitetura em Camadas

```
┌─────────────────────────────────────────────┐
│          CONTROLLER LAYER                    │
│  - FlightController                          │
│  - Recebe requisições HTTP                   │
│  - Validação de entrada (@Valid)             │
│  - Retorna ResponseEntity<DTO>               │
└──────────────────┬──────────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────────┐
│           SERVICE LAYER                      │
│  - PredictionService                         │
│  - FlightService                             │
│  - WeatherService                            │
│  - Lógica de negócio                         │
│  - Orquestração de operações                 │
└──────────────────┬──────────────────────────┘
                   │
      ┌────────────┴────────────┐
      ▼                         ▼
┌─────────────────┐   ┌─────────────────────┐
│ INTEGRATION     │   │  REPOSITORY LAYER   │
│  - PredictionClient │  - FlightRepository  │
│  - WeatherClient    │  - AirportRepository │
│  - RestClient       │  - AirlineRepository │
│  - APIs Externas    │  - Spring Data JPA   │
└─────────────────┘   └──────────┬──────────┘
                                  │
                                  ▼
                        ┌──────────────────┐
                        │  ENTITY LAYER    │
                        │  - Flight        │
                        │  - Airport       │
                        │  - Airline       │
                        │  - JPA Entities  │
                        └──────────────────┘
```

---

## 📦 Estrutura de Pacotes

```
com.flightontime.api/
│
├── config/                    # Configurações do Spring
│   ├── CorsConfig            # Configuração CORS
│   ├── H2ConsoleConfig       # Console H2
│   ├── OpenApiConfig         # Swagger/OpenAPI
│   └── RestClientConfig      # HTTP Clients
│
├── controller/               # Controllers REST
│   └── FlightController      # Endpoints da API
│
├── dto/                      # Data Transfer Objects
│   ├── FlightRequestDTO      # Request de predição
│   ├── FlightResponseDTO     # Response de predição
│   ├── PredictionDTO         # Dados de predição
│   ├── WeatherDTO           # Dados meteorológicos
│   └── FlightStatsDTO       # Estatísticas
│
├── entity/                   # Entidades JPA
│   ├── Flight               # Voo
│   ├── Airport              # Aeroporto
│   └── Airline              # Companhia Aérea
│
├── repository/               # Repositórios Spring Data
│   ├── FlightRepository
│   ├── AirportRepository
│   └── AirlineRepository
│
├── service/                  # Serviços de Negócio
│   ├── PredictionService    # Orquestra predição
│   ├── FlightService        # CRUD e estatísticas
│   └── WeatherService       # Dados climáticos
│
├── integration/              # Integrações Externas
│   ├── prediction/          # API Python ML
│   │   ├── PredictionClient
│   │   └── dto/
│   │       ├── PredictionRequest
│   │       └── PredictionResponse
│   └── weather/             # API OpenMeteo
│       ├── WeatherClient
│       └── dto/
│
└── exception/               # Tratamento de Erros
    └── GlobalExceptionHandler
```

---

## 🔄 Fluxo de Predição Detalhado

### 1. Requisição do Cliente

```http
POST /api/flights/predict
Content-Type: application/json

{
  "companhia": "GOL",
  "origem": "SBGR",
  "destino": "SBBR",
  "data_partida": "2025-12-15T14:30:00"
}
```

### 2. Processamento no Backend

```
┌──────────────────────────────────────────────────────┐
│  1. FlightController.predict()                       │
│     - Valida formato (@Valid)                        │
│     - Passa para PredictionService                   │
└──────────────────┬───────────────────────────────────┘
                   │
                   ▼
┌──────────────────────────────────────────────────────┐
│  2. PredictionService.predict()                      │
│                                                       │
│  2.1 Validar Companhia Aérea                        │
│      ↓                                                │
│      AirlineRepository.findByAirlineCode("GOL")      │
│      Se null → RuntimeException                      │
│                                                       │
│  2.2 Validar Aeroporto de Origem                    │
│      ↓                                                │
│      AirportRepository.findByAirportCode("SBGR")    │
│      Se null → RuntimeException                      │
│                                                       │
│  2.3 Validar Aeroporto de Destino                   │
│      ↓                                                │
│      AirportRepository.findByAirportCode("SBBR")    │
│      Se null → RuntimeException                      │
│                                                       │
│  2.4 Calcular Distância                             │
│      ↓                                                │
│      calculateDistanceKm(origin, destination)        │
│      - Fórmula de Haversine                         │
│      - Baseado em lat/long dos aeroportos           │
│      - Retorna distância em km (int)                │
│                                                       │
│  2.5 Preparar Payload                               │
│      ↓                                                │
│      new PredictionRequest(                          │
│          companhia, origem, destino,                 │
│          data, dia_semana, distancia_km)            │
└──────────────────┬───────────────────────────────────┘
                   │
                   ▼
┌──────────────────────────────────────────────────────┐
│  3. PredictionClient.predict()                       │
│     - RestClient.post()                              │
│     - URL: http://localhost:8000/predict            │
│     - Timeout: 30s                                   │
│     - Tratamento de erros 4xx/5xx                   │
└──────────────────┬───────────────────────────────────┘
                   │
                   ▼
┌──────────────────────────────────────────────────────┐
│  4. API Python (FastAPI)                             │
│     - Recebe dados do voo                            │
│     - Executa modelo de ML (scikit-learn)           │
│     - Retorna predição + probabilidade              │
└──────────────────┬───────────────────────────────────┘
                   │
                   ▼
┌──────────────────────────────────────────────────────┐
│  5. WeatherService.getWeatherForAirport()           │
│     - WeatherClient.getWeatherForecast()            │
│     - URL: https://api.open-meteo.com               │
│     - Parâmetros: lat, long, datetime               │
│     - Retorna: temp, precipitação, vento            │
└──────────────────┬───────────────────────────────────┘
                   │
                   ▼
┌──────────────────────────────────────────────────────┐
│  6. Persistência                                     │
│     - Cria entidade Flight                           │
│     - Seta todos os campos                           │
│     - FlightRepository.save(flight)                 │
│     - Retorna Flight com ID gerado                  │
└──────────────────┬───────────────────────────────────┘
                   │
                   ▼
┌──────────────────────────────────────────────────────┐
│  7. Resposta ao Cliente                              │
│     - FlightResponseDTO(                             │
│         predict: {previsao, probabilidade},         │
│         weather: {temp, precip, vento}              │
│       )                                              │
│     - HTTP 200 OK                                    │
└──────────────────────────────────────────────────────┘
```

---

## 🧮 Cálculo de Distância (Haversine)

### Fórmula Implementada

```java
private double calculateDistanceKm(Airport origin, Airport destination) {
    final int EARTH_RADIUS_KM = 6371;
    
    // Converter graus para radianos
    double latDistance = Math.toRadians(
        destination.getAirportLatitude() - origin.getAirportLatitude()
    );
    double lonDistance = Math.toRadians(
        destination.getAirportLongitude() - origin.getAirportLongitude()
    );
    
    // Fórmula de Haversine
    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
            + Math.cos(Math.toRadians(origin.getAirportLatitude()))
            * Math.cos(Math.toRadians(destination.getAirportLatitude()))
            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
    
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    
    // Distância em km
    return EARTH_RADIUS_KM * c;
}
```

### Exemplo de Cálculo

```
Origem: SBGR (Guarulhos)
- Latitude: -23.4356
- Longitude: -46.4731

Destino: SBBR (Brasília)
- Latitude: -15.8711
- Longitude: -47.9189

Distância Calculada: ~873 km
```

---

## 🗄️ Modelo de Dados

### Entidade Flight

```java
@Entity
@Table(name = "flights")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String airline;              // Código da companhia (3 chars)
    private String origin;               // Código ICAO origem (4 chars)
    private String destination;          // Código ICAO destino (4 chars)
    private LocalDate scheduledDepartureDate;
    private int dayOfWeek;               // 1-7 (Segunda a Domingo)
    private int distanceKm;              // Distância calculada
    private String predictionResult;     // "Pontual" ou "Atrasado"
    private Double predictionProbability; // 0.0 - 1.0
    private LocalDateTime createdAt;     // Timestamp criação
    private LocalDateTime updatedAt;     // Timestamp atualização
}
```

### Entidade Airport

```java
@Entity
@Table(name = "airports")
public class Airport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String airportCode;          // ICAO code (4 chars)
    private String airportName;          // Nome completo
    private String airportCity;          // Cidade
    private String airportState;         // Estado/UF
    private Double airportLatitude;      // Coordenada geográfica
    private Double airportLongitude;     // Coordenada geográfica
}
```

### Entidade Airline

```java
@Entity
@Table(name = "airlines")
public class Airline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String airlineCode;          // Código IATA (3 chars)
    private String airlineName;          // Nome completo
}
```

---

## 🔌 Integrações Externas

### 1. API Python (Machine Learning)

**Base URL:** `http://localhost:8000`

**Endpoint:** `POST /predict`

**Request:**
```json
{
  "companhia": "GOL",
  "origem": "SBGR",
  "destino": "SBBR",
  "data_partida": "2025-12-15",
  "dia_semana": 3,
  "distancia_km": 873
}
```

**Response:**
```json
{
  "previsao": true,
  "probabilidade": 0.78
}
```

### 2. OpenMeteo API (Meteorologia)

**Base URL:** `https://api.open-meteo.com/v1/forecast`

**Parâmetros:**
- latitude
- longitude
- hourly=temperature_2m,precipitation,wind_speed_10m

**Response:**
```json
{
  "current": {
    "temperature_2m": 25.5,
    "precipitation": 0.0,
    "wind_speed_10m": 12.5
  }
}
```

---

## ⚙️ Configurações

### application.properties

```properties
# Servidor
server.port=8080

# API Python
python.api.url=http://localhost:8000
python.api.timeout=30

# API Clima
weather.api.url=https://api.open-meteo.com/v1/forecast

# Banco H2
spring.datasource.url=jdbc:h2:mem:flightdb
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# Swagger
springdoc.swagger-ui.path=/swagger-ui.html
```

---

## 🎯 Endpoints da API

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/flights/predict` | Predição de atraso |
| GET | `/api/flights` | Lista todos os voos |
| GET | `/api/flights/{id}` | Busca voo por ID |
| GET | `/api/flights/stats` | Estatísticas agregadas |
| GET | `/api/flights/search/origin` | Busca por origem |
| GET | `/api/flights/search/destination` | Busca por destino |
| GET | `/api/flights/search/route` | Busca por rota |
| GET | `/api/flights/search/ontime` | Voos pontuais |
| GET | `/api/flights/search/delayed` | Voos atrasados |
| DELETE | `/api/flights/{id}` | Remove voo |

---

## 🚀 Decisões de Design

### 1. Cálculo Automático de Distância
**Decisão:** Remover `distancia_km` do request e calcular automaticamente.

**Motivo:**
- Simplifica a API para o cliente
- Elimina possibilidade de erro humano
- Garante precisão usando coordenadas reais
- Reduz campos obrigatórios

### 2. Validação Antecipada
**Decisão:** Validar aeroportos e companhias **antes** de chamar API Python.

**Motivo:**
- Fail-fast: detecta erros imediatamente
- Economia de recursos: não processa dados inválidos
- Melhor experiência do usuário: mensagens de erro claras
- Reduz carga na API de ML

### 3. Separação de Responsabilidades
**Decisão:** Cada serviço tem uma responsabilidade única.

**Motivo:**
- PredictionService: orquestra o fluxo
- FlightService: CRUD e estatísticas
- WeatherService: apenas dados climáticos
- Facilita manutenção e testes

### 4. DTOs para Comunicação
**Decisão:** Usar DTOs ao invés de expor entidades.

**Motivo:**
- Controle sobre dados expostos
- Facilita versionamento da API
- Desacopla modelo de domínio da API
- Permite transformações customizadas

---

## 📊 Performance e Cache

### Cache de Estatísticas

```java
@Cacheable(value = "flightStats")
public FlightStatsDTO getFlightStats() {
    // Cálculos pesados em memória
}

@CacheEvict(value = "flightStats", allEntries = true)
public FlightResponseDTO predict(...) {
    // Invalida cache ao adicionar novo voo
}
```

### Queries Otimizadas

- Uso de projeções para reduzir dados carregados
- Queries agregadas no banco (COUNT, GROUP BY)
- Evita carregar todos os voos em memória

---

## 🔐 Segurança

### CORS
- Configurado para permitir origens específicas
- Headers permitidos para APIs REST

### Validação
- Bean Validation (@Valid, @NotNull, @Size)
- Validação de existência no banco
- Sanitização de inputs

---

## 📝 Logs

### Níveis de Log

- **INFO:** Operações principais (predição recebida, voo salvo)
- **DEBUG:** Detalhes técnicos (payload enviado, distância calculada)
- **ERROR:** Erros e exceções (aeroporto não encontrado, falha na API)

### Exemplo de Logs

```log
INFO  - Recebendo requisição de predição: FlightRequestDTO(...)
DEBUG - Aeroportos validados - Origem: Guarulhos, Destino: Brasília
DEBUG - Distância calculada entre aeroportos: 873 km
DEBUG - Enviando payload para API Python: PredictionRequest(...)
INFO  - Predição recebida: PredictionResponse(previsao=true, probabilidade=0.78)
INFO  - Voo salvo no banco de dados com ID: 42
```

---

## 🧪 Testabilidade

A arquitetura facilita testes em todos os níveis:

- **Unitários:** Testar métodos isolados (ex: calculateDistanceKm)
- **Integração:** Testar repositories com banco H2
- **E2E:** Testar controllers com MockMvc
- **Mocks:** Facilita mock de serviços externos

---

## 📚 Documentação

- **README.md:** Guia de instalação e uso
- **STATUS.md:** Status de implementação
- **CHANGELOG.md:** Histórico de mudanças
- **ARQUITETURA.md:** Este documento
- **Swagger UI:** Documentação interativa da API
