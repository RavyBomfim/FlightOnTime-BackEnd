# ✈️ Flight on Time - Backend API

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

API REST desenvolvida em Spring Boot para **predição inteligente de atrasos de voos** utilizando Machine Learning. Este projeto faz parte do hackaton da Oracle Next Education (ONE) e oferece uma solução completa para análise preditiva de pontualidade de voos com integração a dados meteorológicos em tempo real.

## 🎯 Sobre o Projeto

O FlightOnTime resolve um problema crítico na aviação: **prever atrasos de voos antes que eles aconteçam**. Com base em dados históricos, características do voo e condições meteorológicas, a API fornece predições precisas que ajudam:

- ✈️ **Passageiros**: Planejar melhor suas viagens e conexões
- 🏢 **Companhias Aéreas**: Otimizar operações e reduzir custos
- 🛫 **Aeroportos**: Gerenciar recursos e infraestrutura com eficiência

### 🌟 Diferenciais

- 🤖 **Machine Learning Avançado**: Modelo treinado com dados históricos reais
- 🌤️ **Dados Meteorológicos**: Integração em tempo real com OpenMeteo
- 📊 **Estatísticas Completas**: Análise detalhada por companhia, rota, aeroporto e data
- 🔐 **Segurança**: Autenticação JWT e rate limiting
- 📈 **Alta Performance**: Cache inteligente e otimizações de banco de dados
- 📚 **Documentação Completa**: Swagger/OpenAPI integrado

## 🚀 Tecnologias Utilizadas

### Backend
- **Java 21** - Linguagem de programação
- **Spring Boot 3.2.5** - Framework principal
- **Spring Security + JWT** - Autenticação e autorização
- **Spring Data JPA** - Persistência de dados
- **Hibernate** - ORM
- **H2 Database** - Banco de dados em memória (desenvolvimento)

### Ferramentas e Bibliotecas
- **Maven** - Gerenciamento de dependências
- **Lombok** - Redução de boilerplate
- **Bean Validation** - Validação de dados com regex
- **RestClient** - Comunicação HTTP com APIs externas
- **Bucket4j** - Rate limiting
- **SpringDoc OpenAPI** - Documentação automática
- **JaCoCo** - Cobertura de testes (58% nos serviços)

### Integrações Externas
- **FastAPI Python** - Modelo de Machine Learning
- **OpenMeteo API** - Dados meteorológicos em tempo real

## 📋 Pré-requisitos

- **Java 21** ou superior
- **Maven 3.6+**
- **API Python de Machine Learning** rodando na porta 8000 (obrigatório)

## ⚠️ IMPORTANTE: Configurar API Python de Machine Learning

Este backend **depende obrigatoriamente** da API Python que contém o modelo de Machine Learning treinado. Sem ela, as predições não funcionarão.

### Passo 1: Clone o repositório da API Python

```bash
git clone https://github.com/RavyBomfim/FlightOnTime-DataScience.git
cd FlightOnTime-DataScience/API
```

### Passo 2: Configure e inicie a API Python

Siga as instruções detalhadas no README do projeto Python para:
- Instalar dependências (FastAPI, scikit-learn, pandas, etc.)
- Configurar o ambiente virtual
- Carregar o modelo treinado
- Iniciar o servidor na porta 8000

**📚 Link do Projeto:** https://github.com/RavyBomfim/FlightOnTime-DataScience/tree/main/API

### Passo 3: Verifique se a API está rodando

```bash
# Teste básico
curl http://localhost:8000

# Deve retornar informações sobre a API
```

⚠️ **Atenção**: Não prossiga sem a API Python funcionando!

## 🔧 Instalação e Execução

### 1️⃣ Certifique-se que a API Python está rodando

```bash
# Verifique se está respondendo
curl http://localhost:8000
```

### 2️⃣ Clone o repositório

```bash
git clone https://github.com/seu-usuario/FlightOnTime-BackEnd.git
cd FlightOnTime-BackEnd
```

### 3️⃣ Compile e execute

**Windows (PowerShell):**
```powershell
# Compilar
.\mvnw.cmd clean install

# Executar
.\mvnw.cmd spring-boot:run
```

**Linux/Mac:**
```bash
# Compilar
./mvnw clean install

# Executar
./mvnw spring-boot:run
```

### 4️⃣ Acesse a aplicação

- **API Backend**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:flightdb`
  - Username: `sa`
  - Password: _(vazio)_

## 🔐 Autenticação e Segurança

A API utiliza **JWT (JSON Web Token)** para autenticação. Todos os endpoints de voos são protegidos.

### 🔓 Endpoints Públicos (Sem Autenticação)

- `POST /api/auth/register` - Registrar novo usuário
- `POST /api/auth/login` - Fazer login e obter token
- `GET /h2-console/**` - Console do banco H2 (apenas dev)
- `GET /swagger-ui.html` - Documentação interativa

### 🔒 Endpoints Protegidos (Requerem JWT)

Todos os endpoints `/api/flights/**` requerem token JWT no header `Authorization`.

### Fluxo de Autenticação

#### 1. Registrar um Usuário

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "usuario@email.com",
    "password": "senha123"
  }'
```

**Response**: `200 OK`
```json
"Usuário registrado com sucesso!"
```

#### 2. Fazer Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "usuario@email.com",
    "password": "senha123"
  }'
```

**Response**: `200 OK`
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c3VhcmlvQGVtYWlsLmNvbSIsImlhdCI6MTcwNjk5OTk5OX0.signature"
}
```

#### 3. Usar o Token

Adicione o token no header `Authorization` com prefixo `Bearer`:

```bash
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### ⏱️ Validade do Token

- **Duração**: 1 hora após login
- **Renovação**: Faça login novamente para obter novo token

### 🛡️ Recursos de Segurança

- ✅ **Senhas criptografadas** com BCrypt
- ✅ **Rate Limiting**: 100 requisições/minuto por IP
- ✅ **CORS configurado** para origens específicas
- ✅ **Validação de entrada** com regex e Bean Validation

## 📡 Endpoints da API

### 🔮 Predição de Voos

#### POST /api/flights/predict

**Realiza predição de atraso de voo com Machine Learning**

🔒 **Requer autenticação JWT**

**Request:**
```json
{
  "companhia": "G3",
  "origem": "SBGR",
  "destino": "SBBR",
  "data_partida": "2026-01-15T14:30:00"
}
```

**Campos:**
- `companhia` (String, 2-3 chars): Código IATA da companhia (G3, LA, AD, etc.)
- `origem` (String, 4 chars): Código ICAO do aeroporto de origem (SBGR, SBSP, etc.)
- `destino` (String, 4 chars): Código ICAO do aeroporto de destino
- `data_partida` (DateTime): Data/hora de partida no formato ISO 8601

**Response:**
```json
{
  "predict": {
    "predictionResult": "Atrasado",
    "predictionProbability": 0.78
  },
  "weather": {
    "temperature": "25.5°C",
    "precipitation": "2.0mm",
    "windSpeed": "15.0 km/h"
  }
}
```

**Validações automáticas:**
- ✅ Formato de códigos (regex validation)
- ✅ Existência da companhia aérea no banco
- ✅ Existência dos aeroportos no banco
- ✅ **Cálculo automático de distância** (Haversine)
- ✅ Dados meteorológicos em tempo real

---

### 🗂️ Consulta de Voos

#### GET /api/flights

Lista todos os voos registrados (ordenados por data de criação).

🔒 **Requer autenticação JWT**

**Response:**
```json
[
  {
    "id": 123,
    "airline": "G3",
    "origin": "SBGR",
    "destination": "SBBR",
    "distanceMeters": 872000,
    "scheduledDeparture": "2026-01-15T14:30:00",
    "predictionResult": "Atrasado",
    "predictionProbability": 0.78,
    "createdAt": "2026-01-13T10:30:00",
    "updatedAt": "2026-01-13T10:30:00"
  }
]
```

#### GET /api/flights/{id}

Busca voo específico por ID.

#### GET /api/flights/search/origin?origin={code}

Busca voos por aeroporto de origem.

**Exemplo:** `/api/flights/search/origin?origin=SBGR`

#### GET /api/flights/search/destination?destination={code}

Busca voos por aeroporto de destino.

**Exemplo:** `/api/flights/search/destination?destination=SBBR`

#### GET /api/flights/search/route?origin={code}&destination={code}

Busca voos por rota específica (origem + destino).

**Exemplo:** `/api/flights/search/route?origin=SBGR&destination=SBBR`

#### GET /api/flights/search/ontime

Retorna todos os voos com predição de **pontualidade**.

#### GET /api/flights/search/delayed

Retorna todos os voos com predição de **atraso**.

#### DELETE /api/flights/{id}

Remove um voo do banco de dados.

**Response:** `204 No Content`

---

### 📊 Estatísticas e Análises

#### GET /api/flights/stats

**Retorna estatísticas completas agregadas de todos os voos**

🔒 **Requer autenticação JWT**

**Response:**
```json
{
  "overall": {
    "totalFlights": 150,
    "totalDelayed": 45,
    "totalOnTime": 105,
    "delayRate": 30.0
  },
  "byDate": [
    {
      "date": "2026-01-15",
      "totalFlights": 25,
      "delayedFlights": 8,
      "delayRate": 32.0
    }
  ],
  "byAirline": [
    {
      "airline": "G3",
      "totalFlights": 50,
      "delayedFlights": 15,
      "delayRate": 30.0
    }
  ],
  "byOrigin": [
    {
      "origin": "SBGR",
      "totalFlights": 40,
      "delayedFlights": 12,
      "delayRate": 30.0
    }
  ],
  "byDestination": [
    {
      "destination": "SBBR",
      "totalFlights": 35,
      "delayedFlights": 10,
      "delayRate": 28.5
    }
  ],
  "byRoute": [
    {
      "route": "SBGR-SBBR",
      "totalFlights": 20,
      "delayedFlights": 6,
      "delayRate": 30.0
    }
  ]
}
```

**Análises disponíveis:**
- 📈 Estatísticas gerais (total, atrasados, pontuais, taxa de atraso)
- 📅 Análise por data
- ✈️ Análise por companhia aérea
- 🛫 Análise por aeroporto de origem
- 🛬 Análise por aeroporto de destino
- 🛤️ Análise por rota completa

**Performance:** Cache automático para respostas rápidas

---

### 🏢 Consulta de Companhias e Aeroportos

#### GET /api/airlines

Lista todas as companhias aéreas cadastradas.

**Response:**
```json
[
  {
    "id": 1,
    "airlineCode": "G3",
    "airlineName": "Gol Linhas Aéreas"
  },
  {
    "id": 2,
    "airlineCode": "LA",
    "airlineName": "LATAM Airlines"
  }
]
```

#### GET /api/airlines/{code}

Busca companhia aérea por código.

#### GET /api/airports

Lista todos os aeroportos cadastrados.

**Response:**
```json
[
  {
    "id": 1,
    "airportCode": "SBGR",
    "airportName": "Aeroporto Internacional de Guarulhos",
    "airportCity": "São Paulo",
    "airportState": "SP",
    "latitude": -23.432075,
    "longitude": -46.469511
  }
]
```

#### GET /api/airports/{code}

Busca aeroporto por código ICAO.

## 🧪 Exemplos Práticos

### 1️⃣ Registrar e Fazer Login

**cURL:**
```bash
# Registrar
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email": "usuario@email.com", "password": "senha123"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "usuario@email.com", "password": "senha123"}'
```

**PowerShell:**
```powershell
# Registrar
$body = @{email = "usuario@email.com"; password = "senha123"} | ConvertTo-Json
Invoke-RestMethod -Uri "http://localhost:8080/api/auth/register" -Method POST -ContentType "application/json" -Body $body

# Login e salvar token
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method POST -ContentType "application/json" -Body $body
$token = $response.token
```

### 2️⃣ Fazer Predição de Voo

**cURL:**
```bash
curl -X POST http://localhost:8080/api/flights/predict \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN" \
  -d '{
    "companhia": "G3",
    "origem": "SBGR",
    "destino": "SBBR",
    "data_partida": "2026-01-20T14:30:00"
  }'
```

**PowerShell:**
```powershell
$headers = @{
    "Content-Type" = "application/json"
    "Authorization" = "Bearer $token"
}
$body = @{
    companhia = "G3"
    origem = "SBGR"
    destino = "SBBR"
    data_partida = "2026-01-20T14:30:00"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/flights/predict" -Method POST -Headers $headers -Body $body
```

**JavaScript (Fetch):**
```javascript
const response = await fetch("http://localhost:8080/api/flights/predict", {
  method: "POST",
  headers: {
    "Content-Type": "application/json",
    "Authorization": "Bearer " + token
  },
  body: JSON.stringify({
    companhia: "G3",
    origem: "SBGR",
    destino: "SBBR",
    data_partida: "2026-01-20T14:30:00"
  })
});
const data = await response.json();
console.log(data);
```

**Python:**
```python
import requests

headers = {
    "Content-Type": "application/json",
    "Authorization": f"Bearer {token}"
}
data = {
    "companhia": "G3",
    "origem": "SBGR",
    "destino": "SBBR",
    "data_partida": "2026-01-20T14:30:00"
}

response = requests.post(
    "http://localhost:8080/api/flights/predict",
    headers=headers,
    json=data
)
print(response.json())
```

### 3️⃣ Consultar Estatísticas

```bash
curl -X GET http://localhost:8080/api/flights/stats \
  -H "Authorization: Bearer SEU_TOKEN"
```

### 4️⃣ Listar Voos com Atraso

```bash
curl -X GET http://localhost:8080/api/flights/search/delayed \
  -H "Authorization: Bearer SEU_TOKEN"
```

## ⚠️ Tratamento de Erros

### Validações de Formato

**Status:** `400 Bad Request`

```json
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Invalid request content.",
  "instance": "/api/flights/predict",
  "errors": {
    "companhia": "Código da companhia aérea deve conter apenas letras maiúsculas e números",
    "origem": "Código do aeroporto de origem deve conter apenas letras maiúsculas"
  }
}
```

### Validações de Negócio

**Status:** `400 Bad Request`

```json
{
  "message": "Companhia aérea inválida: XXX"
}
```

**Status:** `404 Not Found`

```json
{
  "message": "Aeroporto de origem não encontrado: XXXX"
}
```

### Erro de Autenticação

**Status:** `401 Unauthorized`

```json
{
  "message": "Token inválido ou expirado"
}
```

### API Python Indisponível

**Status:** `503 Service Unavailable`

```json
{
  "message": "Serviço de predição indisponível. Verifique se a API Python está rodando."
}
```

## 🗄️ Banco de Dados

### H2 Database (Desenvolvimento)

- **Tipo**: In-memory (dados são perdidos ao reiniciar)
- **Console Web**: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:h2:mem:flightdb`
- **Username**: `sa`
- **Password**: _(vazio)_

### Schema

**Entidades:**
- `airlines` - Companhias aéreas (G3, LA, AD, etc.)
- `airports` - Aeroportos com coordenadas geográficas
- `flights` - Histórico de predições
- `users` - Usuários cadastrados

**Dados Pré-carregados:**
- 7 companhias aéreas brasileiras
- 95 aeroportos brasileiros com coordenadas

## 🧪 Testes

### Executar Testes

```bash
# Todos os testes
.\mvnw.cmd test

# Gerar relatório de cobertura
.\mvnw.cmd clean test jacoco:report
```

### Cobertura de Testes

- **Total**: 39 testes automatizados
- **Services**: 58% de cobertura
  - WeatherService: 100%
  - PredictionService: 95%
  - FlightService: 45%
- **Controllers**: 36% de cobertura
- **Security**: 77% de cobertura

**Tipos de Testes:**
- ✅ Testes unitários (services)
- ✅ Testes de integração (API completa)
- ✅ Testes de validação (inputs inválidos)
- ✅ Testes de segurança (JWT)

## 📚 Documentação Adicional

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs
- **Arquitetura**: Ver [ARQUITETURA.md](ARQUITETURA.md)
- **Changelog**: Ver [CHANGELOG.md](CHANGELOG.md)
- **Status do Projeto**: Ver [STATUS.md](STATUS.md)

## 🔧 Configuração Avançada

### Variáveis de Ambiente

```properties
# Python API
PYTHON_API_URL=http://localhost:8000
PYTHON_API_TIMEOUT=30

# JWT
JWT_SECRET=seu_secret_minimo_32_caracteres

# Server
SERVER_PORT=8080

# Database (para produção com PostgreSQL)
DATABASE_URL=jdbc:postgresql://localhost:5432/flightontime
DATABASE_USERNAME=usuario
DATABASE_PASSWORD=senha
```

### application.properties

```properties
# Application
spring.application.name=Flight on Time
server.port=8080

# Python API Integration
python.api.url=${PYTHON_API_URL:http://localhost:8000}
python.api.timeout=30

# JWT Configuration
jwt.secret=${JWT_SECRET:mySecretKeyForJWTTokenGeneration12345678901234567890}
jwt.expiration=3600000

# Database H2 (Development)
spring.datasource.url=jdbc:h2:mem:flightdb
spring.datasource.driverClassName=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.sql.init.mode=always

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Error Handling
server.error.include-message=always
server.error.include-binding-errors=always
spring.mvc.problemdetails.enabled=true

# Logging
logging.level.com.flightontime.api=INFO
logging.level.org.springframework.web=INFO

# Actuator
management.endpoints.web.exposure.include=health,info
```

## 🐛 Troubleshooting

### Problema: "API Python não responde"

**Solução:**
1. Verifique se a API Python está rodando: `curl http://localhost:8000`
2. Verifique os logs do serviço Python
3. Confirme que a porta 8000 está livre: `netstat -ano | findstr :8000` (Windows)

### Problema: "Token JWT expirou"

**Solução:**
- Tokens expiram em 1 hora
- Faça login novamente para obter novo token

### Problema: "Companhia/Aeroporto não encontrado"

**Solução:**
- Verifique se o código está correto (2-3 chars para companhia, 4 chars para aeroporto)
- Consulte `/api/airlines` e `/api/airports` para ver códigos disponíveis
- Use códigos ICAO para aeroportos (SBGR, não GRU)

### Problema: "Rate limit exceeded"

**Solução:**
- Aguarde 1 minuto (limite: 100 req/min por IP)
- Para testes intensivos, desabilite o rate limit temporariamente

## 🏗️ Arquitetura

### Diagrama de Camadas

```
┌─────────────────────────────────────┐
│     PRESENTATION LAYER              │
│  Controllers + DTOs + Validation    │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│      BUSINESS LOGIC LAYER           │
│  Services + Business Rules          │