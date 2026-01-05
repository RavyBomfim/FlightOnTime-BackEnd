# Flight on Time - Backend API

API REST desenvolvida em Spring Boot para predição de atrasos de voos. Este projeto faz parte do hackaton da Oracle Next Education (ONE) e fornece endpoints para análise preditiva de pontualidade de voos.

## 🚀 Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 4.0.1**
- **Maven** - Gerenciamento de dependências
- **Lombok** - Redução de boilerplate
- **Spring Validation** - Validação de dados
- **Spring DevTools** - Hot reload durante desenvolvimento
- **Spring Actuator** - Monitoramento da aplicação

## 📋 Pré-requisitos

- Java 21 ou superior
- Maven 3.6+
- **API Python de Predição** rodando na porta 8000

## ⚠️ IMPORTANTE: Configurar API Python

Este backend depende da API Python de Machine Learning para funcionar. Antes de iniciar o backend, você **DEVE** configurar e rodar a API Python:

### 1. Clone o repositório da API Python

```bash
git clone https://github.com/RavyBomfim/FlightOnTime-DataScience.git
cd FlightOnTime-DataScience/API
```

### 2. Siga as instruções do README do projeto Python

Acesse o README do projeto Python e siga as instruções para:

- Instalar as dependências Python
- Configurar o ambiente
- Iniciar o servidor na porta 8000

**Link do projeto:** https://github.com/RavyBomfim/FlightOnTime-DataScience/tree/main/API

### 3. Verifique se a API Python está rodando

```bash
# Teste se a API Python está respondendo
curl http://localhost:8000
```

Somente após a API Python estar rodando, prossiga com a instalação do backend Java abaixo.

## 🔧 Instalação e Execução

#### 1. Certifique-se que a API Python está rodando

```bash
# Navegue até o diretório da API Python
cd FlightOnTime-DataScience/API

# Siga o README do projeto Python para iniciar o servidor
# A API deve estar rodando em http://localhost:8000
```

#### 2. Clone o repositório do Backend

```bash
git clone https://github.com/RavyBomfim/FlightOnTime-BackEnd.git
cd FlightOnTime-BackEnd
```

#### 3. Compile o projeto

**Windows (PowerShell):**

```powershell
.\mvnw.cmd clean install
```

**Linux/Mac:**

```bash
./mvnw clean install
```

#### 4. Execute a aplicação

**Windows (PowerShell):**

```powershell
.\mvnw.cmd spring-boot:run
```

**Linux/Mac:**

```bash
./mvnw spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

**Nota:** Certifique-se que a API Python está rodando em `http://localhost:8000` antes de iniciar o backend.

## � Autenticação

Esta API utiliza autenticação JWT (JSON Web Token). Para acessar os endpoints protegidos, você precisa:

1. **Registrar um usuário** (endpoint público)
2. **Fazer login** para obter o token JWT (endpoint público)
3. **Usar o token** no header `Authorization` para acessar os endpoints protegidos

### Endpoints Públicos (Sem Autenticação)

- `POST /api/auth/register` - Registrar novo usuário
- `POST /api/auth/login` - Fazer login e obter token JWT
- `GET /h2-console/**` - Console do banco de dados H2 (apenas desenvolvimento)

### Endpoints Protegidos (Requerem JWT)

- Todos os endpoints `/api/flights/**` requerem autenticação

### 1. Registrar Usuário

**Endpoint:** `POST /api/auth/register`

**Request:**
```json
{
  "email": "seu@email.com",
  "password": "suaSenha123"
}
```

**Response:** `200 OK`
```json
"Usuário registrado com sucesso!"
```

### 2. Fazer Login

**Endpoint:** `POST /api/auth/login`

**Request:**
```json
{
  "email": "seu@email.com",
  "password": "suaSenha123"
}
```

**Response:** `200 OK`
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Nota:** Copie o token retornado para usar nas próximas requisições.

### 3. Usar o Token nas Requisições

Adicione o token no header `Authorization` com o prefixo `Bearer`:

```bash
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Exemplo com cURL:**
```bash
curl -X POST http://localhost:8080/api/flights/predict \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -d '{
    "airline": "GOL",
    "originIcao": "SBGR",
    "destinationIcao": "SBBR",
    "scheduledDeparture": "2026-01-15T14:30:00"
  }'
```

**Exemplo com PowerShell:**
```powershell
$headers = @{
    "Content-Type" = "application/json"
    "Authorization" = "Bearer SEU_TOKEN_AQUI"
}

Invoke-RestMethod -Uri "http://localhost:8080/api/flights/predict" `
    -Method POST `
    -Headers $headers `
    -Body '{"airline":"GOL","originIcao":"SBGR","destinationIcao":"SBBR","scheduledDeparture":"2026-01-15T14:30:00"}'
```

### Segurança

- Tokens JWT expiram em **1 hora**
- Senhas são armazenadas com **BCrypt** (hash seguro)
- Rate limiting: **10 requisições por minuto por IP**
- CORS configurado para permitir origens específicas

## �📡 Endpoints da API

### POST /api/flights/predict

**⚠️ Requer autenticação JWT**

Realiza a predição de atraso de um voo com base nos dados fornecidos.

### GET /api/flights

**⚠️ Requer autenticação JWT**

Retorna a lista de todos os voos cadastrados no banco de dados, ordenados do mais recente para o mais antigo.

**URL:** `http://localhost:8080/api/flights`

**Method:** `GET`

**Headers:**
```
Authorization: Bearer {seu_token_jwt}
```

**Response:** `200 OK`

```json
[
  {
    "id": 10,
    "airline": "Azul",
    "origin": "SSA",
    "destination": "GRU",
    "distanceKm": 1960,
    "scheduledDeparture": "2025-12-26T06:00:00",
    "scheduledArrival": "2025-12-26T08:40:00",
    "predictionResult": "Pontual",
    "predictionProbability": 0.81,
    "createdAt": "2025-12-22T16:22:42.876429",
    "updatedAt": "2025-12-22T16:22:42.876429"
  }
]
```

### GET /api/flights/{id}

Busca um voo específico pelo ID.

**URL:** `http://localhost:8080/api/flights/1`

**Method:** `GET`

**Response:** `200 OK`

### GET /api/flights/search/origin?origin={code}

Busca voos por aeroporto de origem (ex: GRU, CGH, SDU).

**URL:** `http://localhost:8080/api/flights/search/origin?origin=GRU`

**Method:** `GET`

### GET /api/flights/search/destination?destination={code}

Busca voos por aeroporto de destino.

**URL:** `http://localhost:8080/api/flights/search/destination?destination=BSB`

**Method:** `GET`

### GET /api/flights/search/ontime

Retorna todos os voos com predição de pontualidade (ontime).

**URL:** `http://localhost:8080/api/flights/search/ontime`

**Method:** `GET`

### GET /api/flights/search/route?origin={code}&destination={code}

Busca voos por rota específica (origem e destino).

**URL:** `http://localhost:8080/api/flights/search/route?origin=GRU&destination=CGH`

**Method:** `GET`

### GET /api/flights/search/delayed

Retorna todos os voos com predição de atraso.

**URL:** `http://localhost:8080/api/flights/search/delayed`

**Method:** `GET`

### DELETE /api/flights/{id}

Remove um voo do banco de dados.

**URL:** `http://localhost:8080/api/flights/1`

**Method:** `DELETE`

**Response:** `204 No Content`

---

### POST /api/flights/predict - Detalhes

**⚠️ Requer autenticação JWT**

Realiza a predição de atraso de um voo com base nos dados fornecidos.

#### Request

**URL:** `http://localhost:8080/api/flights/predict`

**Method:** `POST`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {seu_token_jwt}
```

**Body:**

```json
{
  "airline": "GOL",
  "originIcao": "SBGR",
  "destinationIcao": "SBBR",
  "scheduledDeparture": "2026-01-15T14:30:00"
}
```

#### Parâmetros

| Campo                  | Tipo     | Obrigatório | Descrição                                                          |
| ---------------------- | -------- | ----------- | ------------------------------------------------------------------ |
| `airline`              | String   | Sim         | Código da companhia aérea (3 caracteres). Ex: GOL, LATAM, Azul    |
| `originIcao`           | String   | Sim         | Código ICAO do aeroporto de origem (4 caracteres). Ex: SBGR, SBBR  |
| `destinationIcao`      | String   | Sim         | Código ICAO do aeroporto de destino (4 caracteres). Ex: SBSP, SBGL |
| `scheduledDeparture`   | DateTime | Sim         | Data e hora de partida (formato ISO 8601)                          |

**Nota:** A distância entre aeroportos é calculada automaticamente usando a fórmula de Haversine com base nas coordenadas geográficas.

#### Response

**Status:** `200 OK`

**Body:**

```json
{
  "predict": {
    "previsao": true,
    "probabilidade": 0.78
  },
  "weather": {
    "temperatura": "25.5°C",
    "precipitacao": "0.0mm",
    "vento": "12.5 km/h"
  }
}
```

**Campos de resposta:**

| Campo                   | Tipo    | Descrição                                  |
| ----------------------- | ------- | ------------------------------------------ |
| `predict.previsao`      | Boolean | true = Atrasado, false = Pontual           |
| `predict.probabilidade` | Double  | Probabilidade de atraso (0.0 a 1.0)        |
| `weather.temperatura`   | String  | Temperatura no aeroporto de origem         |
| `weather.precipitacao`  | String  | Precipitação no aeroporto de origem        |
| `weather.vento`         | String  | Velocidade do vento no aeroporto de origem |

**Validações realizadas:**

- Verifica se o código da companhia aérea existe no banco de dados
- Verifica se o código do aeroporto de origem existe no banco de dados
- Verifica se o código do aeroporto de destino existe no banco de dados
- Calcula automaticamente a distância entre os aeroportos usando Haversine
- Busca dados meteorológicos para o aeroporto de origem

## 🧪 Exemplos de Chamadas

### 1. Registrar Usuário

**cURL:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "teste@email.com",
    "password": "senha123"
  }'
```

**PowerShell:**
```powershell
$body = @{
    email = "teste@email.com"
    password = "senha123"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/auth/register" `
  -Method POST `
  -ContentType "application/json" `
  -Body $body
```

### 2. Fazer Login e Obter Token

**cURL:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "teste@email.com",
    "password": "senha123"
  }'
```

**PowerShell:**
```powershell
$body = @{
    email = "teste@email.com"
    password = "senha123"
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" `
  -Method POST `
  -ContentType "application/json" `
  -Body $body

# Salvar o token para usar nas próximas requisições
$token = $response.token
Write-Host "Token: $token"
```

### 3. Fazer Predição de Voo (Com Token)

**Voo com alta probabilidade de atraso:**

**cURL:**
```bash
# Substitua SEU_TOKEN_AQUI pelo token recebido no login
curl -X POST http://localhost:8080/api/flights/predict \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -d '{
    "airline": "GOL",
    "originIcao": "SBGL",
    "destinationIcao": "SBGR",
    "scheduledDeparture": "2026-01-20T18:00:00"
  }'
```

**PowerShell:**
```bash
# Usando o token salvo anteriormente
$headers = @{
    "Content-Type" = "application/json"
    "Authorization" = "Bearer $token"
}

$body = @{
    airline = "GOL"
    originIcao = "SBGL"
    destinationIcao = "SBGR"
    scheduledDeparture = "2026-01-20T18:00:00"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/flights/predict" `
  -Method POST `
  -Headers $headers `
  -Body $body
```

**Voo com baixa probabilidade de atraso:**

**cURL:**
```bash
curl -X POST http://localhost:8080/api/flights/predict \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -d '{
    "airline": "Azul",
    "originIcao": "SBGR",
    "destinationIcao": "SBSP",
    "scheduledDeparture": "2026-01-18T08:30:00"
  }'
```

### Usando JavaScript (Fetch API)

```javascript
fetch("http://localhost:8080/api/flights/predict", {
  method: "POST",
  headers: {
    "Content-Type": "application/json",
  },
  body: JSON.stringify({
    companhia: "AZU",
    origem: "SBGR",
    destino: "SBRF",
    data_partida: "2025-12-30T11:20:00",
  }),
})
  .then((response) => response.json())
  .then((data) => console.log(data))
  .catch((error) => console.error("Erro:", error));
```

### Usando Python (requests)

```python
import requests
import json

url = "http://localhost:8080/api/flights/predict"
headers = {"Content-Type": "application/json"}
data = {
    "companhia": "TAM",
    "origem": "SBGR",
    "destino": "SBFZ",
    "data_partida": "2025-12-22T13:15:00"
}

response = requests.post(url, headers=headers, data=json.dumps(data))
print(response.json())
```

## ⚠️ Validações e Erros

### Erros de Validação

A API valida todos os campos de entrada. Em caso de erro, retorna:

**Status:** `400 Bad Request`

**Exemplo de erro:**

```json
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Invalid request content.",
  "instance": "/api/flights/predict",
  "errors": {
    "companhia": "O nome da companhia aérea deve ter 3 caracteres",
    "origem": "O código do aeroporto de origem deve ter 4 caracteres"
  }
}
```

### Possíveis Mensagens de Validação

**Validações de formato:**

- **companhia:** "A companhia aérea é obrigatória" ou "O nome da companhia aérea deve ter 3 caracteres"
- **origem:** "O aeroporto de origem é obrigatório" ou "O código do aeroporto de origem deve ter 4 caracteres"
- **destino:** "O aeroporto de destino é obrigatório" ou "O código do aeroporto de destino deve ter 4 caracteres"
- **data_partida:** "A data de partida é obrigatória"

**Validações de existência (Runtime):**

- **Companhia aérea inválida:** "Companhia aérea inválida: XXX" (quando o código não existe no banco)
- **Aeroporto de origem inválido:** "Aeroporto de origem não encontrado: XXXX" (quando o código não existe no banco)
- **Aeroporto de destino inválido:** "Aeroporto de destino não encontrado: XXXX" (quando o código não existe no banco)

## 🔍 Como Funciona

### Arquitetura

O projeto segue uma arquitetura em camadas:

```
Controller (FlightController)
    ↓
Service (PredictionService)
    ↓
Integration (PredictionClient / WeatherService)
    ↓
Repository (FlightRepository / AirportRepository / AirlineRepository)
    ↓
DTOs (FlightRequestDTO / FlightResponseDTO / PredictionDTO / WeatherDTO)
```

### Fluxo de Predição

1. **Validação de Entrada:** Valida formato dos códigos (3 caracteres para companhia, 4 para aeroportos)
2. **Validação de Existência:** Verifica se companhia aérea e aeroportos existem no banco de dados
3. **Cálculo de Distância:** Usa fórmula de Haversine para calcular distância entre aeroportos
4. **Chamada à API Python:** Envia dados para o modelo de Machine Learning
5. **Busca de Dados Meteorológicos:** Obtém condições climáticas do aeroporto de origem
6. **Persistência:** Salva a predição no banco de dados
7. **Resposta:** Retorna predição e dados meteorológicos ao cliente

### Integração com Machine Learning

A aplicação integra com uma API Python (FastAPI) que executa o modelo de Machine Learning treinado:

- Utiliza RestClient para comunicação HTTP
- Envia: companhia, origem, destino, data, dia da semana e distância
- Recebe: predição (boolean) e probabilidade (double)

### CORS

A aplicação está configurada para aceitar requisições das seguintes origens:

- `http://localhost:3000` (React - Create React App)
- `http://localhost:5173` (Vite)

Métodos permitidos: `GET`, `POST`, `PUT`, `DELETE`, `OPTIONS`

## 🛠️ Configuração

### application.properties

```properties
spring.application.name=Flight on Time
server.port=8080
server.error.include-message=always
server.error.include-binding-errors=always
spring.mvc.problemdetails.enabled=true

# Python API Configuration
python.api.url=http://localhost:8000
python.api.timeout=30
```

### Variáveis de Ambiente (Docker)

Ao executar com Docker, você pode configurar as seguintes variáveis:

| Variável             | Padrão                  | Descrição                               |
| -------------------- | ----------------------- | --------------------------------------- |
| `PYTHON_API_URL`     | `http://localhost:8000` | URL da API Python de predição           |
| `PYTHON_API_TIMEOUT` | `30`                    | Timeout em segundos para chamadas à API |
| `JAVA_OPTS`          | `-Xmx512m -Xms256m`     | Opções da JVM (memória, GC, etc.)       |

**Exemplo de uso:**

```bash
docker run -d \
  -p 8080:8080 \
  -e PYTHON_API_URL=http://python-api:8000 \
  -e PYTHON_API_TIMEOUT=60 \
  -e JAVA_OPTS="-Xmx1g -Xms512m" \
  flightontime-backend
```

### Alterar a Porta

Para executar em outra porta, modifique o arquivo `src/main/resources/application.properties`:

```properties
server.port=9090
```

## 📊 Monitoramento

O Spring Actuator está habilitado. Endpoints de monitoramento disponíveis:

- **Health Check:** `http://localhost:8080/actuator/health`
- **Info:** `http://localhost:8080/actuator/info`

## 📚 Documentação da API

A documentação interativa da API está disponível através do Swagger UI:

- **Swagger UI:** `http://localhost:8080/swagger-ui/index.html`
- **OpenAPI JSON:** `http://localhost:8080/api-docs`

No Swagger UI você pode:

- Visualizar todos os endpoints disponíveis
- Testar as requisições diretamente no navegador
- Ver exemplos de request e response
- Consultar os schemas dos DTOs

## 🏗️ Estrutura do Projeto

```
src/main/java/com/flightontime/api/
├── FlightOnTimeApplication.java    # Classe principal
├── config/
│   ├── CorsConfig.java             # Configuração de CORS
│   └── RestClientConfig.java       # Configuração do RestClient
├── controller/
│   └── FlightController.java       # Controlador REST
├── dto/
│   ├── FlightRequestDTO.java       # DTO de requisição
│   └── FlightResponseDTO.java      # DTO de resposta
└── service/
    └── PredictionService.java      # Lógica de predição e integração com Python
```

### Health Check

O container inclui health check automático que verifica o endpoint `/actuator/health` a cada 30 segundos.

## 🤝 Contribuindo

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/NovaFuncionalidade`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova funcionalidade'`)
4. Push para a branch (`git push origin feature/NovaFuncionalidade`)
5. Abra um Pull Request

## 📝 Licença

Este projeto foi desenvolvido para o hackaton da Oracle Next Education (ONE).
