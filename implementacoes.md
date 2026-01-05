# Implementações - FlightOnTime BackEnd

Este documento contém o status das implementações do projeto FlightOnTime-BackEnd.

---

## **Funcionalidades Implementadas ✅**

### 1. ✅ Endpoint POST /api/flights/predict

**Status:** IMPLEMENTADO

**Descrição:** Predição de atraso de voos com integração à API Python de Machine Learning

**Funcionalidades:**
- Validação de formato dos códigos (companhia: 3 caracteres, aeroportos: 4 caracteres)
- Validação de existência de companhia aérea no banco de dados
- Validação de existência de aeroportos de origem e destino no banco de dados
- Cálculo automático de distância usando fórmula de Haversine
- Integração com API Python para predição via RestClient
- Busca de dados meteorológicos do aeroporto de origem via OpenMeteo
- Persistência de todas as predições no banco H2
- Retorno de predição + probabilidade + dados climáticos

### 2. ✅ Endpoint GET /api/flights/stats

**Status:** IMPLEMENTADO

**Descrição:** Retorna estatísticas agregadas de voos

**Funcionalidades:**
- Estatísticas gerais (total de voos, atrasados, pontuais, %)
- Estatísticas por data
- Estatísticas por companhia aérea
- Estatísticas por origem
- Estatísticas por destino
- Estatísticas por rota (origem-destino)
- Cache de estatísticas para performance
- Invalidação automática de cache ao adicionar/remover voos

### 3. ✅ Persistência de Dados com JPA/Hibernate

**Status:** IMPLEMENTADO

**Detalhes:**
- Banco H2 em memória para desenvolvimento
- Entidades: Flight, Airport, Airline
- Repositories com Spring Data JPA
- Dados pré-carregados via data.sql
- Timestamps automáticos (createdAt, updatedAt)

### 4. ✅ Endpoints de Consulta

**Status:** IMPLEMENTADO

**Endpoints disponíveis:**
- `GET /api/flights` - Lista todos os voos
- `GET /api/flights/{id}` - Busca voo por ID
- `GET /api/flights/search/origin?origin={code}` - Busca por origem
- `GET /api/flights/search/destination?destination={code}` - Busca por destino
- `GET /api/flights/search/route?origin={code}&destination={code}` - Busca por rota
- `GET /api/flights/search/ontime` - Busca voos pontuais
- `GET /api/flights/search/delayed` - Busca voos atrasados
- `DELETE /api/flights/{id}` - Remove voo do banco

### 5. ✅ Integração com API externa de clima

**Status:** IMPLEMENTADO

**Detalhes:**
- Integração com OpenMeteo API
- Busca condições meteorológicas por coordenadas geográficas
- Retorna temperatura, precipitação e velocidade do vento
- Integrado na resposta do endpoint /predict

### 6. ✅ Documentação com OpenAPI/Swagger

**Status:** IMPLEMENTADO

**Detalhes:**
- Documentação interativa disponível em `/swagger-ui.html`
- Todas as APIs documentadas com descrições e exemplos
- Schemas de request/response detalhados

### 7. ✅ Containerização com Docker

**Status:** IMPLEMENTADO

**Detalhes:**
- Dockerfile multi-stage para otimização
- docker-compose.yml para orquestração
- Documentação em DOCKER_NETWORKING.md
- Health checks configurados

---

## **Funcionalidades Pendentes/Sugeridas**

### 8. Exemplos documentados no README

**Status:** EM ANDAMENTO

**Descrição:** Adicionar mais exemplos práticos de uso

**Pendente:**
- ✅ Exemplos básicos de cURL adicionados
- ✅ Exemplos PowerShell adicionados
- ✅ Exemplos JavaScript adicionados
- ✅ Exemplos Python adicionados
- ⏳ Adicionar vídeo/GIF demonstrativo
- ⏳ Adicionar Postman Collection exportada

### 9. Validação completa de contrato

**Status:** IMPLEMENTADO ✅

**Detalhes:**
- ✅ Validação de formato (tamanhos de campos)
- ✅ Validação de existência (banco de dados)
- ✅ Mensagens de erro claras e específicas
- ✅ Tipos de dados corretos
- ✅ Formato de datas ISO 8601

---

## **Funcionalidades Opcionais Sugeridas:**

### 10. Batch Prediction

**Descrição:** Adicionar 3 exemplos de uso via cURL/Postman

**Detalhes:**

- Um exemplo de voo pontual
- Um exemplo de voo atrasado
- Um exemplo com erro de validação
- Incluir requisição e resposta esperada

### 3. Validação de contrato

**Descrição:** Garantir que o formato de entrada/saída está 100% conforme especificado no projeto.md

**Detalhes:**

- Validar que todos os campos do contrato estão implementados
- Garantir tipos de dados corretos
- Validar formato de datas e campos obrigatórios

---

## **Funcionalidades Opcionais Sugeridas no Projeto:**

### 4. Persistência de Dados

**Descrição:** Implementar JPA/Hibernate para salvar histórico de previsões

**Detalhes:**

- Adicionar Spring Data JPA
- Configurar H2 para desenvolvimento
- Configurar PostgreSQL para produção
- Criar entidades: `FlightPrediction`, `PredictionHistory`
- Salvar todas as consultas realizadas com timestamp
- Endpoint para consultar histórico

**Tecnologias:**

- Spring Data JPA
- H2 Database (dev)
- PostgreSQL (prod)
- Flyway ou Liquibase para migrations

### 5. Batch Prediction

**Descrição:** Endpoint para aceitar arquivo CSV com múltiplos voos

**Detalhes:**

- Endpoint: `POST /api/flights/predict/batch`
- Aceitar upload de arquivo CSV
- Processar múltiplas previsões em lote
- Retornar arquivo CSV com resultados ou JSON
- Validar formato do arquivo

**Formato CSV esperado:**

```csv
companhia,origem,destino,data_partida,distancia_km
AZ,GIG,GRU,2025-11-10T14:30:00,350
LA,GRU,SDU,2025-11-10T15:00:00,400
```

### 6. Explicabilidade

**Descrição:** Retornar as variáveis mais importantes na decisão

**Detalhes:**

- Adicionar campo `features_importantes` na resposta
- Mostrar quais features mais impactaram a previsão
- Exemplo: "Horário da tarde e aeroporto GIG aumentam o risco"
- Integrar com SHAP ou LIME do modelo Python

**Exemplo de resposta:**

```json
{
  "previsao": "Atrasado",
  "probabilidade": 0.78,
  "features_importantes": [
    { "nome": "horario", "impacto": 0.35 },
    { "nome": "aeroporto_origem", "impacto": 0.25 },
    { "nome": "companhia", "impacto": 0.2 }
  ]
}
```

### 7. Integração com API externa de clima

**Descrição:** Adicionar condições meteorológicas como feature

**Detalhes:**

- Integrar com OpenWeatherMap, WeatherAPI ou similar
- Buscar condições climáticas para origem e destino
- Adicionar temperatura, precipitação, visibilidade ao payload
- Enviar para API Python como features adicionais

**APIs sugeridas:**

- OpenWeatherMap API
- WeatherAPI
- NOAA Weather API

### 8. Containerização Completa

**Descrição:** Melhorar Docker e docker-compose para ambiente completo

**Detalhes:**

- Testar e validar Dockerfile existente
- Melhorar docker-compose.yml para incluir:
  - Backend Java
  - API Python
  - Banco de dados PostgreSQL
  - Redis (se implementar cache)
- Configurar redes Docker conforme DOCKER_NETWORKING.md
- Adicionar health checks
- Documentar comandos de execução

**Arquivo docker-compose.yml ideal:**

```yaml
services:
  backend:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - python-api
      - postgres

  python-api:
    image: python-ml-api:latest
    ports:
      - "8000:8000"

  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: flightontime
      POSTGRES_PASSWORD: secret
```

### 9. Dashboard Visual

**Descrição:** Interface web para visualização de dados

**Detalhes:**

- Página HTML simples ou usar Thymeleaf
- Mostrar taxa de atrasos previstos em tempo real
- Gráficos de pizza, barras, linha temporal
- Top companhias/aeroportos com mais atrasos
- Filtros por data, companhia, rota

**Tecnologias sugeridas:**

- Thymeleaf + Bootstrap
- Chart.js ou ApexCharts para gráficos
- OU: SPA simples com HTML/CSS/JS vanilla

---

## **Melhorias de Qualidade e Boas Práticas:**

### 10. Testes Automatizados

**Descrição:** Implementar suite completa de testes

**Detalhes:**

**Testes Unitários:**

- `PredictionServiceTest` - testar lógica de serviço
- `FlightControllerTest` - testar endpoints
- `PredictionClientTest` - mock de chamadas HTTP

**Testes de Integração:**

- Usar `@SpringBootTest` e `MockMvc`
- Testar fluxo completo da API
- MockRestServiceServer para mockar API Python

**Ferramentas:**

- JUnit 5
- Mockito
- MockMvc
- WireMock (para mock de APIs externas)
- Jacoco (cobertura de código)

**Meta:** Alcançar 80%+ de cobertura de código

### 11. Tratamento de Exceções Customizadas

**Descrição:** Melhorar tratamento de erros com exceções específicas

**Detalhes:**

**Exceções customizadas:**

- `PredictionServiceException` - erro no serviço de predição
- `InvalidFlightDataException` - dados inválidos
- `PythonApiUnavailableException` - API Python indisponível
- `FlightNotFoundException` - voo não encontrado (se houver persistência)

**Melhorias no GlobalExceptionHandler:**

- Tratar `MethodArgumentNotValidException` (validação)
- Tratar `HttpClientErrorException` (erro 4xx da API Python)
- Tratar `ResourceAccessException` (timeout/conexão)
- Retornar JSON padronizado com formato RFC 7807 (Problem Details)

**Formato de erro padronizado:**

```json
{
  "type": "about:blank",
  "title": "Validation Error",
  "status": 400,
  "detail": "A companhia aérea é obrigatória",
  "instance": "/api/flights/predict",
  "timestamp": "2025-12-19T10:30:00Z"
}
```

### 12. Circuit Breaker/Resiliência

**Descrição:** Implementar padrões de resiliência para comunicação com API Python

**Detalhes:**

**Resilience4j:**

- Circuit Breaker - abrir circuito após N falhas consecutivas
- Retry - tentar novamente com backoff exponencial
- Rate Limiter - limitar requisições por segundo
- Timeout - configurar timeout customizado
- Fallback - retornar resposta padrão quando serviço indisponível

**Configuração exemplo:**

```properties
resilience4j.circuitbreaker.instances.pythonApi.slidingWindowSize=10
resilience4j.circuitbreaker.instances.pythonApi.failureRateThreshold=50
resilience4j.circuitbreaker.instances.pythonApi.waitDurationInOpenState=30s

resilience4j.retry.instances.pythonApi.maxAttempts=3
resilience4j.retry.instances.pythonApi.waitDuration=1s
resilience4j.retry.instances.pythonApi.exponentialBackoffMultiplier=2
```

### 13. Cache

**Descrição:** Implementar cache para previsões recentes

**Detalhes:**

- Spring Cache com Caffeine (in-memory) ou Redis (distribuído)
- Cachear previsões idênticas por X minutos
- Cache key baseado em hash dos dados do voo
- Evitar chamadas duplicadas à API Python
- Endpoint para limpar cache (admin)

**Tecnologias:**

- Spring Cache
- Caffeine (cache local)
- Redis (cache distribuído - produção)

**Exemplo:**

```java
@Cacheable(value = "predictions", key = "#flightRequestDTO.hashCode()")
public FlightResponseDTO predict(FlightRequestDTO flightRequestDTO) {
    // ...
}
```

### 14. Observabilidade

**Descrição:** Adicionar métricas, logs e rastreamento

**Detalhes:**

**Logging:**

- Logging estruturado (JSON) com Logback
- Diferentes níveis por ambiente (DEBUG em dev, INFO em prod)
- Adicionar trace IDs para correlação de logs

**Métricas:**

- Métricas customizadas no Actuator
- Contador de previsões realizadas
- Tempo médio de resposta
- Taxa de erro da API Python
- Gauge para circuit breaker status

**Rastreamento:**

- Spring Cloud Sleuth para trace IDs
- Integração com Zipkin/Jaeger (opcional)

**Monitoramento:**

- Prometheus para coleta de métricas
- Grafana para dashboards

### 15. Segurança

**Descrição:** Implementar camada de segurança

**Detalhes:**

**Spring Security:**

- Autenticação via JWT ou API Key
- Diferentes perfis (USER, ADMIN)
- Endpoint /stats protegido (apenas ADMIN)

**Rate Limiting:**

- Limitar requisições por IP/usuário
- Prevenir abuso e ataques DDoS
- Bucket4j ou Spring Cloud Gateway

**CORS:**

- Configuração mais restritiva em produção
- Permitir apenas origens conhecidas
- Configurar headers de segurança

**Outras medidas:**

- HTTPS obrigatório em produção
- Validação de entrada robusta
- Sanitização de dados
- Headers de segurança (CSP, X-Frame-Options, etc.)

### 16. Validações Avançadas

**Descrição:** Validações customizadas mais robustas

**Detalhes:**

**Validações a implementar:**

- Códigos IATA de aeroportos (3 letras maiúsculas)
- Códigos de companhias aéreas válidos
- Data de partida não pode ser no passado
- Distância entre 1 e 20.000 km (razoável)
- Origem diferente de destino

**Anotações customizadas:**

```java
@IATACode
String origem;

@AirlineCode
String companhia;

@FutureOrPresent
LocalDateTime data_partida;

@ValidRoute
// validar se rota origem-destino existe
```

### 17. Documentação da API

**Descrição:** Melhorar documentação Swagger/OpenAPI

**Detalhes:**

- Swagger/OpenAPI já está implementado
- Adicionar mais exemplos de requisição/resposta
- Documentar todos os códigos de erro (400, 404, 500, 503)
- Adicionar descrições detalhadas de cada campo
- Exemplos de payloads inválidos
- Documentar headers necessários
- Adicionar informações de autenticação (quando implementada)

### 18. Health Checks

**Descrição:** Health checks customizados

**Detalhes:**

- Endpoint `/actuator/health` customizado
- Verificar conectividade com API Python
- Verificar conexão com banco de dados
- Verificar disponibilidade de APIs externas
- Readiness probe (pronto para receber tráfego)
- Liveness probe (aplicação está viva)

**Para Kubernetes/Cloud:**

```yaml
livenessProbe:
  httpGet:
    path: /actuator/health/liveness
    port: 8080

readinessProbe:
  httpGet:
    path: /actuator/health/readiness
    port: 8080
```

### 19. Configuração por Ambiente

**Descrição:** Profiles Spring para diferentes ambientes

**Detalhes:**

**Profiles:**

- `application-dev.properties` - desenvolvimento local
- `application-test.properties` - ambiente de testes
- `application-prod.properties` - produção

**Configurações externalizadas:**

- Variáveis de ambiente para dados sensíveis
- ConfigMaps no Kubernetes
- AWS Parameter Store / Azure Key Vault
- Não commitar senhas/tokens no Git

**Exemplo:**

```properties
# application-prod.properties
spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
python.api.url=${PYTHON_API_URL}
```

### 20. Pipeline CI/CD

**Descrição:** Automação de build, teste e deploy

**Detalhes:**

**GitHub Actions:**

- Build automático em cada push
- Executar testes unitários e integração
- Análise de código com SonarQube
- Build de imagem Docker
- Push para Docker Hub/Registry
- Deploy automático para OCI (Oracle Cloud)

**Stages do pipeline:**

1. Checkout código
2. Setup Java 17
3. Build com Maven
4. Executar testes
5. Análise de código
6. Build Docker image
7. Push para registry
8. Deploy para ambiente (dev/staging/prod)

**Exemplo workflow:**

```yaml
name: CI/CD Pipeline

on:
  push:
    branches: [main, develop]
  pull_request:
    branches: [main]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: "17"
      - name: Build with Maven
        run: ./mvnw clean install
      - name: Run tests
        run: ./mvnw test
```

---

## **Melhorias de Arquitetura:**

### 21. DTOs mais ricos

**Descrição:** Enriquecer DTOs com validações e documentação

**Detalhes:**

- Validações customizadas com mensagens em português
- Swagger annotations detalhadas
- Builder pattern para construção
- Métodos auxiliares (toEntity, fromEntity)
- Validações cross-field (ex: data_partida > now())

### 22. Versionamento de API

**Descrição:** Implementar versionamento para evolução da API

**Detalhes:**

- Versionar endpoints: `/api/v1/flights/predict`
- Manter compatibilidade com versões antigas
- Deprecation strategy clara
- Documentar mudanças entre versões
- Headers de versionamento alternativos

### 23. Feature Flags

**Descrição:** Controle de funcionalidades via feature flags

**Detalhes:**

- Habilitar/desabilitar features sem deploy
- Useful para A/B testing
- Rollout gradual de novas funcionalidades
- Togglz ou FF4J para Java
- ConfigMap/environment variables

### 24. Auditoria

**Descrição:** Registrar todas as operações para auditoria

**Detalhes:**

- @CreatedDate, @CreatedBy, @LastModifiedDate, @LastModifiedBy
- Spring Data JPA Auditing
- Registrar IP do cliente
- Timestamp de todas as operações
- Histórico de alterações (audit log)
- GDPR compliance (se aplicável)

### 25. Compressão de Resposta

**Descrição:** Habilitar compressão GZIP para melhorar performance

**Detalhes:**

- Habilitar compressão GZIP no Spring Boot
- Reduzir tamanho das respostas HTTP
- Melhorar performance de rede
- Especialmente útil para batch predictions

**Configuração:**

```properties
server.compression.enabled=true
server.compression.mime-types=application/json,application/xml,text/html,text/xml,text/plain
server.compression.min-response-size=1024
```

---

## **Priorização Sugerida:**

### **Fase 1 - MVP (Essencial):**

1. Exemplos documentados no README
2. Validação de contrato
3. Testes Automatizados básicos
4. Tratamento de Exceções Customizadas

### **Fase 2 - Estabilidade:**

5. Circuit Breaker/Resiliência
6. Health Checks
7. Configuração por Ambiente
8. Validações Avançadas

### **Fase 3 - Features:**

9. Endpoint GET /stats
10. Persistência de Dados
11. Cache
12. Observabilidade

### **Fase 4 - Escalabilidade:**

13. Containerização Completa
14. Pipeline CI/CD
15. Segurança
16. Compressão de Resposta

### **Fase 5 - Valor Agregado:**

17. Batch Prediction
18. Dashboard Visual
19. Explicabilidade
20. Integração com API de clima

---

## **Estimativa de Esforço:**

| Implementação    | Complexidade | Tempo Estimado |
| ---------------- | ------------ | -------------- |
| Endpoint /stats  | Baixa        | 4-8 horas      |
| Exemplos README  | Baixa        | 1-2 horas      |
| Testes Unitários | Média        | 16-24 horas    |
| Persistência JPA | Média        | 16-24 horas    |
| Circuit Breaker  | Média        | 8-12 horas     |
| Cache            | Baixa        | 4-8 horas      |
| Segurança JWT    | Alta         | 16-32 horas    |
| Batch Prediction | Média        | 12-16 horas    |
| Dashboard        | Alta         | 24-40 horas    |
| CI/CD Pipeline   | Média        | 8-16 horas     |

---

## **Tecnologias Adicionais Recomendadas:**

- **Resilience4j** - Circuit breaker, retry, rate limiter
- **Caffeine** ou **Redis** - Cache
- **PostgreSQL** - Banco de dados produção
- **Flyway** ou **Liquibase** - Database migrations
- **Jacoco** - Cobertura de código
- **WireMock** - Mock de APIs externas
- **Testcontainers** - Testes de integração com containers
- **Prometheus** + **Grafana** - Monitoramento
- **SonarQube** - Análise de qualidade de código
- **Docker** + **Kubernetes** - Containerização e orquestração

---

## **Conclusão:**

Este documento apresenta um roadmap completo de evolução do projeto FlightOnTime-BackEnd, desde funcionalidades básicas do MVP até features avançadas de produção. A implementação pode ser feita de forma incremental, priorizando primeiro as funcionalidades essenciais e depois evoluindo para features mais complexas.

**Última atualização:** 19 de Dezembro de 2025
